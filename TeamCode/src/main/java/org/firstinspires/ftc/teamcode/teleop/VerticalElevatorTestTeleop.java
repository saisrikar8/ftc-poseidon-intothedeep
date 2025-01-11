package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Elevator;

@TeleOp(name = "Vertical Slide Testing Teleop")
public class VerticalElevatorTestTeleop extends LinearOpMode {
    Elevator elevator;
    DcMotor vertical1, vertical2;

    @Override
    public void runOpMode() throws InterruptedException {
        vertical1 = hardwareMap.get(DcMotor.class, "vertical-slide-1");
        vertical2 = hardwareMap.get(DcMotor.class, "vertical-slide-2");
        elevator = new Elevator(vertical1, vertical2);
        updateTelemetry();

        waitForStart();
        while (!isStopRequested() && opModeIsActive()) {
            updateTelemetry();
            Actions.runBlocking(elevator.setMotorPowers(gamepad2.left_stick_y));
        }
    }

    void updateTelemetry() {
        telemetry.addData("motor 1 pos: ", vertical1.getCurrentPosition());
        telemetry.addData("motor 2 pos: ", vertical2.getCurrentPosition());
        telemetry.addData("motor 1 power: ", vertical1.getPower());
        telemetry.addData("motor 2 power: ", vertical2.getPower());
        telemetry.update();
    }
}
