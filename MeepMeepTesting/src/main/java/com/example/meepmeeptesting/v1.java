package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class v1 {
    /*
    INTO THE DEEP: https://www.youtube.com/watch?v=ewlDPvRK4U4
    Start neutral side.
    Strategy: Try to pickup all 3 samples from neutral side and drop into net zone (bottom corner) no basket
     */

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-16, -60, Math.toRadians(90)))
                        // move to first yellow sample
                        .splineToConstantHeading(new Vector2d(-68, -30), Math.toRadians(90))
                        .addDisplacementMarker(()->{
                            // claw to pickup position
                            // claw close on sample
                        })
                        // move to base
                        .splineTo(new Vector2d(-60, -60), Math.toRadians(90))
                        .addDisplacementMarker(()->{
                            // open claw to drop sample
                        })
                        // move to second yellow sample
                        .splineTo(new Vector2d(-60, -30), Math.toRadians(90))
                        .addDisplacementMarker(()->{
                            // claw to pickup position
                            // claw close on sample
                        })
                        // move to base
                        .splineTo(new Vector2d(-60, -60), Math.toRadians(90))
                        .addDisplacementMarker(()->{
                            // open claw to drop sample
                        })
                        // third yellow sample
                        .splineTo(new Vector2d(-48, -30), Math.toRadians(90))
                        .addDisplacementMarker(()->{
                            // claw to pickup position
                            // claw close on sample
                        })
                        .splineTo(new Vector2d(-60, -60), Math.toRadians(90))
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
