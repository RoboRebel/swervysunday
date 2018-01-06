package org.usfirst.frc.team1329.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1329.robot.OI;
import org.usfirst.frc.team1329.robot.Robot;

public class DriveCommand extends Command {

    private Joystick gamepad;
    private double x,y;
    private double[] values = {0.0,0.0};
    //^^ speed and direction ^^

    public DriveCommand(){
        super("SwerveDriveCommand");
        this.requires(Robot.swerveDrive);

        gamepad = Robot.oi.getJoystick();
    }

    @Override
    protected void execute() {
        x = gamepad.getRawAxis(OI.LEFT_STICK_X_AXIS);
        y = gamepad.getRawAxis(OI.LEFT_STICK_Y_AXIS);
        if(Math.abs(x)+Math.abs(y) >= .2){
        	values[0] = Math.abs((x*x)+(y*y))/5.0;
        	values[1] = Math.toDegrees(Math.atan2(y,x));
        	values[1] += (values[1]>0.0) ? 0.0 : 360.0;
        	//        System.out.println(values[1]);
        	Robot.swerveDrive.drive(values[0],values[1]);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}