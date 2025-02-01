package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slide {

    private DcMotor motor;
    public double specificPower;

    private Gamepad gamepad;
    private double targetPower;

    //
    private double targetPosition = 0;
    public final double startingPosition;

    public Slide(HardwareMap map, String motorName) {
        motor = map.get(DcMotor.class, motorName);
        startingPosition = motor.getCurrentPosition();
    }

    public Slide(DcMotor slideMotor) {
        motor = slideMotor;
        startingPosition = motor.getCurrentPosition();
    }
    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    public class TestMoveABit implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                motor.setPower(-0.4);
                initialized = true;
            }
            double motorPosition = motor.getCurrentPosition();
            double motorPower = motor.getPower();
            telemetryPacket.put("slideMotorPosition", motorPosition);
            telemetryPacket.put("slideMotorPower", motorPower);
            return false;
        }
    }

    public class MoveToPosition implements Action {
        private boolean init = false;
        private int tPosition;
        public MoveToPosition(int targetPos){
            tPosition = targetPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!init) {
                init = true;
                motor.setTargetPosition(tPosition);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (motor.isBusy()) {
                if (motor.getCurrentPosition() - 50 >= tPosition) {
                    motor.setPower(0.4);
                    return true;
                }
                motor.setPower(0.75);
                return true;
            }
            motor.setPower(0);
            return false;
        }
    }

    public class StayAtRest implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (motor.getPower() == 0) {
                motor.setTargetPosition(motor.getCurrentPosition());
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (gamepad.atRest() && !(gamepad.a || gamepad.b || gamepad.x || gamepad.y)) {
                if (motor.isBusy()){
                    motor.setPower(0.08);
                }
                else{
                    motor.setPower(0);
                }
                return true;
            }
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            return false;
        }
    }

    public class SetMotorPower implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            motor.setPower(targetPower);
            return false;
        }
    }

    public Action moveToPosition(double targetPosition) {
        return new MoveToPosition((int)(startingPosition - targetPosition * Constants.SLIDE_TICKS_PER_INCH));
    }

    public Action moveToFourStageHighestPos() {
        return new MoveToPosition((int)(startingPosition + Constants.MAX_SLIDE_EXTENSION));
    }

    public Action moveToTwoStageHighestPos() {
        return new MoveToPosition((int)(startingPosition + Constants.MAX_TWO_STAGE_EXTENSION));
    }

    public Action moveToLowestPos() {
        return new MoveToPosition((int)(startingPosition));
    }

    public Action stayAtRest(Gamepad gamepad1) {
        gamepad = gamepad1;
        return new StayAtRest();
    }

    public Action setMotorPower(double power) {
        targetPower = power;
        return new SetMotorPower();
    }
}

