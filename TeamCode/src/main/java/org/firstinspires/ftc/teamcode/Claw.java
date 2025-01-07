package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    public Servo leftRotator, rightRotator;
    public Servo claw;


    public Claw(Servo clawServo, Servo rotator1, Servo rotator2){
        claw = clawServo;
        leftRotator = rotator1;
        rightRotator = rotator2;
    }
    public class SetOrientation implements Action{
        private double orientation;
        public SetOrientation(double degrees){
            orientation = degrees/360;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (leftRotator != null) {
                leftRotator.setPosition(1 - orientation);
            }
            if (rightRotator != null) {
                rightRotator.setPosition(orientation);
            }
            return false;
        }
    }
    public class SetClawPosition implements Action{
        double position;
        public SetClawPosition(double clawPos){
            this.position = clawPos;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            claw.setPosition(position);
            return false;
        }
    }
    public Action setOrientation(double degrees){
        return new SetOrientation(degrees);
    }
    public Action setClawPosition(double position){
        return new SetClawPosition(position);
    }
}
