package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "something")
public class SlideOpMode extends LinearOpMode {
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();
    // op mode for slide

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motor = hardwareMap.get(DcMotor.class, "motor");
        Slide slide = new Slide(hardwareMap, "motor");
        telemetryPacket.put("status", "initialized");
        dashboard.sendTelemetryPacket(telemetryPacket);
        waitForStart();
        telemetryPacket.put("status", "blocking action");
        dashboard.sendTelemetryPacket(telemetryPacket);

        // Actions.runBlocking(slide.testMoveABit());
        while (opModeIsActive() && !isStopRequested()) {
            telemetryPacket.put("status", "started");
            telemetryPacket.put("left stick y", gamepad1.left_stick_y);
            telemetryPacket.put("left stick x", gamepad1.left_stick_x);
            telemetryPacket.put("motor position", motor.getCurrentPosition());
            dashboard.sendTelemetryPacket(telemetryPacket);
            motor.setPower(gamepad1.left_stick_y); // stick debug with controller
        }
    }
}
