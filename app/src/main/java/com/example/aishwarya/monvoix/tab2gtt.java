package com.example.aishwarya.monvoix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import static android.R.attr.value;
import static android.app.Activity.RESULT_OK;
import static com.example.aishwarya.monvoix.R.id.button;
import static com.example.aishwarya.monvoix.R.id.imageView;
import static com.example.aishwarya.monvoix.R.id.image_view;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

/**
 * Created by aishwarya on 19/02/17.
 */
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Mat;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class tab2gtt extends Fragment {
    private File imageFile;
    Button btn;
    ImageView mimageView;
    static final int CAM_REQUEST=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2gtt, container, false);
btn=(Button)rootView.findViewById(R.id.button3);
        mimageView=(ImageView)rootView.findViewById(R.id.image_view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File file=getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));

                startActivityForResult(camera_intent,CAM_REQUEST);
            }
        });


        return rootView;
    }
private File getFile() {
    File folder = new File("/storage/emulated/0/Download/Camera_app");
    if (!folder.exists()) {
        folder.mkdir();
    }
    File image_file = new File(folder, "cam_image.jpg");
  /*  Mat source = Imgcodecs.imread("cam_image.jpg",
            Imgcodecs.CV_LOAD_IMAGE_COLOR);

    Mat destination = new Mat(source.rows(),source.cols(),source.type());

    Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);

    Imgcodecs.imwrite("grayscale.jpg", destination);

    Mat source2 = Imgcodecs.imread("grayscale.jpg",
            Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

    Mat destination2 = new Mat(source.rows(),source.cols(),source.type());

    Imgproc.adaptiveThreshold(source2, destination2, 255,
            Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 4);
*/
    return image_file;
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path="/storage/emulated/0/Download/Camera_app/cam_image.jpg";

mimageView.setImageDrawable(Drawable.createFromPath(path));

    }
}