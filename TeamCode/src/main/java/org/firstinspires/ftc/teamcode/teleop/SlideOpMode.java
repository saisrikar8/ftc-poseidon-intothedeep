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

import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;
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
        DcMotor motor3 = hardwareMap.get(DcMotor.class, "horizontal-slide-1");
        DcMotor motor4 = hardwareMap.get(DcMotor.class, "horizontal-slide-2");
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor3.setDirection(DcMotorSimple.Direction.REVERSE);
        motor4.setDirection(DcMotorSimple.Direction.REVERSE);
        Elevator elevator = new Elevator(motor1, motor2);

        HorizontalArmRotator arm = new HorizontalArmRotator(motor3, motor4);
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

            Action selectedAction;
            if (gamepad1.a) {
                selectedAction = elevator.moveToHighestPosition();
            }
            else if (gamepad1.b) {
                selectedAction = elevator.moveToLowestPosition();
            }
            else if (gamepad1.x) {
                selectedAction = elevator.moveToPosition(10);
            }
            else {
                selectedAction = elevator.setMotorPowers(gamepad1.left_stick_y);
            }
            if (selectedAction != null){
                Actions.runBlocking(
                        new ParallelAction(
                            selectedAction,
                            arm.setMotorPowers(gamepad1.right_stick_x)
                        )
                );
            }
        }
    }
}