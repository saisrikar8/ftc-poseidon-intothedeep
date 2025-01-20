package org.firstinspires.ftc.teamcode.pipelines;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class SampleDetectionPipeline extends OpenCvPipeline {
    @Override
    public Mat processFrame(Mat input) {
        EdgeDetectionPipeline edgeDetectionPipeline = new EdgeDetectionPipeline();
        Mat edges = edgeDetectionPipeline.processFrame(input);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Point imageCenter = new Point(edges.width() / 2, edges.height() / 2);

        double minDistance = Double.MAX_VALUE;
        RotatedRect closestRect = null;

        for (MatOfPoint contour : contours) {
            if (Imgproc.contourArea(contour) > 500) {
                RotatedRect minRect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

                Point rectCenter = minRect.center;

                double distance = Math.sqrt(Math.pow(rectCenter.x - imageCenter.x, 2) + Math.pow(rectCenter.y - imageCenter.y, 2));

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
                Imgproc.line(edges, points[i], points[(i + 1) % 4], new Scalar(0, 255, 0), 2);
            }

            double angle = closestRect.angle;
            if (closestRect.size.width < closestRect.size.height) {
                angle += 90;
            }

            Imgproc.putText(edges, "Angle: " + angle, new Point(closestRect.center.x - 50, closestRect.center.y),
                    Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
        }

        return edges;
    }
}
