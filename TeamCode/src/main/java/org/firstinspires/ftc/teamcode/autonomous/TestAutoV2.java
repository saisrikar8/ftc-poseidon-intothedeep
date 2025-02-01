package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE1_DEG;
import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE2_DEG;
import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE3_DEG;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_AIM_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_CLOSE_POS;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_IDLE_YAW;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_PICKUP_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_TRANSFER_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_CLOSE_CLAWPOS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_DROP_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_OPEN_CLAWPOS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_TRANSFER_PITCH;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Claw;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "Test Auto v2")
public class TestAutoV2 extends LinearOpMode {
    double ARM_DEGREES = Constants.ARM_STAGE2_DEG;
    private double claw2Pos = Constants.VERTICAL_CLAW_OPEN_CLAWPOS;
    private double claw2Pitch = Constants.VERTICAL_CLAW_DROP_PITCH;
    private double claw1Pitch = HORIZONTAL_CLAW_AIM_POS_PITCH;
    private double claw1Yaw = Constants.HORIZONTAL_CLAW_IDLE_YAW;
    private double claw1Pos = Constants.HORIZONTAL_CLAW_OPEN_POS;
    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor horizontal1, horizontal2, vertical1, vertical2;
    Servo verticalClaw, verticalClawRotator;
    Servo armRotator, armRotator2, horizontalClaw, horizontalClawRotator, horizontalClawYaw;
    Claw claw, claw2;
    HorizontalArmRotator arm;
    Elevator elevator;
    MecanumDrive drive;
    Pose2d currentPose;
    // EdgeDetectionPipeline edgeDetection;
    OpenCvWebcam webcam;

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.get(DcMotor.class, "front-left");
        frontRight = hardwareMap.get(DcMotor.class, "front-right");
        backLeft = hardwareMap.get(DcMotor.class, "rear-left");
        backRight = hardwareMap.get(DcMotor.class, "rear-right");

        horizontal1 = hardwareMap.get(DcMotor.class, "horizontal-slide-1");
        horizontal2 = hardwareMap.get(DcMotor.class, "horizontal-slide-2");
        vertical1 = hardwareMap.get(DcMotor.class, "vertical-slide-1");
        vertical2 = hardwareMap.get(DcMotor.class, "vertical-slide-2");
        vertical1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertical2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertical1.setDirection(DcMotorSimple.Direction.REVERSE);
        vertical1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertical2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertical1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        vertical2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armRotator = hardwareMap.get(Servo.class, "arm-rotator");
        armRotator2 = hardwareMap.get(Servo.class, "arm-rotator-2");

        verticalClaw = hardwareMap.get(Servo.class, "vertical-claw");
        verticalClawRotator = hardwareMap.get(Servo.class, "vertical-claw-rotator");

        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator");
        horizontalClawYaw = hardwareMap.get(Servo.class, "horizontal-claw-rotator-2");

        arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator, armRotator2);
        claw = new Claw(horizontalClaw, horizontalClawRotator, horizontalClawYaw);
        claw2 = new Claw(verticalClaw, verticalClawRotator);

        elevator = new Elevator(vertical1, vertical2);

        telemetry.addData("Stage", "Arm moved to inital position");
        telemetry.update();

        ARM_DEGREES = ARM_STAGE2_DEG;
        claw1Pos = HORIZONTAL_CLAW_CLOSE_POS;
        claw1Pitch = HORIZONTAL_CLAW_TRANSFER_POS_PITCH;
        claw1Yaw = HORIZONTAL_CLAW_IDLE_YAW;

        claw2Pos = VERTICAL_CLAW_CLOSE_CLAWPOS;
        claw2Pitch = VERTICAL_CLAW_TRANSFER_PITCH;


        currentPose = new Pose2d(-16, -60, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, currentPose);

        // initialize stage 2
        Actions.runBlocking(arm.setOrientation(ARM_DEGREES));
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));

        waitForStart();

        // go to basket
        TrajectoryActionBuilder traj0 = drive.actionBuilder(currentPose).splineTo(new Vector2d(-40, -46), Math.toRadians(90)).turnTo(Math.toRadians(55));
        currentPose = new Pose2d(new Vector2d(-40, -46), Math.toRadians(55));
        Actions.runBlocking(traj0.build());
        TrajectoryActionBuilder traj3 = drive.actionBuilder(currentPose).lineToY(-55);
        Actions.runBlocking(traj3.build());
        sleep(200);

        // make elevator go up
        // Actions.runBlocking(new ParallelAction(elevator.moveToHighestPosition()));
        slidesToFourStage();


        // move vertical claw to drop position and drop
        claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;
        claw2Pitch = VERTICAL_CLAW_DROP_PITCH;
        Actions.runBlocking(claw2.setClawPitch(claw2Pitch));
        sleep(800);
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));
        sleep(100);
        claw2Pitch = VERTICAL_CLAW_TRANSFER_PITCH;
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));
        slidesToLowestStage();

//        traj02 = drive.actionBuilder(currentPose).turnTo(Math.toRadians(90));
//        Actions.runBlocking(traj02.build());


        // go to right most sample
        TrajectoryActionBuilder traj01 = drive.actionBuilder(currentPose).strafeTo(new Vector2d(currentPose.position.x, currentPose.position.y + 10));
        Actions.runBlocking(new ParallelAction(traj01.build(), elevator.moveToLowestPosition()));

        TrajectoryActionBuilder traj02 = drive.actionBuilder(currentPose).splineTo(new Vector2d(-33, -36), Math.toRadians(90));
        Actions.runBlocking(traj02.build());


//        telemetry.addData("Sample detected: ", edgeDetection.sampleDetected);
//        if (edgeDetection.sampleDetected) {
//            telemetry.addData("sample location x: ", edgeDetection.boundingBox.center.x);
//            telemetry.addData("sample location y: ", edgeDetection.boundingBox.center.y);
//            telemetry.addData("sample location width: ", edgeDetection.boundingBox.size.width);
//            telemetry.addData("sample location height: ", edgeDetection.boundingBox.size.height);
//        }
        // grab it
        Actions.runBlocking(claw.setClawPitch(HORIZONTAL_CLAW_PICKUP_POS_PITCH));
        sleep(1500);
        Actions.runBlocking(arm.setOrientation(ARM_STAGE3_DEG));
        sleep(1500);
        Actions.runBlocking(claw.setClawPosition(HORIZONTAL_CLAW_CLOSE_POS));

        // transfer it up to vertical claw (USE TRANSFER FROM BEFORE)
        claw2Pitch = VERTICAL_CLAW_TRANSFER_PITCH;
        claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;
        Actions.runBlocking(new ParallelAction(claw2.setClawPitch(claw2Pitch), claw2.setClawPosition(claw2Pos)));
        sleep(500);
        ARM_DEGREES = ARM_STAGE1_DEG;
        claw1Pitch = HORIZONTAL_CLAW_TRANSFER_POS_PITCH;
        Actions.runBlocking(new ParallelAction(arm.setOrientation(ARM_DEGREES), claw.setClawPitch(claw1Pitch)));
        sleep(1500);
        // go to basket
        TrajectoryActionBuilder traj03 = drive.actionBuilder(currentPose).splineTo(new Vector2d(-40, -43), Math.toRadians(90)).turnTo(Math.toRadians(45));
        Actions.runBlocking(traj03.build());


        // go to middle sample


        telemetry.update();
        while (opModeIsActive() && !isStopRequested()) {
            sleep(1000);
        }
    }
    public void slidesToFourStage() {
        vertical1.setTargetPosition(Constants.MAX_SLIDE_EXTENSION);
        vertical2.setTargetPosition(Constants.MAX_SLIDE_EXTENSION);
        vertical1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        vertical2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive() && !isStopRequested() && vertical1.getCurrentPosition() != Constants.MAX_SLIDE_EXTENSION ) {
            vertical1.setPower(0.92);
            vertical2.setPower(0.92);
        }
    }
    public void slidesToLowestStage() {
        vertical1.setTargetPosition(15);
        vertical2.setTargetPosition(15);
        vertical1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        vertical2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (opModeIsActive() && !isStopRequested() && vertical1.getCurrentPosition() != 15 ) {
            vertical1.setPower(0.92);
            vertical2.setPower(0.92);
        }
    }
}
