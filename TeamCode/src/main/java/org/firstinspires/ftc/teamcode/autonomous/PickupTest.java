package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Claw;
import org.firstinspires.ftc.teamcode.Elevator;
import org.firstinspires.ftc.teamcode.HorizontalArmRotator;

@Autonomous(name = "Pickup Test")
public class PickupTest extends LinearOpMode {
    int armDegrees = 210;

    DcMotor vertical1, vertical2;
    DcMotor horizontal1, horizontal2;

    Elevator elevator;
    HorizontalArmRotator arm;

    Claw claw, claw2;

    Servo armRotator;

    Servo horizontalClaw, horizontalClawRotator;

    Servo verticalClaw, verticalClawRotator;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket telemetryPacket = new TelemetryPacket();


    @Override
    public void runOpMode() throws InterruptedException {
        vertical1 = hardwareMap.get(DcMotor.class, "vertical-slide-1");
        vertical2 = hardwareMap.get(DcMotor.class, "vertical-slide-2");

        horizontal1 = hardwareMap.get(DcMotor.class, "horizontal-slide-1");
        horizontal2 = hardwareMap.get(DcMotor.class, "horizontal-slide-2");
        armRotator = hardwareMap.get(Servo.class, "arm-rotator");

        horizontalClaw = hardwareMap.get(Servo.class, "horizontal-claw");
        horizontalClawRotator = hardwareMap.get(Servo.class, "horizontal-claw-rotator");

        verticalClaw = hardwareMap.get(Servo.class, "vertical-claw");
        verticalClawRotator = hardwareMap.get(Servo.class, "vertical-claw-rotator");

        vertical1.setDirection(DcMotorSimple.Direction.REVERSE);

        vertical1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vertical2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        elevator = new Elevator(vertical1, vertical2);
        arm = new HorizontalArmRotator(horizontal1, horizontal2, armRotator);
        claw = new Claw(horizontalClaw, horizontalClawRotator);
        claw2 = new Claw(verticalClaw, verticalClawRotator);

        telemetry.addData("Stage", "Waiting");
        waitForStart();
        telemetry.addData("Stage", "Closing arm");

        // open claw
        returnHorizontalToPickupPosition();
        Thread.sleep(2000);

        // TODO: set slides to lower, and claw to good pos
        //Actions.runBlocking(new ParallelAction(elevator.moveToPosition(VERTICAL_1_LOWERED_INITIAL), openVerticalClaw()));
        //Thread.sleep(2000);

        // set vertical claw to open and waiting
        Actions.runBlocking(verticalClawToPickupPosition());
        // arm up
        returnHorizontalToInitialPosition();
        Thread.sleep(2000);
        // close vertical claw
        Actions.runBlocking(closeVerticalClaw());
        // open horizontal claw
        // arm back to pickup position
    }

    public void returnHorizontalToInitialPosition() throws InterruptedException {
        armDegrees = 100;
        Actions.runBlocking(claw.setClawPosition(0.49));
        Thread.sleep(250);
        Actions.runBlocking(new ParallelAction(arm.setOrientation(armDegrees), claw.setClawPitch(0.3)));
    }

    public void returnHorizontalToPickupPosition() {
        armDegrees = 210;
        Actions.runBlocking(new ParallelAction(arm.setOrientation(armDegrees), claw.setClawPitch(0.99), claw.setClawPosition(0.7)));
    }

    public Action openHorizontalClaw() {
        return claw.setClawPosition(0.7);
    }

    public Action verticalClawToPickupPosition() {
        return new ParallelAction(claw2.setClawPosition(0.5), claw2.setClawPitch(0.0806));
    }

    public Action closeVerticalClaw() {
        return claw2.setClawPosition(0.7);
    }

}
