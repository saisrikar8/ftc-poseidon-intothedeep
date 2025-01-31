package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Claw Testing")
public class ClawTesting extends LinearOpMode {
    Servo horizontalClaw, horizontalClawRotator, horizontalClawRotator2;
    Servo verticalClaw, verticalClawRotator;

    @Override
    public void runOpMode() throws InterruptedException {
        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator");
        horizontalClawRotator2 = hardwareMap.get(Servo.class, "horizontal-claw-rotator-2");
        verticalClaw = hardwareMap.get(Servo.class, "vertical-claw");
        verticalClawRotator = hardwareMap.get(Servo.class, "vertical-claw-rotator");


        waitForStart();
        while (opModeIsActive() && !isStopRequested()){
            horizontalClaw.setPosition(filterPosition(horizontalClaw.getPosition()+gamepad1.right_stick_x/1000));
            horizontalClawRotator.setPosition(filterPosition(horizontalClawRotator.getPosition()+gamepad1.left_stick_y/1000));
            horizontalClawRotator2.setPosition(filterPosition(horizontalClawRotator2.getPosition() + gamepad1.left_stick_x/1000));
            verticalClaw.setPosition(filterPosition(verticalClaw.getPosition() + gamepad2.right_stick_x/1000));
            verticalClawRotator.setPosition(filterPosition(verticalClawRotator.getPosition() + gamepad2.left_stick_y/1000));
            updateTelemetry();
        }
    }

    public double filterPosition(double position){
        return Math.max(0, Math.min(1, position));
    }
    public void updateTelemetry(){
        telemetry.addData("horizontal claw pos", horizontalClaw.getPosition());
        telemetry.addData("horizontal claw pitch pos", horizontalClawRotator.getPosition());
        telemetry.addData("horizontal claw yaw pos", horizontalClawRotator2.getPosition());
        telemetry.addData("vertical claw pos", verticalClaw.getPosition());
        telemetry.addData("vertical claw pitch pos", verticalClawRotator.getPosition());
        telemetry.update();

    }
}
