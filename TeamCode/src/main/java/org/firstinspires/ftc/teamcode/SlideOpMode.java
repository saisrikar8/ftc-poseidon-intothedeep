package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Constants.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;

@Autonomous(name = "something")
public class SlideOpMode extends LinearOpMode {
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();
    // op mode for slide

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motor1 = hardwareMap.get(DcMotor.class, "motor");
        DcMotor motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        double startPosition1 = motor1.getCurrentPosition();
        Slide slide1 = new Slide(motor1);
        Slide slide2 = new Slide(motor2);
        telemetryPacket.put("status", "initialized");
        dashboard.sendTelemetryPacket(telemetryPacket);
        waitForStart();
        telemetryPacket.put("status", "started");
        dashboard.sendTelemetryPacket(telemetryPacket);
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Actions.runBlocking(slide.testMoveABit());
        while (opModeIsActive() && !isStopRequested()) {
            telemetryPacket.put("left stick y", gamepad1.left_stick_y);
            telemetryPacket.put("left stick x", gamepad1.left_stick_x);
            telemetryPacket.put("motor1 position", motor1.getCurrentPosition());
            dashboard.sendTelemetryPacket(telemetryPacket);

            Action selectedAction1 = null;
            Action selectedAction2 = null;
            if (gamepad1.a) {
                selectedAction1 = slide1.moveToHighestPos();
                selectedAction2 = slide2.moveToHighestPos();
            }
            else if (gamepad1.b) {
                selectedAction1 = slide1.moveToLowestPos();
                selectedAction2 = slide2.moveToLowestPos();
            }
            else if (gamepad1.x) {
                selectedAction1 = slide1.moveToPosition(10);
                selectedAction2 = slide2.moveToPosition(10);
            }
            if (selectedAction1 != null){
                Actions.runBlocking(
                        new ParallelAction(
                                selectedAction1,
                                selectedAction2
                        )
                );
            }
            motor1.setPower((motor1.getCurrentPosition() < 0)?((motor1.getCurrentPosition() > MAX_SLIDE_EXTENSION+slide1.startingPosition) ? (gamepad1.left_stick_y):(Math.max(gamepad1.left_stick_y, 0))):(Math.min(0, gamepad1.left_stick_y)));
            motor2.setPower((motor2.getCurrentPosition() < 0)?((motor2.getCurrentPosition() > MAX_SLIDE_EXTENSION+slide2.startingPosition) ? (gamepad1.left_stick_y):(Math.max(gamepad1.left_stick_y, 0))):(Math.min(0, gamepad1.left_stick_y)));
        }
    }
}
