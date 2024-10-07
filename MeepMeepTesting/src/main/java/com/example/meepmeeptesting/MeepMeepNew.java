package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepNew {
    public static void main(String[] args) {
        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        MeepMeep meepMeep = new MeepMeep(800);
        Vector2d COLLECTION_AREA_VECTOR = new Vector2d(-58, 60);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Required: Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                // Option: Set theme. Default = ColorSchemeRedDark()
                .setColorScheme(new ColorSchemeRedDark())
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-58, 45, Math.toRadians(-90)))
                                // GET MIDDLE SAMPLE, PUT BACK INTO COLLECTION AREA
                                .forward(10)
                                .addDisplacementMarker(() -> {
                                    // claw get sample, transfer to second claw
                                })
                                .waitSeconds(1) // simulate hardware movement time
                                .lineTo(COLLECTION_AREA_VECTOR) // go back to the collection area
                                .addDisplacementMarker(() -> {
                                    // second claw to drop sample into collection area
                                })
                                .waitSeconds(1) // simulate hardware movement time

                                // GET RIGHTMOST SAMPLE, PUT BACK INTO COLLECTION AREA
                                .lineTo(new Vector2d(-68, 37))
                                .addDisplacementMarker(() -> {
                                    // claw get sample
                                })
                                .waitSeconds(1)
                                .lineTo(COLLECTION_AREA_VECTOR)
                                .addDisplacementMarker(() -> {
                                    // drop sample into collection area
                                })
                                .waitSeconds(1)

                                // GET LEFTMOST SAMPLE, PUT BACK INTO COLLECTION AREA
                                .lineTo(new Vector2d(-52, 37))
                                .addDisplacementMarker(() -> {
                                    // claw get sample
                                })
                                .waitSeconds(1)
                                .lineTo(COLLECTION_AREA_VECTOR)
                                .addDisplacementMarker(() -> {
                                    // drop sample into collection area
                                })
                                .build()
                );


        // Set field image
        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                // Background opacity from 0-1
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
