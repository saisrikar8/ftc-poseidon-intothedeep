package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Claw;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;
import org.firstinspires.ftc.teamcode.MecanumDrive;

public class v1ColorSide extends LinearOpMode {
    int ARM_DEGREES = 210; // initial arm degrees, will run IMMEDIATELY when the op mode start
    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor horizontal1, horizontal2;
    Servo armRotator, armRotator2, horizontalClaw, horizontalClawRotator;
    Claw claw;
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

        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator");

        arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator, armRotator2);
        claw = new Claw(horizontalClaw, horizontalClawRotator);


        currentPose = new Pose2d(-16, -60, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, currentPose);


        telemetry.addData("Stage", "Arm moved to inital position");
        telemetry.update();
        // return claw to inital before start
        Actions.runBlocking(returnClawToStart());


        waitForStart();
        telemetry.addData("Stage", "Sample 1");
        telemetry.update();
        // move to first sample leftmost
        TrajectoryActionBuilder traj1 = drive.actionBuilder(currentPose).splineToConstantHeading(new Vector2d(68, -30), Math.toRadians(90));
        Actions.runBlocking(traj1.build());
        // pickup sample
        Actions.runBlocking(grabSample());
        // return back to base plate
        TrajectoryActionBuilder traj2 = traj1.endTrajectory().fresh().splineTo(new Vector2d(60, -60), Math.toRadians(90));
        Actions.runBlocking(traj2.build());
        // drop sample
        Actions.runBlocking(releaseClaw());

        telemetry.addData("Stage", "Sample 2");
        telemetry.update();
        // move to second yellow sample
        TrajectoryActionBuilder traj3 = traj2.endTrajectory().fresh().splineTo(new Vector2d(60, -30), Math.toRadians(90));
        Actions.runBlocking(traj3.build());
        // pickup sample
        Actions.runBlocking(grabSample());
        // return back to base plate
        TrajectoryActionBuilder traj4 = traj3.endTrajectory().fresh().splineTo(new Vector2d(60, -60), Math.toRadians(90));
        Actions.runBlocking(traj4.build());
        // drop sample
        Actions.runBlocking(releaseClaw());

        telemetry.addData("Stage", "Sample 3");
        telemetry.update();
        // third yellow sample
        TrajectoryActionBuilder traj5 = traj4.endTrajectory().fresh().splineTo(new Vector2d(48, -30), Math.toRadians(90));
        Actions.runBlocking(traj5.build());
        // pickup sample
        Actions.runBlocking(grabSample());
        // return back to base plate
        TrajectoryActionBuilder traj6 = traj5.endTrajectory().fresh().splineTo(new Vector2d(60, -60), Math.toRadians(90));
        Actions.runBlocking(traj6.build());
        // drop sample
        Actions.runBlocking(releaseClaw());
        telemetry.addData("Stage", "DONE");
        telemetry.update();

    }
    Action returnClawToStart() {
        ARM_DEGREES = 210;
        return new ParallelAction(arm.setOrientation(ARM_DEGREES), claw.setClawPitch(0.99), claw.setClawPosition(0.7));
    }

    Action grabSample() throws InterruptedException {
        return claw.setClawPosition(0.5);
    }
    Action releaseClaw() throws InterruptedException {
        return claw.setClawPosition(0.3);
    }

    Action returnClawToTop() {
        ARM_DEGREES = 90;
        return new ParallelAction(arm.setOrientation(ARM_DEGREES), claw.setClawPitch(0.3));
    }

}

