package org.usfirst.frc.team1329.robot.subsystems;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1329.robot.commands.DriveCommand;

import java.util.ArrayList;

public class SwerveDrive extends Subsystem {
	private static final double PULSES_PER_REV = 414.0;
    private static final double TOLERANCE = 5.0;
    private SwerveModule topLeft, topRight, backLeft, backRight;
    private ArrayList<SwerveModule> swerveModules;

    //first is top left, second is top right, third is back left, fourth is back right
    private int[] rotIDS = {5, 6, 7, 8};
    private int[] driveIDS = {1, 2, 3, 4};
    //encoders take two channels
    private int[][] encoderChannels = {{0,1},{2,3},{4,5},{6,7}};

    public SwerveDrive(){
        swerveModules = new ArrayList<>();

        topLeft = new SwerveModule(rotIDS[0],driveIDS[0],encoderChannels[0]);
        swerveModules.add(topLeft);

        topRight = new SwerveModule(rotIDS[1],driveIDS[1],encoderChannels[1]);
        swerveModules.add(topRight);

        backLeft = new SwerveModule(rotIDS[2],driveIDS[2],encoderChannels[2]);
        swerveModules.add(backLeft);

        backRight = new SwerveModule(rotIDS[3],driveIDS[3],encoderChannels[3]);
        swerveModules.add(backRight);
    }
    public void drive(double speed,double direction){
        for(SwerveModule swerveModule:swerveModules){
            swerveModule.drive(speed, direction);
        }
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }
    private class SwerveModule {
        private final int number;
        private final CANTalon rotTalon;
        private final CANTalon driveTalon;
        private final Encoder encoder;
        private final PIDController pidController;
        public SwerveModule(int rotID, int driveID, int[] encoderChannels){
            number = driveID;
            rotTalon = new CANTalon(rotID);
//            rotTalon.setControlMode(CANTalon.TalonControlMode.Voltage.value);
            driveTalon = new CANTalon(driveID);
            driveTalon.setEncPosition(0);
            encoder = new Encoder(encoderChannels[0],encoderChannels[1],false, CounterBase.EncodingType.k4X);
            encoder.reset();
            encoder.setDistancePerPulse(360.0/PULSES_PER_REV);
//            pidController = new PIDController(1.0,0,0,new CorrectedEncoder(encoder),rotTalon);
            pidController = new PIDController(1.0,0,0,new CorrectedEncoder(encoder),new PIDTESTOUT());
            pidController.setInputRange(0.0,360.0);
//            pidController.setOutputRange(-1.0,1.0);
            pidController.setContinuous();
            pidController.enable();
        }
        public void drive(double speed,double angle){
//            driveTalon.set(speed);
            pidController.setSetpoint(angle);
        }
        private class CorrectedEncoder implements PIDSource{
            private final Encoder encoder;
            public CorrectedEncoder(Encoder encoder){
                this.encoder = encoder;
            }

            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return PIDSourceType.kDisplacement;
            }

            @Override
            public double pidGet() {
                double encoderDistance = encoder.getDistance();
//                System.out.println(encoderDistance);
                while(encoderDistance > 360.0 || encoderDistance < 0.0){
                    if(encoderDistance > 360.0){
                        encoderDistance -= 360.0;
                    }else{
                        encoderDistance += 360.0;
                    }
                }
                return encoderDistance;
            }
        }
        private class PIDTESTOUT implements PIDOutput{

			@Override
			public void pidWrite(double output) {
				// TODO Auto-generated method stub
				System.out.println("pid" + output);
				
			}
        	
        }
    }
}
