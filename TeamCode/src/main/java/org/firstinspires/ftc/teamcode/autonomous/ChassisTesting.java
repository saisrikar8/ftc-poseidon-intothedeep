package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TimeTurn;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.MecanumDrive.TurnAction;

@Autonomous(name = "Auto Chassis Testing")
public class ChassisTesting extends LinearOpMode {
    MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

    public void runOpMode() {
        waitForStart();
        TrajectoryActionBuilder traj1 = null;
        TrajectoryActionBuilder traj2 = null;
        TrajectoryActionBuilder traj3 = null;
        TrajectoryActionBuilder traj4 = null;
        TrajectoryActionBuilder traj5 = null;
        TrajectoryActionBuilder traj6 = null;
        while (!isStopRequested() && !opModeIsActive()) {
            // init code here
            traj1 = drive.actionBuilder(new Pose2d(0, 0, 0))
                    .strafeTo(new Vector2d(10, 0));

            traj2 = drive.actionBuilder(new Pose2d(10, 0, 0))
                    .strafeTo(new Vector2d(-10, 0));

            traj3 = drive.actionBuilder(new Pose2d(-10, 0, 0))
                    .strafeTo(new Vector2d(0, 0));

            traj4 = drive.actionBuilder(new Pose2d(0, 0, 0))
                    .splineTo(new Vector2d(10, 10), 0);

            traj5 = drive.actionBuilder(new Pose2d(10, 10, 0))
                    .splineTo(new Vector2d(-10, -10), 0);

            traj6 = drive.actionBuilder(new Pose2d(-10, -10, 0))
                    .splineTo(new Vector2d(0, 0), 0);

        }

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
