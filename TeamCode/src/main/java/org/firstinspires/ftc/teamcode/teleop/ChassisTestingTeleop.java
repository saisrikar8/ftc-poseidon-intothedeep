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

    Claw claw;

    Servo armRotator;

    Servo horizontalClaw, horizontalClawRotator;

    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();

    private double armDegrees = 120;
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
        armRotator = hardwareMap.get(Servo.class, "arm-rotator");

        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator");


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
        arm = new HorizontalArm(horizontal1, horizontal2, armRotator);
        claw = new Claw(horizontalClaw, null, horizontalClawRotator);


        waitForStart();


        while (opModeIsActive() && !isStopRequested()){
            //Moving robot based on gamepad 1's inputs
            moveRobot(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            if (gamepad2.x){
                armDegrees = 210;
            }
            if (gamepad2.b){
                armDegrees = 95;
            }
            if (gamepad2.a){
               horizontalClaw.setPosition(0.53);
            }
            if (gamepad2.y){
                horizontalClaw.setPosition(0.7);
            }

            //Running
            Actions.runBlocking(
                    new ParallelAction(
                            elevator.setMotorPowers(gamepad2.left_stick_y),
                            arm.setMotorPowers(gamepad2.right_stick_x),
                            arm.setOrientation(armDegrees)
                    )
            );

            telemetry.addData("left rotator", arm.rotator.getPosition());
            telemetry.update();
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
