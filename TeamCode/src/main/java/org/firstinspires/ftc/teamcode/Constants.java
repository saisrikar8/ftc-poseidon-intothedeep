package org.firstinspires.ftc.teamcode;

public class Constants {
    public static final double SLIDE_MOTOR_POWER = 0.5;
    public static final double TICKS_PER_REV = 537.7;
    public static final double MAX_RPM = 312;

    public static final double SLIDE_TICKS_PER_INCH = 113.64;
    public static final int MAX_SLIDE_EXTENSION = -3894;

    public static final int MAX_TWO_STAGE_EXTENSION = -1990;

    public static final double VERTICAL_CLAW_TRANSFER_PITCH = 0.3906;
    public static final double VERTICAL_CLAW_DROP_PITCH = 1;

    public static final double HORIZONTAL_CLAW_CLOSE_POS = 0.8;
    public static final double HORIZONTAL_CLAW_OPEN_POS = 0.5689;
    public static final double HORIZONTAL_CLAW_AIM_POS_PITCH = 0.8633; // claw points down for aiming
    public static final double HORIZONTAL_CLAW_PICKUP_POS_PITCH = 0.6334; // claw aims to down
    public static final double HORIZONTAL_CLAW_TRANSFER_POS_PITCH = 0.0283; // for for transfer
    public static final double HORIZONTAL_CLAW_IDLE_YAW = 0.4456;
    public static final double HORIZONTAL_CLAW_VERTICALSAMPLE_YAW = HORIZONTAL_CLAW_IDLE_YAW;
    public static final double HORIZONTAL_CLAW_HORIZONTALSAMPLE_YAW = 0.7539;

    public static final double VERTICAL_CLAW_OPEN_CLAWPOS = 0;
    public static final double VERTICAL_CLAW_CLOSE_CLAWPOS = 0.1467;
    public static final double ARM_STAGE1_DEG = 127; // transfer
    public static final double ARM_STAGE2_DEG = 205; // aiming
    public static final double ARM_STAGE3_DEG = 234; // pickup

}
