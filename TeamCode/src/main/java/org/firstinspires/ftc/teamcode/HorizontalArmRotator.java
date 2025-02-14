package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

public class HorizontalArmRotator {
    public Slide left, right;
    public Servo rotator1, rotator2;

    public HorizontalArmRotator(DcMotor l, DcMotor r, Servo rotatorServo, Servo rotatorServo2) {
        left = new Slide(l);
        right = new Slide(r);
        rotator1 = rotatorServo;
        rotator2 = rotatorServo2;
    }
    public HorizontalArmRotator(DcMotor l, DcMotor r) {
        left = new Slide(l);
        right = new Slide(r);
    }

    public class SetOrientation implements Action {
        private final double orientation;

        public SetOrientation(double degrees) {
            orientation = degrees / 300;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            rotator1.setPosition(1 - orientation);
            rotator2.setPosition(1 - orientation);
            return false;
        }
    }

    public Action setOrientation(double degrees) {
        return new SetOrientation(degrees);
    }

    public Action moveToPosition(double targetPos) {
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
                right.moveToTwoStageHighestPos(),
                left.moveToTwoStageHighestPos()
        );
    }

    public Action moveToLowestPosition() {
        return new ParallelAction(
                right.moveToLowestPos(),
                left.moveToLowestPos()
        );
    }


    public Action setMotorPowers(double power) {
        return new ParallelAction(
                right.setMotorPower(power),
                left.setMotorPower(power)
        );
    }

}
