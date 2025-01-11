package org.firstinspires.ftc.teamcode;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
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

    public Action moveToPosition(int oneSlideTargetPos) {
        return new MoveToPosition(oneSlideTargetPos);
//        return new ParallelAction(
//                right.moveToPosition(targetPos),
//                left.moveToPosition(targetPos)
//        );
    }

    public class MoveToPosition implements Action {
        int targetPos;

        public MoveToPosition(int targetPos) {
            this.targetPos = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            int currentPosition = left.getCurrentPosition();
            left.setMotorPower(0.2);
            right.setMotorPower(0.2);
            // stop when current position is within upper and lower bound
            return (targetPos - 1000 < currentPosition && currentPosition < targetPos + 1000);
        }
    }

    public Action stayAtRest(Gamepad gamepad1) {
        return new ParallelAction(
                right.stayAtRest(gamepad1),
                left.stayAtRest(gamepad1)
        );
    }

    public Action moveToHighestPosition() {
        return new ParallelAction(
                right.moveToFourStageHighestPos(),
                left.moveToFourStageHighestPos()
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

    public Action motorsStayAtRest(Gamepad gamepad) {
        return new ParallelAction(left.stayAtRest(gamepad), right.stayAtRest(gamepad));
    }
}
