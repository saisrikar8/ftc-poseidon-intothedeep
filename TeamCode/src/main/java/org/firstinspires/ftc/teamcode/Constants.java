package org.firstinspires.ftc.teamcode;

public class Constants {
    public static final double SLIDE_MOTOR_POWER = 0.5;
    public static final double TICKS_PER_REV = 537.7;
    public static final double MAX_RPM = 312;

    public static final double SLIDE_TICKS_PER_INCH = 113.64;
    public static final int MAX_SLIDE_EXTENSION = -3894;

    public static final int MAX_TWO_STAGE_EXTENSION = -1990;

    public static final double VERTICAL_CLAW_TRANSFER_PITCH = 0.2828;
    public static final double VERTICAL_CLAW_IDLE_PITCH = 0.9567;

    public static final double HORIZONTAL_CLAW_CLOSE_POS = 0.8228;
    public static final double HORIZONTAL_CLAW_OPEN_POS = 0.5689;
    public static final double HORIZONTAL_CLAW_AIM_POS_PITCH = 0.7139; // claw points down for aiming

    public static final double HORIZONTAL_CLAW_PICKUP_POS_PITCH = 0.5494; // claw aims to down
    public static final double HORIZONTAL_CLAW_TRANSFER_POS_PITCH = 0; // for for transfer
    public static final double HORIZONTAL_CLAW_IDLE_YAW = 0.1833;
    public static final double HORIZONTAL_CLAW_VERTICALSAMPLE_YAW = HORIZONTAL_CLAW_IDLE_YAW;
    public static final double HORIZONTAL_CLAW_HORIZONTALSAMPLE_YAW = 0.4956;

    public static final double VERTICAL_CLAW_OPEN_CLAWPOS = 0;
    public static final double VERTICAL_CLAW_CLOSE_CLAWPOS = 0.0167;
    public static final double ARM_STAGE1_DEG = 130; // transfer
    public static final double ARM_STAGE2_DEG = 200; // aiming
    public static final double ARM_STAGE3_DEG = 235; // pickup

}
