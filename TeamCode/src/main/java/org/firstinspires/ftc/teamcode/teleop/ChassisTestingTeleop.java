package org.firstinspires.ftc.teamcode.teleop;

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

    Servo armRotator;

    Servo horizontalClaw, horizontalClawRotator;

    Servo verticalClaw, verticalClawRotator;

    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();

    private double armDegrees = 120;
    private double claw2Pos = 0.8264;
    private double verticalDegrees;

    public void runOpMode() throws InterruptedException {
        //Fetching hardware entities from the robot config
        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        frontRight = hardwareMap.get(DcMotor.class, "front-right");
        backLeft = hardwareMap.get(DcMotor.class, "rear-left");
        backRight = hardwareMap.get(DcMotor.class, "rear-right");

        vertical1 = hardwareMap.get(DcMotor.class, "vertical-slide-1");
        vertical2 = hardwareMap.get(DcMotor.class, "vertical-slide-2");

        horizontal1 = hardwareMap.get(DcMotor.class, "horizontal-slide-1");
        horizontal2 = hardwareMap.get(DcMotor.class, "horizontal-slide-2");
        armRotator = hardwareMap.get(Servo.class, "arm-rotator");

        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator");

        verticalClaw = hardwareMap.get(Servo.class, "vertical-claw");
        verticalClawRotator = hardwareMap.get(Servo.class, "vertical-claw-rotator");

        // Setting the entities' characteristics
        vertical1.setDirection(DcMotorSimple.Direction.REVERSE);

        vertical1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertical2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        //-227, -241
        // sorting entities into subsystems
        elevator = new Elevator(vertical1, vertical2);
        arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator);
        claw = new Claw(horizontalClaw, horizontalClawRotator);
        claw2 = new Claw(verticalClaw, null);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            boolean verticalSlideLimitReached = false;
            // Moving robot based on gamepad 1's inputs
            // moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            //moveClaw(gamepad1.left_stick_x, gamepad1.right_stick_y, gamepad1.y, gamepad1.a);

            if (gamepad2.x) {
                claw2Pos = 0.95;
            }
            if (gamepad2.b) {
                claw2Pos = 0.81;
            }
            if (gamepad2.a) {
                horizontalClaw.setPosition(0.53);
            }
            if (gamepad2.y) {
                horizontalClaw.setPosition(0.7);
            }
            if (gamepad2.right_bumper) {
                returnHorizontalToInitialPosition();
            }
            if (gamepad2.left_bumper) {
                returnHorizontalToPickupPosition();
            }
            if (gamepad2.right_stick_x > 0.3) {
                returnHorizontalToPickupPosition();
            }
            verticalClawRotator.setPosition(verticalClawRotator.getPosition() + gamepad1.left_stick_x/100);

            // running
            Actions.runBlocking(
                    new ParallelAction(
                            elevator.setMotorPowers(gamepad2.left_stick_y),
                            arm.setMotorPowers(gamepad2.right_stick_x),
                            arm.setOrientation(armDegrees),
                            claw2.setClawPosition(claw2Pos)
                    )
            );

            telemetry.addData("left rotator", arm.rotator.getPosition());
            telemetry.addData("vertical slide 1 pos: ", vertical1.getCurrentPosition());
            telemetry.addData("vertical slide 2 pos: ", vertical2.getCurrentPosition());
            telemetry.addData("vertical claw pos: ", claw2Pos);
            telemetry.update();
        }
    }

    public void returnHorizontalToInitialPosition() throws InterruptedException {
        armDegrees = 90;
        Actions.runBlocking(claw.setClawPosition(0.5));
        Thread.sleep(250);
        Actions.runBlocking(new ParallelAction(arm.setOrientation(armDegrees), claw.setClawRotatorPosition(0.3)));
    }

    public void returnHorizontalToPickupPosition() {
        armDegrees = 210;
        Actions.runBlocking(new ParallelAction(arm.setOrientation(armDegrees), claw.setClawRotatorPosition(0.99), claw.setClawPosition(0.7)));
    }
    public void returnVerticalToOpenPosition() {
        Actions.runBlocking(claw2.setClawPosition(0.5));
    }


    public void moveClaw(double leftStickX, double rightStickY, boolean gamepadYUp, boolean gamepadADown) {
        // move vertically
        leftStickX /= 1000;
        rightStickY /= 1000;

        double horizontalPosition = (horizontalClawRotator.getPosition() + rightStickY);
        if (horizontalPosition > 1 || horizontalPosition < 0) {
            horizontalPosition = horizontalClawRotator.getPosition();
        }
        horizontalClawRotator.setPosition(horizontalPosition);
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
