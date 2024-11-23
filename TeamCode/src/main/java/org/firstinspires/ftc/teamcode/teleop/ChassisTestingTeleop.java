package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Chassis Testing Teleop")
public class ChassisTestingTeleop extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();
    public void runOpMode(){

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

        waitForStart();


        while (opModeIsActive() && !isStopRequested()){
            // right stick x is horizontal direction!!
            moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
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
