package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.Constants.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Slide;

import java.util.ArrayList;

@TeleOp(name = "Slide Op Mode")
public class SlideOpMode extends LinearOpMode {
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();
    // op mode for slide

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motor1 = hardwareMap.get(DcMotor.class, "vertical-slide-1");
        DcMotor motor2 = hardwareMap.get(DcMotor.class, "vertical-slide-2");
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor2.setDirection(DcMotorSimple.Direction.REVERSE);
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Slide slide1 = new Slide(motor1);
        Slide slide2 = new Slide(motor2);
        dashboard.sendTelemetryPacket(telemetryPacket);
        waitForStart();
        dashboard.sendTelemetryPacket(telemetryPacket);

        // Actions.runBlocking(slide.testMoveABit());
        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addData("motor1 position", motor1.getCurrentPosition());
            telemetry.addData("motor2 position", motor2.getCurrentPosition());
            telemetry.addData("motor1 power", motor1.getPower());
            telemetry.addData("motor2 power", motor2.getPower());
            telemetry.update();

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
        }
    }
}