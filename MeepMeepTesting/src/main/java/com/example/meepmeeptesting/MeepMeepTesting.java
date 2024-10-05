package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        RoadRunnerBotEntity blueTop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(53, 53, Math.toRadians(180), Math.toRadians(180), 12.3)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-60, 10, 0))
                                .forward(30)
                                .waitSeconds(1)
                                .turn(Math.toRadians(90))
                                .strafeRight(10)
                                .waitSeconds(1)
                                .strafeLeft(30)
                                .forward(30)
                                .strafeRight(20)
                                .waitSeconds(1)
                                .strafeRight(5)
                                .waitSeconds(1)
                                .strafeRight(5)
                                .build()
                );
        RoadRunnerBotEntity redTop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(53, 53, Math.toRadians(180), Math.toRadians(180), 12.3)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(10, -70, Math.toRadians(90)))
                                .forward(30)
                                .waitSeconds(1)
                                .turn(Math.toRadians(-90))
                                .strafeLeft(10)
                                .waitSeconds(1)
                                .strafeRight(10)
                                .turn(Math.toRadians(-180))
                                .waitSeconds(1)
                                .strafeRight(10)
                                .strafeLeft(30)
                                .turn(Math.toRadians(-180))
                                .forward(30)
                                .strafeLeft(20)
                                .waitSeconds(1)
                                .strafeLeft(5)
                                .waitSeconds(1)
                                .strafeLeft(5)
                                .waitSeconds(1)
                                .strafeLeft(90)
                                .back(95)
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(redTop)
                .start();
    }
}
