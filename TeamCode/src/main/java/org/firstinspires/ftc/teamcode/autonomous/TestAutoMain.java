package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Claw;
import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;
import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "TestAutoMain")
public class TestAutoMain extends LinearOpMode {
    int INITIAL_POSE_X = 10;
    int INITIAL_POSE_Y = 10;
    int INITIAL_DEGREES = 90;
    int armDegrees = 210; // initial arm degrees, will run IMMEDIATELY when the op mode start

    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor vertical1, vertical2, horizontal1, horizontal2;
    Servo armRotator, horizontalClaw, horizontalClawRotator;
    Servo verticalClaw, verticalClawRotator;
    Claw claw, claw2;
    Elevator elevator;
    HorizontalArmRotator arm;

    @Override
    public void runOpMode() throws InterruptedException {

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

        elevator = new Elevator(vertical1, vertical2);
        arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator, null);
        claw = new Claw(horizontalClaw, horizontalClawRotator);
        claw2 = new Claw(verticalClaw, null);


        Pose2d initialPose = new Pose2d(INITIAL_POSE_X, INITIAL_POSE_Y, Math.toRadians(INITIAL_DEGREES));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        TelemetryPacket packet = new TelemetryPacket();
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

        waitForStart();
        // sets claw to initial position
        packet.put("pose x: ", drive.pose.position.x);
        packet.put("pose y: ", drive.pose.position.y);
        telemetry.update();

        returnClawToStart();
        Thread.sleep(1000);
        grabSample();
        Thread.sleep(1000);
        returnClawToTop();
    }

    void returnClawToStart() {
        armDegrees = 210;
        Actions.runBlocking(new ParallelAction(arm.setOrientation(armDegrees), claw.setClawPitch(0.99), claw.setClawPosition(0.7)));
    }

    void grabSample() throws InterruptedException {
        Actions.runBlocking(claw.setClawPosition(0.5));
        Thread.sleep(250); // wait a bit for it to grab
    }

    void returnClawToTop() {
        armDegrees = 90;
        Actions.runBlocking(new ParallelAction(arm.setOrientation(armDegrees), claw.setClawPitch(0.3)));
    }
}
