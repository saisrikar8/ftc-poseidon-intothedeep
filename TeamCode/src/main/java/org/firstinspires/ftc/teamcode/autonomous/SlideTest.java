package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Slide;

@Autonomous(name = "Slide Test")
public class SlideTest extends LinearOpMode {
    // create slide objects
    Slide slide1 = new Slide(hardwareMap, "slide motor 2");
    Slide slide2 = new Slide(hardwareMap, "slide motor 2");
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetry = new TelemetryPacket();

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.put("Status", "Initialized");
        dashboard.sendTelemetryPacket(telemetry);
        waitForStart();

        // start the slide at smallest length
        slide1.moveToLowestPos();
        slide2.moveToLowestPos();

        // slide to max 2 stage position
        slide1.moveToTwoStageHighestPos();
        slide2.moveToTwoStageHighestPos();

        // slide to max 4 stage position
        slide1.moveToFourStageHighestPos();
        slide2.moveToFourStageHighestPos();

        // move back to lowest position
        slide1.moveToLowestPos();
        slide2.moveToLowestPos();
    }
}
