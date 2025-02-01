package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE1_DEG;
import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE2_DEG;
import static org.firstinspires.ftc.teamcode.Constants.ARM_STAGE3_DEG;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_AIM_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_CLOSE_POS;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_IDLE_YAW;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_OPEN_POS;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_PICKUP_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_TRANSFER_POS_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_CLOSE_CLAWPOS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_DROP_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_OPEN_CLAWPOS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_TRANSFER_PITCH;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TimeTurn;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Claw;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.EdgeDetectionPipeline;
import org.opencv.core.RotatedRect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
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
    EdgeDetectionPipeline edgeDetection;
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
        vertical1.setDirection(DcMotorSimple.Direction.REVERSE);
        vertical1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertical2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        edgeDetection = new EdgeDetectionPipeline();
        webcam.setPipeline(edgeDetection);
        {
            webcam.setMillisecondsPermissionTimeout(5000); // Timeout for obtaining permission is configurable. Set before opening.
            webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                @Override
                public void onOpened() {
                    /*
                     * Tell the webcam to start streaming images to us! Note that you must make sure
                     * the resolution you specify is supported by the camera. If it is not, an exception
                     * will be thrown.
                     *
                     * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
                     * supports streaming from the webcam in the uncompressed YUV image format. This means
                     * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
                     * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
                     *
                     * Also, we specify the rotation that the webcam is used in. This is so that the image
                     * from the camera sensor can be rotated such that it is always displayed with the image upright.
                     * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                     * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                     * away from the user.
                     */
                    webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT, OpenCvWebcam.StreamFormat.MJPEG);
                }

                @Override
                public void onError(int errorCode) {
                    /*
                     * This will be called if the camera could not be opened
                     */
                }
            });
        }

        // initialize stage 2
        Actions.runBlocking(arm.setOrientation(ARM_DEGREES));
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));

        waitForStart();

        // go to basket
        TrajectoryActionBuilder traj0 = drive.actionBuilder(currentPose).splineTo(new Vector2d(-40, -46), Math.toRadians(90)).turnTo(Math.toRadians(45)).splineTo(new Vector2d(-40, -50), Math.toRadians(45));
        Actions.runBlocking(traj0.build());


        // make elevator go up
        Actions.runBlocking(elevator.moveToHighestPosition());
        // move vertical claw to drop position and drop
        claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;
        claw2Pitch = VERTICAL_CLAW_DROP_PITCH;
        Actions.runBlocking(claw2.setClawPitch(claw2Pitch));
        sleep(750);
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));
        sleep(150);
        claw2Pitch = VERTICAL_CLAW_TRANSFER_PITCH;
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));
        sleep(150);

//        traj02 = drive.actionBuilder(currentPose).turnTo(Math.toRadians(90));
//        Actions.runBlocking(traj02.build());


        // go to right most sample
        TrajectoryActionBuilder traj01 = drive.actionBuilder(currentPose).strafeTo(new Vector2d(currentPose.position.x, currentPose.position.y + 10));
        Actions.runBlocking(new ParallelAction(traj01.build(), elevator.moveToLowestPosition()));

        TrajectoryActionBuilder traj1 = drive.actionBuilder(currentPose).splineTo(new Vector2d(-33, -36), Math.toRadians(90));
        Actions.runBlocking(traj1.build());


        telemetry.addData("Sample detected: ", edgeDetection.sampleDetected);
        if (edgeDetection.sampleDetected) {
            telemetry.addData("sample location x: ", edgeDetection.boundingBox.center.x);
            telemetry.addData("sample location y: ", edgeDetection.boundingBox.center.y);
            telemetry.addData("sample location width: ", edgeDetection.boundingBox.size.width);
            telemetry.addData("sample location height: ", edgeDetection.boundingBox.size.height);
        }
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
        TrajectoryActionBuilder traj2 = drive.actionBuilder(currentPose).splineTo(new Vector2d(-40, -43), Math.toRadians(90)).turnTo(Math.toRadians(45));
        Actions.runBlocking(traj2.build());


        // go to middle sample


        telemetry.update();
        while (opModeIsActive() && !isStopRequested()) {
            sleep(1000);
        }
    }

    public void cameraProcess() {
        int TICKS_TILL_TERMINATE = 1000;
        RotatedRect foundBoundingBox = edgeDetection.boundingBox;
        double sample_area = foundBoundingBox.size.width * foundBoundingBox.size.height;
        TrajectoryActionBuilder traj = null;
        telemetry.addData("sample area: ", sample_area);
        // TODO: calibrate ALL numbers
        // test size, if smaller, means not whole box is in view
        while (sample_area < 200) {
            int POS_DISPLACEMENT_AMOUNT = 3;
            // whole sample not detected, fix it
            // is the coordinates on the right or left?
            // too on the left, move right a bit
            if (foundBoundingBox.center.x < 100) {
                // wont work
                traj = drive.actionBuilder(currentPose).lineToX(currentPose.position.x + POS_DISPLACEMENT_AMOUNT);
            }
            // too on the right
            if (foundBoundingBox.center.x > 1000) {
                currentPose = new Pose2d(new Vector2d(currentPose.position.x - POS_DISPLACEMENT_AMOUNT, currentPose.position.y), currentPose.heading);
            }
            // too on the top
            if (foundBoundingBox.center.y > 1000) {
                currentPose = new Pose2d(new Vector2d(currentPose.position.x, currentPose.position.y - POS_DISPLACEMENT_AMOUNT), currentPose.heading);
            }
            // too on the bottom
            if (foundBoundingBox.center.y < 100) {
                currentPose = new Pose2d(new Vector2d(currentPose.position.x, currentPose.position.y + POS_DISPLACEMENT_AMOUNT), currentPose.heading);
            }
            if (traj != null) Actions.runBlocking(traj.build());
            sleep(5000); // SLOW LOOP FOR DEBUG
            sample_area = foundBoundingBox.size.width * foundBoundingBox.size.height;
        }
    }
}
