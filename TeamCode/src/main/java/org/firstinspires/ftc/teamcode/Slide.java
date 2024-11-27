package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slide {

    private DcMotor motor;

    // initialize hardware map
    private double targetPosition = 0;
    public final double startingPosition;
    public Slide(HardwareMap map, String motorName) {
        motor = map.get(DcMotor.class, motorName);
        startingPosition = motor.getCurrentPosition();
    }
    public Slide(DcMotor slideMotor){motor = slideMotor; startingPosition = slideMotor.getCurrentPosition();}
    // class which has the run logic
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
            // terminiating if condition, true keeps running, false causes stop
            return false;
        }
    }
    public class MoveToPosition implements Action{
        private boolean init = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket){
            if (!init){
                init = true;
                motor.setTargetPosition((int)targetPosition);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (motor.isBusy()){
                if (motor.getCurrentPosition() + 50 >= targetPosition){
                    motor.setPower(0.33);
                    return true;
                }
                motor.setPower(0.66);
                return true;
            }
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            return false;
        }
    }
    // method is called for class
    public Action testMoveABit() {
        return new TestMoveABit();
    }

    public Action moveToPosition(double targetPosition){
        this.targetPosition = startingPosition - targetPosition*Constants.SLIDE_TICKS_PER_INCH;
        return new MoveToPosition();
    }
    public Action moveToHighestPos(){
        this.targetPosition = startingPosition + Constants.MAX_SLIDE_EXTENSION;
        return new MoveToPosition();
    }
    public Action moveToLowestPos() {
        this.targetPosition = startingPosition;
        return new MoveToPosition();
    }
}

