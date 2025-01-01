package org.firstinspires.ftc.teamcode.teleop;

import android.util.Size;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.Locale;

@TeleOp(name = "Camera Test")
public class CameraTestTeleop extends LinearOpMode {
    final boolean USING_WEBCAM = false;
    final BuiltinCameraDirection INTERNAL_CAM_DIR = BuiltinCameraDirection.BACK;
    final int RESOLUTION_WIDTH = 640;
    final int RESOLUTION_HEIGHT = 480;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetry = new TelemetryPacket();

    boolean lastX;
    int frameCount;
    long capReqTime;

    @Override
    public void runOpMode() {
        VisionPortal portal;
        AprilTagDetection detection;
        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .build();

        if (USING_WEBCAM) {
            portal = new VisionPortal.Builder()
                    .addProcessor(tagProcessor)
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .setCameraResolution(new Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                    .build();
        } else {
            portal = new VisionPortal.Builder()
                    .addProcessor(tagProcessor)
                    .setCamera(INTERNAL_CAM_DIR)
                    .setCameraResolution(new Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                    .build();
        }

        waitForStart();

        while (!isStopRequested()) {
            boolean x = gamepad1.x;

            if (x && !lastX) {
                portal.saveNextFrameRaw(String.format(Locale.US, "CameraFrameCapture-%06d", frameCount++));
                capReqTime = System.currentTimeMillis();
            }

            lastX = x;

            telemetry.addLine("######## Camera Capture Utility ########");
            telemetry.addLine(String.format(Locale.US, " > Resolution: %dx%d", RESOLUTION_WIDTH, RESOLUTION_HEIGHT));
            telemetry.addLine(" > Press X (or Square) to capture a frame");
            telemetry.put(" > Camera Status", portal.getCameraState());

            try {
                detection = tagProcessor.getDetections().get(0);
                telemetry.put("Tag detection ID", detection.id);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                telemetry.put("Tag detection ID", "Unknown");
            }
            dashboard.sendTelemetryPacket(telemetry);

            if (capReqTime != 0) {
                telemetry.addLine("\nCaptured Frame!");
            }

            if (capReqTime != 0 && System.currentTimeMillis() - capReqTime > 1000) {
                capReqTime = 0;
            }
            dashboard.sendTelemetryPacket(telemetry);
        }
    }
}