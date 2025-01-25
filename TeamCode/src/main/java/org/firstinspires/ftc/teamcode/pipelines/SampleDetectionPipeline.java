package org.firstinspires.ftc.teamcode.pipelines;

import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.opencv.core.Core;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.*;

public class SampleDetectionPipeline extends OpenCvPipeline {
    private Mat hsvImage = new Mat();
    private Mat maskYellow = new Mat();
    private Mat maskRed = new Mat();
    private Mat maskBlue = new Mat();
    private Mat combinedMask = new Mat();
    private Mat hierarchy = new Mat();

    private static final Scalar BLUE_LOWER = new Scalar(90, 50, 50); // HSV range for blue
    private static final Scalar BLUE_UPPER = new Scalar(130, 255, 255);

    private static final Scalar YELLOW_LOWER = new Scalar(20, 100, 100); // HSV range for yellow
    private static final Scalar YELLOW_UPPER = new Scalar(30, 255, 255);

    private static final Scalar RED_LOWER1 = new Scalar(0, 100, 100); // HSV range for red (lower part)
    private static final Scalar RED_UPPER1 = new Scalar(10, 255, 255);
    private static final Scalar RED_LOWER2 = new Scalar(160, 100, 100); // HSV range for red (upper part)
    private static final Scalar RED_UPPER2 = new Scalar(180, 255, 255);
    private Mat maskRed1 = new Mat();
    private Mat maskRed2 = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, hsvImage, Imgproc.COLOR_RGB2HSV);

        Core.inRange(hsvImage, YELLOW_LOWER, YELLOW_UPPER, maskYellow); // Yellow
        Core.inRange(hsvImage, RED_LOWER1, RED_UPPER1, maskRed1); // Red (lower range)
        Core.inRange(hsvImage, RED_LOWER2, RED_UPPER2, maskRed2); // Red (upper range)
        Core.add(maskRed1, maskRed2, maskRed); // Combine both red ranges
        Core.inRange(hsvImage, BLUE_LOWER, BLUE_UPPER, maskBlue); // Blue

        hsvImage.release();
        maskRed1.release();
        maskRed2.release();


        Core.bitwise_or(maskYellow, maskRed, combinedMask);
        Core.bitwise_or(combinedMask, maskBlue, combinedMask);

        maskYellow.release();
        maskBlue.release();
        maskRed.release();


        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(combinedMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        Point imageCenter = new Point(input.width() / 2.0, input.height() / 2.0);
        RotatedRect closestRect = null;
        double minDistance = Double.MAX_VALUE;

        for (MatOfPoint contour : contours) {

            if (Imgproc.contourArea(contour) > 5000 && Imgproc.contourArea(contour) < 200000) {


                RotatedRect minRect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

                double distance = Math.sqrt(Math.pow(minRect.center.x - imageCenter.x, 2) +
                        Math.pow(minRect.center.y - imageCenter.y, 2));

                if (distance < minDistance) {
                    minDistance = distance;
                    closestRect = minRect;
                }

            }
        }

        if (closestRect != null) {
            Point[] points = new Point[4];
            closestRect.points(points);

            for (int i = 0; i < 4; i++) {
                Imgproc.line(input, points[i], points[(i + 1) % 4], new Scalar(0, 255, 0), 2);
            }

            double angle = closestRect.angle;
            if (closestRect.size.width < closestRect.size.height) {
                angle += 90;
            }

            Imgproc.putText(input, "Angle: " + angle, new Point(closestRect.center.x - 50, closestRect.center.y),
                    Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
            Imgproc.putText(input, "Width: " + closestRect.size.width, new Point(closestRect.center.x - 50, closestRect.center.y-50),
                    Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
            Imgproc.putText(input, "Height: " + closestRect.size.height, new Point(closestRect.center.x - 50, closestRect.center.y-100),
                    Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
        }
        return input;
    }
}
