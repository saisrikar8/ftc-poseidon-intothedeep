package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    public Servo claw, clawPitch, armRotator, clawYaw;

    public Claw(Servo clawServo, Servo clawRotater) {
        claw = clawServo;
        clawPitch = clawRotater;
    }

    public Claw(Servo claw, Servo clawPitch, Servo clawYaw) {
        this.claw = claw;
        this.clawPitch = clawPitch;
        this.clawYaw = clawYaw;
    }

    public class SetOrientation implements Action {
        private double orientation;

        public SetOrientation(double degrees) {
            orientation = degrees / 360;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (clawPitch != null) clawPitch.setPosition(orientation);
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

    public class SetClawPitch implements Action {
        double position;

        public SetClawPitch(double clawPos) {
            this.position = clawPos;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            clawPitch.setPosition(position);
            return false;
        }
    }

    public class SetClawYaw implements Action {
        double position;

        SetClawYaw(double clawPosition) {
            this.position = clawPosition;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            clawYaw.setPosition(position);
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

    public Action setClawPitch(double position) {
        return new SetClawPitch(position);
    }

    public Action setClawYaw(double position) {
        return new SetClawYaw(position);
    }

    public Action setArmRotatorOrientation(double position) {
        return new SetArmRotatorPosition(position);
    }
}
