package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE1_DEG;
import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE2_DEG;
import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE3_DEG;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_AIM_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_CLOSE_POS;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_OPEN_POS;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_PICKUP_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_VERTICALSAMPLE_YAW;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_CLOSE_CLAWPOS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_OPEN_CLAWPOS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_RECEIVE_PITCH;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Claw;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;

@TeleOp(name = "Chassis Testing Teleop")
public class ChassisTestingTeleop extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    DcMotor vertical1, vertical2;
    DcMotor horizontal1, horizontal2;

    Elevator elevator;
    HorizontalArmRotator arm;

    Claw claw, claw2;

    Servo armRotator, armRotator2;

    Servo horizontalClaw, horizontalClawPitch, horizontalClawYaw;

    Servo verticalClaw, verticalClawRotator;

    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();
    private double ARM_DEGREES = ARM_STAGE3_DEG;
    private double claw2Pos = Constants.VERTICAL_CLAW_OPEN_CLAWPOS;
    private double claw2Pitch = Constants.VERTICAL_CLAW_RECEIVE_PITCH;
    private double claw1Pitch = HORIZONTAL_CLAW_AIM_POS_PITCH;
    private double claw1Yaw = Constants.HORIZONTAL_CLAW_IDLE_YAW;
    private double claw1Pos = Constants.HORIZONTAL_CLAW_OPEN_POS;

    private double vertical1StartPos;
    private double vertical2StartPos;

    public void runOpMode() throws InterruptedException {
        {
            //Fetching hardware entities from the robot config
//        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
//        frontRight = hardwareMap.get(DcMotor.class, "front-right");
//        backLeft = hardwareMap.get(DcMotor.class, "rear-left");
//        backRight = hardwareMap.get(DcMotor.class, "rear-right");

            vertical1 = hardwareMap.get(DcMotor.class, "vertical-slide-1");
            vertical2 = hardwareMap.get(DcMotor.class, "vertical-slide-2");

            horizontal1 = hardwareMap.get(DcMotor.class, "horizontal-slide-1");
            horizontal2 = hardwareMap.get(DcMotor.class, "horizontal-slide-2");
            armRotator = hardwareMap.get(Servo.class, "arm-rotator");
            armRotator2 = hardwareMap.get(Servo.class, "arm-rotator-2");

            horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
            horizontalClawPitch = hardwareMap.get(Servo.class, "horizontal-claw-rotator");
            horizontalClawYaw = hardwareMap.get(Servo.class, "horizontal-claw-rotator-2");

            verticalClaw = hardwareMap.get(Servo.class, "vertical-claw");
            verticalClawRotator = hardwareMap.get(Servo.class, "vertical-claw-rotator");

            // Setting the entities' characteristics
            vertical1StartPos = vertical1.getCurrentPosition();
            vertical2StartPos = vertical2.getCurrentPosition();

            // vertical1.setDirection(DcMotorSimple.Direction.REVERSE);

            vertical1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            vertical2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            horizontal1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            horizontal2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//        frontRight.setDirection(DcMotor.Direction.REVERSE);
//        backRight.setDirection(DcMotor.Direction.REVERSE);
            //-227, -241
            // sorting entities into subsystems
            elevator = new Elevator(vertical1, vertical2);
            arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator, armRotator2);
            claw = new Claw(horizontalClaw, horizontalClawPitch, horizontalClawYaw);
            claw2 = new Claw(verticalClaw, verticalClawRotator);
        }

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            boolean verticalSlideLimitReached = false;
            // Moving robot based on gamepad 1's inputs
            //moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            // moveClaw(gamepad1.left_stick_x, gamepad1.right_stick_y, gamepad1.y, gamepad1.a);

            // TODO: dpads not working rn, fix later

            if (gamepad2.dpad_left || gamepad2.dpad_right) {
                if (claw1Yaw == Constants.HORIZONTAL_CLAW_VERTICALSAMPLE_YAW)
                    claw1Yaw = Constants.HORIZONTAL_CLAW_HORIZONTALSAMPLE_YAW;
                else claw1Yaw = Constants.HORIZONTAL_CLAW_VERTICALSAMPLE_YAW;
                Actions.runBlocking(claw.setClawYaw(claw1Yaw));
            }
            if (gamepad2.left_bumper) {
                ARM_DEGREES = ARM_STAGE1_DEG;
                claw1Pos = HORIZONTAL_CLAW_OPEN_POS;
                claw1Pitch = HORIZONTAL_CLAW_AIM_POS_PITCH;
                claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;
            }
            if (gamepad2.right_bumper) {
                ARM_DEGREES = ARM_STAGE2_DEG;
                claw1Pos = HORIZONTAL_CLAW_OPEN_POS;
                claw1Pitch = HORIZONTAL_CLAW_AIM_POS_PITCH;
                claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;
            }
            if (gamepad2.right_trigger > 0.5) {
                ARM_DEGREES = ARM_STAGE3_DEG;
                claw1Pos = HORIZONTAL_CLAW_CLOSE_POS;
                claw1Pitch = HORIZONTAL_CLAW_PICKUP_POS_PITCH;
                Thread.sleep(500);
                claw2Pos = VERTICAL_CLAW_CLOSE_CLAWPOS;
            }
            if (gamepad2.left_trigger > 0.5) {
                ARM_DEGREES = ARM_STAGE1_DEG;
                claw1Pos = HORIZONTAL_CLAW_CLOSE_POS;
                claw1Pitch = HORIZONTAL_CLAW_PICKUP_POS_PITCH;
                claw1Yaw = HORIZONTAL_CLAW_VERTICALSAMPLE_YAW;
                claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;
                claw2Pitch = VERTICAL_CLAW_RECEIVE_PITCH;
            }
            if (gamepad2.right_stick_x > 0.6) {
                returnHorizontalToPickupPosition();
            }

            // running
            arm.setMotorPowers(gamepad2.right_stick_x);
            elevator.setMotorPowers(gamepad2.left_stick_y);
            Actions.runBlocking(
                    new ParallelAction(
                            arm.setOrientation(ARM_DEGREES),
                            claw.setClawYaw(claw1Yaw),
                            claw.setClawPosition(claw1Pos),
                            claw.setClawPitch(claw1Pitch),
                            claw2.setClawPitch(claw2Pitch),
                            claw2.setClawPosition(claw2Pos)
                    )
            );
            telemetry.addData("vertical rotator", verticalClawRotator.getPosition());
            telemetry.addData("vertical slide 1 pos: ", vertical1.getCurrentPosition());
            telemetry.addData("vertical slide 2 pos: ", vertical2.getCurrentPosition());
            telemetry.addData("vertical claw pos: ", claw2Pos);
            telemetry.update();
        }
    }

    public void returnHorizontalToInitialPosition() throws InterruptedException {
        ARM_DEGREES = ARM_STAGE1_DEG;
        Actions.runBlocking(claw.setClawPosition(0.49));
        Thread.sleep(250);
        Actions.runBlocking(new ParallelAction(arm.setOrientation(ARM_DEGREES), claw.setClawPitch(0.3)));
    }

    public void returnHorizontalToPickupPosition() {
        ARM_DEGREES = ARM_STAGE3_DEG;
        Actions.runBlocking(new ParallelAction(arm.setOrientation(ARM_DEGREES), claw.setClawPitch(0.99), claw.setClawPosition(0.7)));
    }

    public void autoIntake() throws InterruptedException {
        returnHorizontalToInitialPosition();
        vertical1.setTargetPosition((int) vertical1StartPos + 2846 - 3273);
        vertical2.setTargetPosition((int) vertical2StartPos + 2766 - 3285);
        vertical1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        vertical2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (vertical1.isBusy() && vertical2.isBusy()) {
            vertical1.setPower(0.5);
            vertical2.setPower(0.5);
        }
        vertical1.setPower(0);
        vertical2.setPower(0);
        vertical1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        vertical2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        horizontalClaw.setPosition(1);
    }

    public void returnVerticalToOpenPosition() {
        Actions.runBlocking(claw2.setClawPosition(0.5));
    }


    public void moveClaw(double leftStickX, double rightStickY, boolean gamepadYUp, boolean gamepadADown) {
        // move vertically
        leftStickX /= 1000;
        rightStickY /= 1000;

        double horizontalPosition = (horizontalClawPitch.getPosition() + rightStickY);
        if (horizontalPosition > 1 || horizontalPosition < 0) {
            horizontalPosition = horizontalClawPitch.getPosition();
        }
        horizontalClawPitch.setPosition(horizontalPosition);
        telemetry.addData("CLAW HORIZONTAL POSITION: ", horizontalPosition);

        // open and close claw position, uses RIGHT STICK LEFT AND RIGHT
        double clawOpenPosition = (horizontalClaw.getPosition() + leftStickX);
        if (clawOpenPosition > 1 || clawOpenPosition < 0) {
            clawOpenPosition = horizontalClaw.getPosition();
        }
        horizontalClaw.setPosition(clawOpenPosition);
        telemetry.addData("CLAW OPEN CLOSE POSITION: ", clawOpenPosition);

        double moveArmRotatorDiff = armRotator.getPosition();
        if (gamepadYUp) moveArmRotatorDiff += 0.05;
        if (gamepadADown) moveArmRotatorDiff -= 0.05;
        if (moveArmRotatorDiff > 1 || moveArmRotatorDiff < 0) {
            moveArmRotatorDiff = armRotator.getPosition();
        }
        armRotator.setPosition(moveArmRotatorDiff);
        telemetry.addData("MAIN ARM MOTOR: ", moveArmRotatorDiff);
    }

    public void moveRobot(double leftStickX, double leftStickY, double rightStickX) {
        double speed = leftStickY;   // Forward/Backward movement
        double strafe = -leftStickX;  // Left/Right movement (strafe)
        double turn = -rightStickX;   // Rotation

        // Apply correction factors for uneven weight distribution
        double strafeCorrectionFrontLeft = 0.95;  // Reduce power for front-left motor
        double strafeCorrectionFrontRight = 0.95; // Reduce power for front-right motor

        // Calculate each motor's power
        double frontLeftPower = (speed + turn + strafe) * strafeCorrectionFrontLeft;
        double frontRightPower = (speed - turn - strafe) * strafeCorrectionFrontRight;
        double backLeftPower = speed + turn - strafe;
        double backRightPower = speed - turn + strafe;

        // Normalize powers if any exceed the range [-1.0, 1.0]
        double maxPower = Math.max(1.0,
                Math.max(Math.abs(frontLeftPower),
                        Math.max(Math.abs(frontRightPower),
                                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)))));

        frontLeftPower /= maxPower;
        frontRightPower /= maxPower;
        backLeftPower /= maxPower;
        backRightPower /= maxPower;

        // Set motor powers
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }
}
