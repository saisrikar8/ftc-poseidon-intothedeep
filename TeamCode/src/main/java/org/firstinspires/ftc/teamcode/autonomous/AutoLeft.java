package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "AutoLeft")
public class AutoLeft extends LinearOpMode{
    public final int FLOOR = 0;
    public final int LOWPOLE = 1625;
    public final int MIDPOLE = 2725;
    public final int HIPOLE = 3860;
    @Override
    public void runOpMode() throws InterruptedException {
        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(33)
                .build();
        Trajectory traj2 = drive.trajectoryBuilder(traj1.end())
                .forward(55)
                .addDisplacementMarker(10, () -> {
                    drive.moveSlide(HIPOLE);
                    // This marker runs 20 inches into the trajectory

                    // Run your action in here!
                })
                .build();
        Trajectory traj3 = drive.trajectoryBuilder(traj2.end())
                .strafeRight(16)
                .build();
        Trajectory traj4 = drive.trajectoryBuilder(traj3.end())
                .back(1)
                .build();
        Trajectory traj5 = drive.trajectoryBuilder(traj4.end().plus(new Pose2d(0, 0, Math.toRadians(-90))))
                .forward(36)
                .build();
        waitForStart();

        if (isStopRequested()) return;

        drive.closeClaw();
        sleep(1000);
        drive.moveSlide(200);
        drive.followTrajectory(traj1);
        drive.followTrajectory(traj2);
        drive.followTrajectory(traj3);
        drive.releaseClaw();
        drive.followTrajectory(traj4);
        drive.turn(Math.toRadians(-90));
        drive.moveSlide(600);
        drive.followTrajectory(traj5);
        drive.closeClaw();
        sleep(1000);
        drive.moveSlide(1625);


        while (!isStopRequested() && opModeIsActive()) ;
    }
}