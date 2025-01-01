package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HorizontalArm {
    Slide left, right;
    public HorizontalArm(Slide l, Slide r, Servo lRotator, Servo rRotator){
        left = l;
        right = r;
    }
    public HorizontalArm(DcMotor l, DcMotor r){
        left = new Slide(l);
        right = new Slide(r);

    }
    public HorizontalArm(HardwareMap hardwareMap, String lId, String rId, String lRotatorId, String rRotatorId){
        left = new Slide(hardwareMap, lId);
        right = new Slide(hardwareMap, rId);

    }
    public Action moveToPosition(int targetPos){
        return new ParallelAction(
                right.moveToPosition(targetPos),
                left.moveToPosition(targetPos)
        );
    }
    public Action stayAtRest(Gamepad gamepad1){
        return new ParallelAction(
                right.stayAtRest(gamepad1),
                left.stayAtRest(gamepad1)
        );
    }
    public Action moveToHighestPosition(){
        return new ParallelAction(
                right.moveToTwoStageHighestPos(),
                left.moveToTwoStageHighestPos()
        );
    }
    public Action moveToLowestPosition(){
        return new ParallelAction(
                right.moveToLowestPos(),
                left.moveToLowestPos()
        );
    }
    public Action setMotorPowers(double power){
        return new ParallelAction(
                right.setMotorPower(power),
                left.setMotorPower(power)
        );
    }

}
