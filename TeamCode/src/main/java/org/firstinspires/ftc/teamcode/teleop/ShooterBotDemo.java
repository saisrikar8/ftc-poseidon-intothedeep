package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "Shooter Bot Demo")
public class ShooterBotDemo extends LinearOpMode {
    public DcMotor frontLeft, frontRight, backLeft, backRight;
    public DcMotor shooter;
    public DcMotor slide1, slide2;

    @Override
    public void runOpMode(){
        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        frontRight = hardwareMap.get(DcMotor.class, "front-right");
        backLeft = hardwareMap.get(DcMotor.class, "rear-left");
        backRight = hardwareMap.get(DcMotor.class, "rear-right");

        shooter = hardwareMap.get(DcMotor.class, "shooter");

        slide1 = hardwareMap.get(DcMotor.class, "slide-1");
        slide2 = hardwareMap.get(DcMotor.class, "slide-2");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slide1.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.REVERSE);

        while (opModeIsActive() && !isStopRequested()){
            moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad2.right_stick_x);
            slide1.setPower(gamepad1.right_stick_y);
            slide2.setPower(gamepad1.right_stick_y);
            if (gamepad1.a){
                shooter.setPower(1);
            }
            if (gamepad1.b) {
                shooter.setPower(0);
            }
        }
    }

    public void moveRobot(double leftStickX, double leftStickY, double rightStickX) {
        double speed = leftStickY;   // Forward/Backward movement
        double strafe = -leftStickX;  // Left/Right movement (strafe)
        double turn = -rightStickX;   // Rotation


        // Calculate each motor's power
        double frontLeftPower = (speed + turn + strafe);
        double frontRightPower = (speed - turn - strafe);
        double backLeftPower = speed + turn - strafe;
        double backRightPower = speed - turn + strafe;

        // Set motor powers
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }

}
