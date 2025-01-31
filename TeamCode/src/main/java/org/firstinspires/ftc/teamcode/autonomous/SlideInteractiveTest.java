package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.Constants.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Slide;

@Autonomous(name = "Slide Interactive Test")
public class SlideInteractiveTest extends LinearOpMode {
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();

    // vertical slide motors
    DcMotor motor1;
    DcMotor motor2;


    @Override
    public void runOpMode() {

        // initializing slides
        motor1 = hardwareMap.get(DcMotor.class, "motor");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Slide slide1 = new Slide(motor1);
        Slide slide2 = new Slide(motor2);

        // waiting for start
        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {

            // updating telemetry
            telemetryPacket.put("slide1 position", motor1.getCurrentPosition());
            telemetryPacket.put("slide2 position", motor2.getCurrentPosition());
            telemetryPacket.put("slide1 motor power", motor1.getPower());
            telemetryPacket.put("slide2 motor power", motor2.getPower());
            dashboard.sendTelemetryPacket(telemetryPacket);

            // initializing the Action that should be done by each slide
            Action selectedAction1 = null;
            Action selectedAction2 = null;

            // selecting the Action
            if (gamepad1.a) {
                selectedAction1 = slide1.moveToFourStageHighestPos();
                selectedAction2 = slide2.moveToFourStageHighestPos();
            } else if (gamepad1.b) {
                selectedAction1 = slide1.moveToLowestPos();
                selectedAction2 = slide2.moveToLowestPos();
            } else if (gamepad1.x) {
                selectedAction1 = slide1.moveToPosition(10);
                selectedAction2 = slide2.moveToPosition(10);
            } else {
                // default action, move slide by using gamepads
                slide1.setMotorPower((motor1.getCurrentPosition() < slide1.startingPosition) ? ((motor1.getCurrentPosition() > MAX_SLIDE_EXTENSION + slide1.startingPosition) ? (gamepad1.right_stick_y) : (Math.max(gamepad1.right_stick_y, 0))) : (Math.min(0, gamepad1.right_stick_y)));
                slide2.setMotorPower((motor2.getCurrentPosition() < slide2.startingPosition) ? ((motor2.getCurrentPosition() > MAX_SLIDE_EXTENSION + slide2.startingPosition) ? (gamepad1.right_stick_y) : (Math.max(gamepad1.right_stick_y, 0))) : (Math.min(0, gamepad1.right_stick_y)));
                if (motor1.getPower() == 0 && motor2.getPower() == 0) {
                    selectedAction1 = slide1.stayAtRest(gamepad1);
                    selectedAction2 = slide2.stayAtRest(gamepad1);
                }
            }

            // running the Action of each slide in parallel
            Actions.runBlocking(
                    new ParallelAction(
                            selectedAction1,
                            selectedAction2
                    )
            );
        }
    }
}

