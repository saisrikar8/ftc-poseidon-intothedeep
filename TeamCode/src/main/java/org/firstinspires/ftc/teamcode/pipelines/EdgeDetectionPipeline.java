package org.firstinspires.ftc.teamcode.pipelines;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class EdgeDetectionPipeline extends OpenCvPipeline {
    private Mat grayed = new Mat();
    private Mat blurred = new Mat();
    private Mat edges = new Mat();
    public double angle = 0;
    @Override
    public Mat processFrame(Mat input) {
        if (input == null || input.empty()) {
            return null; // Safeguard against undefined input Mat
        }
        Imgproc.cvtColor(input, grayed, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(grayed, grayed);
        Imgproc.GaussianBlur(grayed, blurred, new Size(9,9), 0);


        double lowerThreshold = 40;
        double upperThreshold = 170;

        Imgproc.Canny(blurred, edges, lowerThreshold, upperThreshold);
        // Find contours from the binary edge image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.dilate(edges, edges, kernel);
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.erode(edges, edges, kernel);
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        Point imageCenter = new Point(input.width() / 2, input.height() / 2);
        double minDistance = Double.MAX_VALUE;
        MatOfPoint minContour = null;
        RotatedRect closestRect = null;

        // Process each contour and find the closest minAreaRect
        for (MatOfPoint contour : contours) {
            RotatedRect minRect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));
            Point rectCenter = minRect.center;
            double distance = Math.sqrt(Math.pow(rectCenter.x - imageCenter.x, 2) + Math.pow(rectCenter.y - imageCenter.y, 2));

            boolean check1 = minRect.size.width > 200 && minRect.size.width < 300 || minRect.size.height > 550 && minRect.size.height < 650;
            boolean check2 = minRect.size.height > 200 && minRect.size.height < 300 || minRect.size.width > 550 && minRect.size.width < 650;

            if (distance < minDistance && /*(check1||check2) &&*/ minRect.size.height*minRect.size.width > 100000  && minRect.size.height*minRect.size.width < 200000) {
                minDistance = distance;
                closestRect = minRect;
                minContour = contour;
            }
        }

        // If we found the closest rectangle, draw it and display its angle
        if (closestRect != null) {
            Point[] points = new Point[4];
            angle = closestRect.angle;
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
