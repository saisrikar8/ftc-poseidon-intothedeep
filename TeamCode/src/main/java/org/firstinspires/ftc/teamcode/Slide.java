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
    public Slide(HardwareMap map) {
        motor = map.get(DcMotor.class, "slide-motor-1-left");
    }
    // class which has the run logic
    public class TestMoveABit implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                motor.setPower(0.4);
                initialized = true;
            }
            double motorPosition = motor.getCurrentPosition();
            double motorPower = motor.getPower();
            telemetryPacket.put("slideMotorPosition", motorPosition);
            telemetryPacket.put("slideMotorPower", motorPower);
            // terminiating if condition, true keeps running, false causes stop
            return motorPosition < 0.5;
        }
    }
    // method is called for class
    public Action testMoveABit() {
        return new TestMoveABit();
    }
}

@Autonomous(name = "something")
class SlideOpMode extends LinearOpMode {
    // op mode for slide
    @Override
    public void runOpMode() throws InterruptedException {

    }
}