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
import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;
import org.firstinspires.ftc.teamcode.MecanumDrive;

public class v2Neutral extends LinearOpMode {
    int ARM_DEGREES = 210; // initial arm degrees, will run IMMEDIATELY when the op mode start
    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor vertical1, vertical2, horizontal1, horizontal2;
    Servo armRotator, horizontalClaw, horizontalClawRotator;
    Servo verticalClaw, verticalClawRotator;
    Claw claw, claw2;
    HorizontalArmRotator arm;
    MecanumDrive drive;
    Elevator elevator;
    Pose2d currentPose;
    int VERTICAL_SLIDE_START_POSITION;

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

        arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator);
        claw = new Claw(horizontalClaw, horizontalClawRotator);
        claw2 = new Claw(verticalClaw, verticalClawRotator);
        elevator = new Elevator(vertical1, vertical2);

        VERTICAL_SLIDE_START_POSITION = vertical1.getCurrentPosition();
        currentPose = new Pose2d(-16, -60, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, currentPose);


        telemetry.addData("Stage", "Arm moved to inital position");
        telemetry.update();
        // return claw to inital before start
        Actions.runBlocking(returnHorizontalClawToStart());

        waitForStart();
        telemetry.addData("Stage", "Sample 1");
        telemetry.update();
        // move to first sample leftmost
        TrajectoryActionBuilder traj1 = drive.actionBuilder(currentPose).splineToConstantHeading(new Vector2d(-68, -30), Math.toRadians(90));
        Actions.runBlocking(traj1.build());
        // pickup sample
        Actions.runBlocking(closeHorizontalClaw());
        // return back to base plate
        TrajectoryActionBuilder traj2 = traj1.endTrajectory().fresh().splineTo(new Vector2d(-60, -60), Math.toRadians(90));
        Actions.runBlocking(traj2.build());
        // transfer sample - now horizontal claw is down and vertical claw has sample
        clawTransfer();
        // move vertical slide to basket
        moveVerticalSlidesToBasket();
        // rotate vertical claw
        Actions.runBlocking(verticalClawToDropPosition());
        // drop that thang
        Actions.runBlocking(releaseVerticalClaw());

        telemetry.addData("Stage", "Sample 2");
        telemetry.update();
        // move to second yellow sample
        TrajectoryActionBuilder traj3 = traj2.endTrajectory().fresh().splineTo(new Vector2d(-60, -30), Math.toRadians(90));
        Actions.runBlocking(traj3.build());
        // pickup sample
        Actions.runBlocking(closeHorizontalClaw());
        // return back to base plate
        TrajectoryActionBuilder traj4 = traj3.endTrajectory().fresh().splineTo(new Vector2d(-60, -60), Math.toRadians(90));
        Actions.runBlocking(traj4.build());
        // transfer sample - now horizontal claw is down and vertical claw has sample
        clawTransfer();
        // move vertical slide to basket
        moveVerticalSlidesToBasket();
        // rotate vertical claw
        Actions.runBlocking(verticalClawToDropPosition());
        // drop that thang
        Actions.runBlocking(releaseVerticalClaw());

        telemetry.addData("Stage", "Sample 3");
        telemetry.update();
        // third yellow sample
        TrajectoryActionBuilder traj5 = traj4.endTrajectory().fresh().splineTo(new Vector2d(-48, -30), Math.toRadians(90));
        Actions.runBlocking(traj5.build());
        // pickup sample
        Actions.runBlocking(closeHorizontalClaw());
        // return back to base plate
        TrajectoryActionBuilder traj6 = traj5.endTrajectory().fresh().splineTo(new Vector2d(-60, -60), Math.toRadians(90));
        Actions.runBlocking(traj6.build());
        // transfer sample - now horizontal claw is down and vertical claw has sample
        clawTransfer();
        // move vertical slide to basket
        moveVerticalSlidesToBasket();
        // rotate vertical claw
        Actions.runBlocking(verticalClawToDropPosition());
        // drop that thang
        Actions.runBlocking(releaseVerticalClaw());
        telemetry.addData("Stage", "DONE");
        telemetry.update();

    }

    Action returnHorizontalClawToStart() {
        ARM_DEGREES = 210;
        return new ParallelAction(arm.setOrientation(ARM_DEGREES), claw.setClawPitch(0.99), claw.setClawPosition(0.7));
    }

    Action returnHorizontalClawToTop() {
        ARM_DEGREES = 90;
        return new ParallelAction(arm.setOrientation(ARM_DEGREES), claw.setClawPitch(0.3));
    }

    Action closeHorizontalClaw() {
        return claw.setClawPosition(0.5);
    }

    Action releaseHorizontalClaw() {
        return claw.setClawPosition(0.3);
    }

    Action verticalClawInitialPosition() {
        // TODO: validate units
        return new ParallelAction(claw2.setClawPosition(0.7), claw2.setClawPitch(0.99));
    }

    Action closeVerticalClaw() {
        // TODO: validate units
        return claw2.setClawPosition(0.3);
    }

    Action releaseVerticalClaw() {
        // TODO: validate units
        return claw2.setClawPitch(0.7);
    }

    Action verticalClawToDropPosition() {
        // TODO: validate units
        return claw2.setClawPitch(0.3);
    }

    Action moveVerticalSlidesToBasket() {
        // TODO: test this, was made WITHOUT ANY robot testing
        int LOW_BASKET_TARGET_DIFF = 2500; // difference in slide at bottom and desired encoder pos
        int HIGH_BASKET_TARGET_DIFF = 5000;
        return elevator.moveToPosition(HIGH_BASKET_TARGET_DIFF + VERTICAL_SLIDE_START_POSITION);
    }

    void clawTransfer() throws InterruptedException {
        // move vertical claw to ready position
        Actions.runBlocking(verticalClawInitialPosition());
        // move arm to upright position
        Actions.runBlocking(returnHorizontalClawToTop());
        Thread.sleep(100);
        // close vertical claw
        Actions.runBlocking(closeVerticalClaw());
        // return open claw back to initial
        Actions.runBlocking(releaseHorizontalClaw());
        Actions.runBlocking(returnHorizontalClawToStart());
    }
}
