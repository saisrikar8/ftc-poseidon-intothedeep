package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Claw Testing")
public class ClawTesting extends LinearOpMode {
    Servo horizontalClaw, horizontalClawRotator, horizontalClawRotator2;

    @Override
    public void runOpMode() throws InterruptedException {
        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator");
        horizontalClawRotator2 = hardwareMap.get(Servo.class, "horizontal-claw-rotator-2");

        waitForStart();
        while (opModeIsActive() && !isStopRequested()){
            horizontalClaw.setPosition(filterPosition(horizontalClaw.getPosition()+gamepad1.right_stick_x/1000));
            horizontalClawRotator.setPosition(filterPosition(horizontalClawRotator.getPosition()+gamepad1.left_stick_y/1000));
            horizontalClawRotator2.setPosition(filterPosition(horizontalClawRotator2.getPosition() + gamepad1.left_stick_x/1000));
        }
    }

    public double filterPosition(double position){
        return Math.max(0, Math.min(1, position));
    }
}
