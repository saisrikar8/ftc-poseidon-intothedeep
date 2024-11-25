package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Constants.*;

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
        double startPosition = motor.getCurrentPosition();
        Slide slide = new Slide(hardwareMap, "motor");
        telemetryPacket.put("status", "initialized");
        dashboard.sendTelemetryPacket(telemetryPacket);
        waitForStart();
        telemetryPacket.put("status", "started");
        dashboard.sendTelemetryPacket(telemetryPacket);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Actions.runBlocking(slide.testMoveABit());
        while (opModeIsActive() && !isStopRequested()) {
            telemetryPacket.put("left stick y", gamepad1.left_stick_y);
            telemetryPacket.put("left stick x", gamepad1.left_stick_x);
            telemetryPacket.put("motor position", motor.getCurrentPosition());
            dashboard.sendTelemetryPacket(telemetryPacket);
            if (gamepad1.a) {
                motor.setTargetPosition(MAX_SLIDE_EXTENSION+(int)startPosition);
                motor.setPower(0.6);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while(motor.isBusy() && gamepad1.atRest()){
                    telemetryPacket.put("status", "going to position");
                    dashboard.sendTelemetryPacket(telemetryPacket);
                }
                motor.setPower(0);
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                telemetryPacket.put("status", "reached position");
            }
            if (gamepad1.b) {
                motor.setTargetPosition((int)startPosition);
                motor.setPower(0.6);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while(motor.isBusy() && gamepad1.atRest()){
                    telemetryPacket.put("status", "going to position");
                    dashboard.sendTelemetryPacket(telemetryPacket);
                    if (motor.getCurrentPosition()+50 >= startPosition){
                        motor.setPower(0.33);
                    }
                }
                motor.setPower(0);
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                telemetryPacket.put("status", "reached position");
            }
            if (gamepad1.x) {
                motor.setTargetPosition((int)((-10 * SLIDE_TICKS_PER_INCH)+startPosition));
                motor.setPower(0.6);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while(motor.isBusy() && gamepad1.atRest()){
                    telemetryPacket.put("status", "going to position");
                    dashboard.sendTelemetryPacket(telemetryPacket);
                }
                motor.setPower(0);
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                telemetryPacket.put("status", "reached position");
            }
            motor.setPower((motor.getCurrentPosition() < 0)?((motor.getCurrentPosition() > MAX_SLIDE_EXTENSION+startPosition) ? (gamepad1.left_stick_y):(Math.max(gamepad1.left_stick_y, 0))):(Math.min(0, gamepad1.left_stick_y)));
        }
    }
}
