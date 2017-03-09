package com.example.aishwarya.monvoix;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class cameraview extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    JavaCameraView javaCameraView;
    private static String TAG="MainActivity";
    Button save;
    Mat mRgba,imgGray,imgCanny;

    BaseLoaderCallback mLoaderCallBack=new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {

                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraview);
        save = (Button) findViewById(R.id.save);
        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bitmap bmp = null;
                try {
                    bmp = Bitmap.createBitmap(imgGray.cols(), imgGray.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(imgGray, bmp);
                } catch (CvException e) {
                    Log.d(TAG, e.getMessage());
                }

                imgGray.release();


                FileOutputStream out = null;

                String filename = "frame.png";


                File sd = new File("/storage/emulated/0/Download/Camera_app");
                boolean success = true;
                if (!sd.exists()) {
                    success = sd.mkdir();
                }
                if (success) {
                    File dest = new File(sd, filename);

                    try {
                        out = new FileOutputStream(dest);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                                Log.d(TAG, "OK!!");
                            }
                        } catch (IOException e) {
                            Log.d(TAG, e.getMessage() + "Error");
                            e.printStackTrace();
                        }
                    }
                }
            }

        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (OpenCVLoader.initDebug()) {

            Log.i(TAG,"opencv loaded successfully");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
        else{
            Log.i(TAG,"Opencv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9,this,mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
mRgba=new Mat(height,width, CvType.CV_8UC4);
        imgGray=new Mat(height,width, CvType.CV_8UC4);
        imgCanny=new Mat(height,width, CvType.CV_8UC4);


    }

    @Override
    public void onCameraViewStopped() {
mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba=inputFrame.rgba();
        Imgproc.cvtColor(mRgba,imgGray,Imgproc.COLOR_RGB2GRAY);
        return imgGray;
    }
}
