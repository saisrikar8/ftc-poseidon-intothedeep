package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name = "Claw Testing")
public class ClawTesting extends LinearOpMode {
    Servo horizontalClaw, horizontalClawRotator, horizontalClawRotator2, armRotator;

    @Override
    public void runOpMode() throws InterruptedException {
        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw"); // open claw
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator"); // pitch
        horizontalClawRotator2 = hardwareMap.get(Servo.class, "horizontal-claw-rotator-2"); // yaw
        armRotator = hardwareMap.get(Servo.class, "arm-rotator");

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            if (gamepad2.a) {
                horizontalClaw.setPosition(Constants.HORIZONTAL_CLAW_OPEN_POS);
            }
            if (gamepad2.b) {
                horizontalClaw.setPosition(Constants.HORIZONTAL_CLAW_CLOSE_POS);
            }
            if (gamepad2.x) {
                horizontalClawRotator2.setPosition(Constants.HORIZONTAL_CLAW_IDLE_YAW);
                horizontalClawRotator.setPosition(Constants.HORIZONTAL_CLAW_PICKUP_POS_PITCH);
            }
            if (gamepad2.y) {
                horizontalClawRotator2.setPosition(Constants.HORIZONTAL_CLAW_IDLE_YAW);
                horizontalClawRotator.setPosition(Constants.HORIZONTAL_CLAW_TRANSFER_POS_PITCH);
            }
            if (gamepad2.left_bumper) {

            }
            horizontalClawRotator2.setPosition(horizontalClawRotator2.getPosition() + gamepad2.left_stick_x / 500);
            telemetry.addData("open/close", horizontalClaw.getPosition());
            telemetry.addData("pitch", horizontalClawRotator.getPosition());
            telemetry.addData("yaw", horizontalClawRotator2.getPosition());
            telemetry.update();
        }
    }

    public double filterPosition(double position) {
        return Math.max(0, Math.min(1, position));
    }
}
