package org.firstinspires.ftc.teamcode.pipelines;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class EdgeDetectionPipeline extends OpenCvPipeline {
    private Mat grayed;
    private Mat blurred;
    private Mat edges;
    @Override
    public Mat processFrame(Mat mat) {
        Imgproc.cvtColor(mat, grayed, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(grayed, blurred, new Size(5,5), 0);
        double lowerThreshold = 50;
        double upperThreshold = 150;

        Imgproc.Canny(blurred, edges, lowerThreshold, upperThreshold);

        return edges;
    }
}
