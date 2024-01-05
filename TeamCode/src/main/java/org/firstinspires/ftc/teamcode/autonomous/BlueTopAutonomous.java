package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

@Autonomous(name = "BlueTopAutonomous")
public class BlueTopAutonomous extends LinearOpMode {
    private SampleMecanumDrive drive;
    private int propLocation;
    @Override
    public void runOpMode() throws InterruptedException{
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMotorPowers(1,1,1,1);
        waitForStart();
        while(opModeIsActive() && !isStopRequested()){
            Pose2d firstendposition;

            Trajectory traj1 = drive.trajectoryBuilder(new Pose2d(10,70,Math.toRadians(270)))
                    .forward(30)
                    .build();
            drive.followTrajectory(traj1);
            firstendposition = traj1.end();
            if(detectProp()){
                propLocation = BlueTapeMark.CENTER.getValue();
                drive.openClaw1();
                sleep(30);
                drive.closeClaw1();
            } else {
                drive.turn(Math.toRadians(-90));
                Trajectory traj2 = drive.trajectoryBuilder(traj1.end())
                        .strafeLeft(10)
                        .build();
                drive.followTrajectory(traj2);
                if(detectProp()){
                    propLocation = BlueTapeMark.RIGHT.getValue();
                    drive.openClaw1();
                    sleep(30);
                    drive.closeClaw1();
                    firstendposition = traj2.end();
                }  else {
                    propLocation = BlueTapeMark.LEFT.getValue();
                    Trajectory traj3 = drive.trajectoryBuilder(traj2.end())
                            .strafeRight(10)
                            .build();
                    Trajectory traj4 = drive.trajectoryBuilder(traj3.end())
                            .strafeRight(10)
                            .build();
                    drive.followTrajectory(traj3);
                    drive.turn(Math.toRadians(-180));
                    drive.followTrajectory(traj4);
                    drive.openClaw1();
                    sleep(30);
                    drive.closeClaw1();
                    firstendposition = traj4.end();
                }



            }


        }
    }
    public boolean detectProp(){
        TfodProcessor objectDetector = TfodProcessor.easyCreateWithDefaults();
        VisionPortal visionPortal = new VisionPortal.Builder()
                .addProcessor(objectDetector)
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .build();
        if (objectDetector.getRecognitions().size() > 0 && objectDetector.getRecognitions().get(0).getConfidence() >= 0.9){
            return true;
        }
        return false;
    }
    //evaluates whether the point on the backdrop is correct using the april tag
    public boolean determineIsCorrectPositionUsingPhoneCam(){
        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .build();

        VisionPortal visionPortal = new VisionPortal.Builder()
                .addProcessor(tagProcessor)
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .build();

        //getting details of April tag
        if (tagProcessor.getDetections().size() == 0){
            return false;
        }
        AprilTagDetection detection = tagProcessor.getDetections().get(0);
        int tagId = detection.id;
        if (propLocation == tagId) {
            AprilTagPoseFtc detectionPosition = detection.ftcPose;

            //adjusting bearing of robot to minimize error
            while (detectionPosition.bearing > 0.3) {
                drive.turn(-detectionPosition.bearing);
                detection = tagProcessor.getDetections().get(0);
                detectionPosition = detection.ftcPose;
            }
            //adjusting distance of robot from backdrop to minimize error
            while (detectionPosition.y > 3){
                drive.moveRobot((detectionPosition.y - 4)/20, 0, 0, false);
                detection = tagProcessor.getDetections().get(0);
                detectionPosition = detection.ftcPose;
            }
            //adjusting alignment of robot to correct backdrop positioning
            while(detectionPosition.x > 1){
                drive.moveRobot(0, (detectionPosition.x - 4)/20, 0, false);
                detection = tagProcessor.getDetections().get(0);
                detectionPosition = detection.ftcPose;
            }
            return true;
        }
        return false;
    }
}

