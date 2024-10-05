package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        RoadRunnerBotEntity redTop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(53, 53, Math.toRadians(180), Math.toRadians(180), 12.3)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-26, -55, Math.toRadians(90)))
                                //moving to basket area
                                .splineTo(new Vector2d(-58.5, -46.2), Math.toRadians(300))
                                //grabbing first sample and bringing it onto vertical slide
                                .waitSeconds(3)
                                //turn to check second sample is present
                                .turn(Math.toRadians(-30))
                                //Placing first sample in basket after turning and then grabbing
                                //second sample and placing second sample in basket
                                .waitSeconds(5)
                                //move to spot to grab third sample and be ready to quickly move to
                                //substation to get human player ready for clipping sample
                                .splineTo(new Vector2d(-36,-35), Math.toRadians(320))
                                // grabbing third sample
                                .waitSeconds(3)
                                //moving to position for taking a piece from the submersible
                                .splineTo(new Vector2d(23, -46), Math.toRadians(140))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(redTop)
                .start();
    }
}
