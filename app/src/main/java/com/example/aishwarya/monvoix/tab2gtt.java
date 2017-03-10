package com.example.aishwarya.monvoix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.util.Locale;

import static android.R.attr.height;
import static android.R.attr.path;
import static android.R.attr.type;
import static android.R.attr.value;
import static android.R.attr.width;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.aishwarya.monvoix.R.id.button;
import static com.example.aishwarya.monvoix.R.id.image;
import static com.example.aishwarya.monvoix.R.id.imageView;
import static com.example.aishwarya.monvoix.R.id.image_view;
import static com.example.aishwarya.monvoix.R.id.toolbar;
import static org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.adaptiveThreshold;


/**
 * Created by aishwarya on 19/02/17.
 */
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Mat;

//import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class tab2gtt extends Fragment implements TextToSpeech.OnInitListener {
   // private File imageFile;
    Button btn;
    ImageView mimageView;
    static final int CAM_REQUEST = 1;
    JavaCameraView javaCameraView;
    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    Mat gray;
    Mat result;


private static String TAG="MainActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2gtt, container, false);
        btn = (Button) rootView.findViewById(R.id.button3);
        mimageView = (ImageView) rootView.findViewById(R.id.image_view);
        Button speakButton = (Button) rootView.findViewById(R.id.button2);
        final EditText enteredText = (EditText) rootView.findViewById(R.id.editText4);

        //listen for clicks
        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String words = enteredText.getText().toString();
                speakWords(words);
            }
        });

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //  Intent intent = new Intent(getActivity(), cameraview.class);
                //    startActivity(intent);

                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);

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

   //     Mat imgGray=new Mat(height,width,CvType.CV_8UC1);



        return image_file;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==CAM_REQUEST) {
           //String path = "/storage/emulated/0/Download/Camera_app/cam_image.jpg";
           Mat m= Imgcodecs.imread("/storage/emulated/0/Download/Camera_app/cam_image.jpg");
           // gray= imread("/storage/emulated/0/Download/Camera_app/cam_image.jpg", IMREAD_GRAYSCALE);
           //adaptiveThreshold(gray, result, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, 40);
           Imgproc.cvtColor(m,gray,Imgproc.COLOR_RGB2GRAY);
           Imgcodecs.imwrite("/storage/emulated/0/Download/Camera_app/cam_image1.jpg",gray);
           Toast.makeText(getActivity(), "Threshold done", Toast.LENGTH_LONG).show();
/*if(OpenCVLoader.initDebug()) {
    Log.i(TAG, "Opencv loaded successfully");
mRgba=new Mat(height,width,CvType.CV_8UC4);
    File f = new File("/storage/emulated/0/Download/Camera_app/cam_image.jpg");
    Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
    mimageView.setImageBitmap(bmp);*/
   // mimageView.setImageDrawable(Drawable.createFromPath(path));



//mimageView.setImageDrawable(Drawable.createFromPath(path));

       }
        else if(requestCode==MY_DATA_CHECK_CODE)
       {
           if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
               //the user has the necessary data - create the TTS
               { myTTS = new TextToSpeech(getActivity(),this);}
           } else {
               //no data - install it now
               Intent installTTSIntent = new Intent();
               installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
               startActivity(installTTSIntent);
           }
       }
    }
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(getActivity(), "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }



    //speak the user text
    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }



    //act on result of TTS data check


    //setup TTS

}
