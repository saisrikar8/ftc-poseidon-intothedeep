package org.firstinspires.ftc.teamcode.teleop;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;


@TeleOp(name = "camera testing")
public class CameraTesting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        
        VisionPortal portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(1280, 720))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .enableLiveView(true)
                .setAutoStartStreamOnBuild(true)
                .build();

        try {
            waitForStart();

            while (opModeIsActive() && !isStopRequested()) {
                telemetry.addData("camera state", portal.getCameraState());
                telemetry.addData("fps", portal.getFps());
                telemetry.update();
            }
        } finally {
            portal.close(); // Ensure the portal is closed when the OpMode ends
        }
    }

}
