package org.firstinspires.ftc.teamcode;

public class Constants {
    public static final double SLIDE_MOTOR_POWER = 0.5;
    public static final double TICKS_PER_REV = 537.7;
    public static final double MAX_RPM = 312;

    public static final double SLIDE_TICKS_PER_INCH = 113.64;
    public static final int MAX_SLIDE_EXTENSION = -3740;


    public static final double HORIZONTAL_CLAW_CLOSE_POS = 0.7594;
    public static final double HORIZONTAL_CLAW_OPEN_POS = 0.4717;
    public static final double HORIZONTAL_CLAW_AIM_POS_PITCH = 0.78; // claw points down for aiming

    public static final double HORIZONTAL_CLAW_PICKUP_POS_PITCH = 0.5544; // claw aims to down
    public static final double HORIZONTAL_CLAW_TRANSFER_POS_PITCH = 0.001; // for for transfer
    public static final double HORIZONTAL_CLAW_IDLE_YAW = 0.1833;
    public static final double HORIZONTAL_CLAW_VERTICALSAMPLE_YAW = HORIZONTAL_CLAW_IDLE_YAW;
    public static final double HORIZONTAL_CLAW_HORIZONTALSAMPLE_YAW = 0.4956;


    public static final double ARM_STAGE1_DEG = 130; // transfer
    public static final double ARM_STAGE2_DEG = 200; // aiming
    public static final double ARM_STAGE3_DEG = 235; // pickup

}
