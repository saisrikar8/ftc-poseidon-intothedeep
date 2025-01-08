package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    public Servo claw, clawRotator, armRotator;

    public Claw(Servo clawServo, Servo clawRotater) {
        claw = clawServo;
        clawRotator = clawRotater;
    }


    public class SetOrientation implements Action {
        private double orientation;

        public SetOrientation(double degrees) {
            orientation = degrees / 360;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (clawRotator != null) clawRotator.setPosition(orientation);
            return false;
        }
    }

    public class SetClawPosition implements Action {
        double position;

        public SetClawPosition(double clawPos) {
            this.position = clawPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            claw.setPosition(position);
            return false;
        }
    }

    public class SetClawRotatorPosition implements Action {
        double position;

        public SetClawRotatorPosition(double clawPos) {
            this.position = clawPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            clawRotator.setPosition(position);
            return false;
        }
    }


    public class SetArmRotatorPosition implements Action {
        private double position;

        public SetArmRotatorPosition(double degrees) {
            double orientation = (degrees / 300);
            this.position = 1 - orientation;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            armRotator.setPosition(position);
            return false;
        }
    }

    public Action setOrientation(double degrees) {
        return new SetOrientation(degrees);
    }

    public Action setClawPosition(double position) {
        return new SetClawPosition(position);
    }

    public Action setClawRotatorPosition(double position) {
        return new SetClawRotatorPosition(position);
    }

    public Action setArmRotatorOrientation(double position) {
        return new SetArmRotatorPosition(position);
    }
}
