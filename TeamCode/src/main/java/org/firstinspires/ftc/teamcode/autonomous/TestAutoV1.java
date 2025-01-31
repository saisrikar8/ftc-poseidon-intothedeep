package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE2_DEG;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_AIM_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_OPEN_POS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_OPEN_CLAWPOS;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Claw;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;
import org.firstinspires.ftc.teamcode.MecanumDrive;

public class TestAutoV1 extends LinearOpMode {
    double ARM_DEGREES = Constants.ARM_STAGE2_DEG;
    private double claw2Pos = Constants.VERTICAL_CLAW_OPEN_CLAWPOS;
    private double claw2Pitch = Constants.VERTICAL_CLAW_RECEIVE_PITCH;
    private double claw1Pitch = HORIZONTAL_CLAW_AIM_POS_PITCH;
    private double claw1Yaw = Constants.HORIZONTAL_CLAW_IDLE_YAW;
    private double claw1Pos = Constants.HORIZONTAL_CLAW_OPEN_POS;
    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor horizontal1, horizontal2;
    Servo verticalClaw, verticalClawRotator;
    Servo armRotator, armRotator2, horizontalClaw, horizontalClawRotator;
    Claw claw, claw2;
    HorizontalArmRotator arm;
    MecanumDrive drive;
    Pose2d currentPose;
    @Override
    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        frontRight = hardwareMap.get(DcMotor.class, "front-right");
        backLeft = hardwareMap.get(DcMotor.class, "rear-left");
        backRight = hardwareMap.get(DcMotor.class, "rear-right");

        horizontal1 = hardwareMap.get(DcMotor.class, "horizontal-slide-1");
        horizontal2 = hardwareMap.get(DcMotor.class, "horizontal-slide-2");
        armRotator = hardwareMap.get(Servo.class, "arm-rotator");
        armRotator2 = hardwareMap.get(Servo.class, "arm-rotator-2");

        verticalClaw = hardwareMap.get(Servo.class, "vertical-claw");
        verticalClawRotator = hardwareMap.get(Servo.class, "vertical-claw-rotator");

        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator");

        arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator, armRotator2);
        claw = new Claw(horizontalClaw, horizontalClawRotator);
        claw2 = new Claw(verticalClaw, verticalClawRotator);

        currentPose = new Pose2d(-16, -60, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, currentPose);

        telemetry.addData("Stage", "Arm moved to inital position");
        telemetry.update();

        ARM_DEGREES = ARM_STAGE2_DEG;
        claw1Pos = HORIZONTAL_CLAW_OPEN_POS;
        claw1Pitch = HORIZONTAL_CLAW_AIM_POS_PITCH;
        claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;

        currentPose = new Pose2d(-16, -60, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, currentPose);

        // initialize stage 2
        Actions.runBlocking(arm.setOrientation(ARM_DEGREES));
        Actions.runBlocking(claw.setClawPosition(claw1Pos));
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));

        waitForStart();
        // goes to left most sample
        TrajectoryActionBuilder traj1 = drive.actionBuilder(currentPose).splineToConstantHeading(new Vector2d(-68, -30), Math.toRadians(90));
        Actions.runBlocking(traj1.build());
    }
}
