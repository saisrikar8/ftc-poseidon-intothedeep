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
                                /*
                                We start with one sample on the robot, so our plan is:
                                bring first sample to high basket
                                grab another sample from the 3 samples row and put it into the high basket
                                take the other 2 samples remaining from the row and put it into the low basket
                                 */
                                .splineTo(new Vector2d(-58.5, -46.2), Math.toRadians(50)) // go to the basket w the sample
                                .addDisplacementMarker(() -> {
                                    // load sample into second claw
                                    // extend claw
                                    // drop sample claw into high basket
                                }).waitSeconds(1)
                                // turn left to face the middle sample
                                .turn(Math.toRadians(40))
                                .addDisplacementMarker(() -> {
                                    // extend claw to get middle sample
                                    // move to second claw
                                }).waitSeconds(1)
                                .turn(Math.toRadians(-40))
                                .addDisplacementMarker(() -> {
                                    // extend second claw
                                    // drop sample into high basket
                                })
                                // Placing first sample in basket after turning and then grabbing
                                // second sample and placing second sample in basket
                                // move to spot to grab third sample and be ready to quickly move to
                                // substation to get human player ready for clipping sample
                                .splineTo(new Vector2d(-36, -35), Math.toRadians(150))
                                // grabbing third sample
                                .waitSeconds(3)
                                // moving to position for taking a piece from the submersible
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
