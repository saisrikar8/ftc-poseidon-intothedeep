package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Servo Testing")
public class ServoTesting extends LinearOpMode {
    Servo servo;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();

    public void runOpMode(){
        servo=hardwareMap.get(Servo.class, "servo");
        waitForStart();
        double position = servo.getPosition();
        while (opModeIsActive() && !isStopRequested()){
            telemetryPacket.put("servo position", servo.getPosition());
            dashboard.sendTelemetryPacket(telemetryPacket);
            if (gamepad1.a){
                position=0;
            }
            else if (gamepad1.b){
                position=0.5;
            }
            else if (gamepad1.x){
                position=1;
            }
            servo.setPosition(position);
        }

    }
}
