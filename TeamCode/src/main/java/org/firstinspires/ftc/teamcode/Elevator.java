package org.firstinspires.ftc.teamcode;


import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Elevator {
    Slide right, left;

    public Elevator(Slide r, Slide l) {
        left = l;
        right = r;
    }

    public Elevator(DcMotor r, DcMotor l) {
        left = new Slide(l);
        right = new Slide(r);
    }

    public Elevator(HardwareMap hardwareMap, String rId, String lId) {
        left = new Slide(hardwareMap, lId);
        right = new Slide(hardwareMap, rId);
    }

    public Action moveToPosition(int targetPos) {
        return new ParallelAction(
                right.moveToPosition(targetPos),
                left.moveToPosition(targetPos)
        );
    }

    public Action stayAtRest(Gamepad gamepad1) {
        return new ParallelAction(
                right.stayAtRest(gamepad1),
                left.stayAtRest(gamepad1)
        );
    }

    public Action moveToHighestPosition() {
        return new ParallelAction(
                left.moveToFourStageHighestPos(),
                right.moveToFourStageHighestPos()
        );
    }

    public Action moveToLowestPosition() {
        return new ParallelAction(
                right.moveToLowestPos(),
                left.moveToLowestPos()
        );
    }

    public Action setMotorPowers(double rPower) {
        return new ParallelAction(
                right.setMotorPower(rPower),
                left.setMotorPower(0.83 * rPower)
        );
    }
}
