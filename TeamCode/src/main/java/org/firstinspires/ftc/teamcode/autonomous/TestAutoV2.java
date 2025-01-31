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
import static org.firstinspires.ftc.teamcode.Constants.HORIZONTAL_CLAW_VERTICALSAMPLE_YAW;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_CLOSE_CLAWPOS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_DROP_PITCH;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_OPEN_CLAWPOS;
import static org.firstinspires.ftc.teamcode.Constants.VERTICAL_CLAW_TRANSFER_PITCH;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Claw;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.EdgeDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name="Test Auto v2")
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
    Servo armRotator, armRotator2, horizontalClaw, horizontalClawRotator;
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

        arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator, armRotator2);
        claw = new Claw(horizontalClaw, horizontalClawRotator);
        claw2 = new Claw(verticalClaw, verticalClawRotator);

        currentPose = new Pose2d(-16, -60, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, currentPose);

        elevator = new Elevator(vertical1, vertical2);

        telemetry.addData("Stage", "Arm moved to inital position");
        telemetry.update();

        ARM_DEGREES = ARM_STAGE1_DEG;
        claw1Pos = HORIZONTAL_CLAW_CLOSE_POS;
        claw1Pitch = HORIZONTAL_CLAW_TRANSFER_POS_PITCH;
        claw1Yaw = HORIZONTAL_CLAW_IDLE_YAW;

        claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;
        claw2Pitch = VERTICAL_CLAW_TRANSFER_PITCH;

        // make sure all arm stuff is in the correct position
        Actions.runBlocking(new ParallelAction(claw2.setClawPitch(claw2Pitch), claw2.setClawPosition(claw2Pos)));
        sleep(1000);
        Actions.runBlocking(new ParallelAction(arm.setOrientation(ARM_DEGREES), claw.setClawPosition(claw1Pos), claw.setClawYaw(claw1Yaw), claw.setClawPosition(claw1Pos)));


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
        Actions.runBlocking(claw.setClawPosition(claw1Pos));
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));
        Actions.runBlocking(claw2.setClawPitch(claw2Pitch));


        waitForStart();

        // initiate transfer
        sleep(1000);
        claw1Pos = HORIZONTAL_CLAW_OPEN_POS;
        claw2Pos = VERTICAL_CLAW_CLOSE_CLAWPOS;
        ARM_DEGREES = ARM_STAGE2_DEG;


        // close vertical claw
        Actions.runBlocking(claw2.setClawPosition(claw2Pos));
        sleep(250);
        // open horizontal claw and move arm out of the way
        Actions.runBlocking(new SequentialAction(claw.setClawPosition(claw1Pos), arm.setOrientation(ARM_DEGREES)));
        sleep(1000);
        // make elevator go up
        Actions.runBlocking(elevator.moveToHighestPosition());
        // move vertical claw to drop position and drop
        claw2Pos = VERTICAL_CLAW_OPEN_CLAWPOS;
        claw2Pitch = VERTICAL_CLAW_DROP_PITCH;
        Actions.runBlocking(new SequentialAction(claw2.setClawPitch(claw2Pos), claw2.setClawPosition(claw2Pos)));



        // goes to left most sample
        TrajectoryActionBuilder traj1 = drive.actionBuilder(currentPose).splineTo(new Vector2d(-33, -36), Math.toRadians(90));
        Actions.runBlocking(traj1.build());
        telemetry.addData("Sample detected: ", edgeDetection.sampleDetected);
        if (edgeDetection.sampleDetected) {
            telemetry.addData("sample location x: ", edgeDetection.boundingBox.center.x);
            telemetry.addData("sample location y: ", edgeDetection.boundingBox.center.y);
            telemetry.addData("sample location width: ", edgeDetection.boundingBox.size.width);
            telemetry.addData("sample location height: ", edgeDetection.boundingBox.size.height);
        }
        Actions.runBlocking(claw.setClawPitch(HORIZONTAL_CLAW_PICKUP_POS_PITCH));
        sleep(1500);
        Actions.runBlocking(arm.setOrientation(ARM_STAGE3_DEG));
        sleep(1500);
        Actions.runBlocking(claw.setClawPosition(HORIZONTAL_CLAW_CLOSE_POS));


        telemetry.update();
        while (opModeIsActive() && !isStopRequested()) {
            sleep(1000);
        }

    }
}
