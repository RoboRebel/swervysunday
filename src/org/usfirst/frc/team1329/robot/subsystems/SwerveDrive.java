package org.usfirst.frc.team1329.robot.subsystems;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1329.robot.commands.DriveCommand;

import java.util.ArrayList;

public class SwerveDrive extends Subsystem {
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
        private final CANTalon rotTalon;
        private final CANTalon driveTalon;
        private final Encoder encoder;
        public SwerveModule(int rotID, int driveID, int[] encoderChannels){
            rotTalon = new CANTalon(rotID);
            driveTalon = new CANTalon(driveID);
            encoder = new Encoder(encoderChannels[0],encoderChannels[1]);
        }
        public void drive(double speed,double angle){
            driveTalon.set(speed);
        }
    }
}
