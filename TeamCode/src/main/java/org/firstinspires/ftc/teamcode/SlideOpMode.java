package org.firstinspires.ftc.teamcode;

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

import org.firstinspires.ftc.teamcode.teleop.ChassisTestingTeleop;
import org.firstinspires.ftc.teamcode.teleop.ChassisTestingTeleop.*;

import java.util.ArrayList;

@Autonomous(name = "")
public class SlideOpMode extends LinearOpMode {
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();

    //vertical slide motors
    DcMotor motor1;
    DcMotor motor2;


    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    @Override
    public void runOpMode() throws InterruptedException {

        //initializing slides
        motor1 = hardwareMap.get(DcMotor.class, "vertical1");
        motor2 = hardwareMap.get(DcMotor.class, "vertical2");
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Slide slide1 = new Slide(motor1);
        Slide slide2 = new Slide(motor2);

        //initializing drivetrain
        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        frontRight = hardwareMap.get(DcMotor.class, "front-right");
        backLeft = hardwareMap.get(DcMotor.class, "rear-left");
        backRight = hardwareMap.get(DcMotor.class, "rear-right");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);


        //waiting for start
        waitForStart();


        while (opModeIsActive() && !isStopRequested()) {

            //updating telemetry
            telemetryPacket.put("slide1 position", motor1.getCurrentPosition());
            telemetryPacket.put("slide2 position", motor2.getCurrentPosition());
            telemetryPacket.put("slide1 motor power", motor1.getPower());
            telemetryPacket.put("slide2 motor power", motor2.getPower());
            dashboard.sendTelemetryPacket(telemetryPacket);

            //initializing the Action that should be done by each slide
            Action selectedAction1 = null;
            Action selectedAction2 = null;

            //selecting the Action
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
            else{
                selectedAction1=slide1.setMotorPower((motor1.getCurrentPosition() < slide1.startingPosition) ? ((motor1.getCurrentPosition() > MAX_SLIDE_EXTENSION + slide1.startingPosition) ? (gamepad1.right_stick_y) : (Math.max(gamepad1.right_stick_y, 0))) : (Math.min(0, gamepad1.right_stick_y)));
                selectedAction2=slide2.setMotorPower((motor2.getCurrentPosition() < slide2.startingPosition) ? ((motor2.getCurrentPosition() > MAX_SLIDE_EXTENSION + slide2.startingPosition) ? (gamepad1.right_stick_y) : (Math.max(gamepad1.right_stick_y, 0))) : (Math.min(0, gamepad1.right_stick_y)));
                if (motor1.getPower() == 0 && motor2.getPower() == 0) {
                    selectedAction1 = slide1.stayAtRest(gamepad1);
                    selectedAction2 = slide2.stayAtRest(gamepad1);
                }
            }

            //drivetrain update
            moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            //running the Action of each slide in parallel
            Actions.runBlocking(
                    new ParallelAction(
                            selectedAction1,
                            selectedAction2
                    )
            );
        }
    }

    // drivetrain calculations and setting motor powers
    public void moveRobot(double leftStickX, double leftStickY, double rightStickX) {
        double speed = leftStickY;   // Forward/Backward movement
        double strafe = -leftStickX;  // Left/Right movement (strafe)
        double turn = -rightStickX;   // Rotation

        // Calculate each motor's power
        double frontLeftPower = speed + turn + strafe;
        double frontRightPower = speed - turn - strafe;
        double backLeftPower = speed + turn - strafe;
        double backRightPower = speed - turn + strafe;

        // Set motor powers
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }
}

