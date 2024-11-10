package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "Auto Chassis Testing")
public class ChassisTesting extends LinearOpMode {
    MecanumDrive drive;

    public void runOpMode() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

        TrajectoryActionBuilder traj1 = drive.actionBuilder(new Pose2d(0, 0, 0))
                .strafeTo(new Vector2d(10, 0));
        TrajectoryActionBuilder traj2 = drive.actionBuilder(new Pose2d(10, 0, 0))
                .strafeTo(new Vector2d(-10, 0));
        TrajectoryActionBuilder traj3 = drive.actionBuilder(new Pose2d(-10, 0, 0))
                .strafeTo(new Vector2d(0, 0));
        TrajectoryActionBuilder traj4 = drive.actionBuilder(new Pose2d(0, 0, 0))
                .splineTo(new Vector2d(10, 10), 0);
        TrajectoryActionBuilder traj5 = drive.actionBuilder(new Pose2d(10, 10, 0))
                .splineTo(new Vector2d(-10, -10), 0);
        TrajectoryActionBuilder traj6 = drive.actionBuilder(new Pose2d(-10, -10, 0))
                .splineTo(new Vector2d(0, 0), 0);

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        traj1.build(),
                        traj2.build(),
                        traj3.build(),
                        traj4.build(),
                        traj5.build(),
                        traj6.build()
                )
        );
        }
}
