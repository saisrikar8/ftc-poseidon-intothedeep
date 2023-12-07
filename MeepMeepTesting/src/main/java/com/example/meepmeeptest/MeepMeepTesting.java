package com.example.meepmeeptest;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        RoadRunnerBotEntity blueTop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(53, 53, Math.toRadians(180), Math.toRadians(180), 12.3)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-70, 10, 0))
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
                        drive.trajectorySequenceBuilder(new Pose2d(70, 10, Math.toRadians(180)))
                                .forward(30)
                                .waitSeconds(1)
                                .turn(Math.toRadians(-90))
                                .strafeLeft(10)
                                .waitSeconds(1)
                                .strafeRight(30)
                                .forward(30)
                                .strafeLeft(20)
                                .waitSeconds(1)
                                .strafeLeft(5)
                                .waitSeconds(1)
                                .strafeLeft(5)
                                .waitSeconds(1)
                                .strafeLeft(19)
                                .back(90)
                                .lineTo(new Vector2d(34, -59))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_LIGHT)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(redTop)
                .start();
    }
}