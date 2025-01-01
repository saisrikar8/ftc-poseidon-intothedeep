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

import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArm;

@TeleOp(name = "Chassis Testing Teleop")
public class ChassisTestingTeleop extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    DcMotor vertical1,vertical2;
    DcMotor horizontal1, horizontal2;

    Elevator elevator;
    HorizontalArm arm;

    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();
    public void runOpMode(){
        //Fetching hardware entities from the robot config
        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        frontRight = hardwareMap.get(DcMotor.class, "front-right");
        backLeft = hardwareMap.get(DcMotor.class, "rear-left");
        backRight = hardwareMap.get(DcMotor.class, "rear-right");

        vertical1 = hardwareMap.get(DcMotor.class, "vertical-slide-1");
        vertical2 = hardwareMap.get(DcMotor.class, "vertical-slide-2");

        horizontal1 = hardwareMap.get(DcMotor.class, "horizontal-slide-1");
        horizontal2 = hardwareMap.get(DcMotor.class, "horizontal-slide-2");


        //Setting the entities' characteristics
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


        //sorting entities into subsystems
        elevator = new Elevator(vertical1, vertical2);
        arm = new HorizontalArm(horizontal1, horizontal2);

        waitForStart();


        while (opModeIsActive() && !isStopRequested()){
            //Moving robot based on gamepad 1's inputs
            moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            //Running
            Actions.runBlocking(
                    new ParallelAction(
                        elevator.setMotorPowers(gamepad2.left_stick_y),
                        arm.setMotorPowers(gamepad2.right_stick_x)
                    )
            );
            telemetryPacket.put("right stick x", gamepad1.right_stick_x);
            telemetryPacket.put("right stick y", gamepad1.right_stick_y);

            telemetryPacket.put("left stick x", gamepad1.left_stick_x);
            telemetryPacket.put("left stick y", gamepad1.left_stick_y);
            dashboard.sendTelemetryPacket(telemetryPacket);
        }
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
