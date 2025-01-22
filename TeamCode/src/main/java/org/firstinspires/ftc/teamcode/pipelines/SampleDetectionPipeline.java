package org.firstinspires.ftc.teamcode.pipelines;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class SampleDetectionPipeline extends OpenCvPipeline {
    private Mat grayed = new Mat();
    private Mat blurred = new Mat();
    private Mat edges = new Mat();
    @Override
    public Mat processFrame(Mat input) {
        if (input == null || input.empty()) {
            return null; // Safeguard against undefined input Mat
        }
        Imgproc.cvtColor(input, grayed, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(grayed, blurred, new Size(5,5), 0);
        double lowerThreshold = 50;
        double upperThreshold = 150;

        Imgproc.Canny(blurred, edges, lowerThreshold, upperThreshold);
        // Find contours from the binary edge image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Process each contour
        for (MatOfPoint contour : contours) {
            // Calculate the minimum area rectangle
            if (Imgproc.contourArea(contour) > 450) { // Filter by minimum area
                RotatedRect minRect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

                // Get the angle and adjust if needed
                double angle = minRect.angle;
                if (minRect.size.width < minRect.size.height) {
                    angle += 90;
                }

                // Get the points of the rectangle
                Point[] points = new Point[4];
                minRect.points(points);

                // Draw the rectangle
                for (int i = 0; i < 4; i++) {
                    Imgproc.line(input, points[i], points[(i + 1) % 4], new Scalar(0, 255, 0), 2);
                }

                // Draw the angle text
                Imgproc.putText(input, "Angle: " + angle, new Point(minRect.center.x - 50, minRect.center.y),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
            }
        }

        // Return the processed image with contours and rectangles drawn
        return input;
    }
}
