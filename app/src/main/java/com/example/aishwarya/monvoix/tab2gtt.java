package com.example.aishwarya.monvoix;

import android.app.Activity;
import android.app.PendingIntent;
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
import android.telephony.SmsManager;
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

import org.opencv.android.Utils;

import java.io.File;
import java.util.Locale;

import static android.R.attr.bitmap;
import static android.R.attr.height;
import static android.R.attr.path;
import static android.R.attr.type;
import static android.R.attr.value;
import static android.R.attr.width;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.BIND_ABOVE_CLIENT;
import static com.example.aishwarya.monvoix.R.id.button;
import static com.example.aishwarya.monvoix.R.id.image;
import static com.example.aishwarya.monvoix.R.id.imageView;
//import static com.example.aishwarya.monvoix.R.id.image_view;
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

import org.opencv.imgcodecs.Imgcodecs;

import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs; // imread, imwrite, etc

public class tab2gtt extends Fragment implements TextToSpeech.OnInitListener {
   // private File imageFile;
    Button btn;
    EditText txtMessage;
    EditText txtphoneNo;
    ImageView mimageView;
    static final int CAM_REQUEST = 1;
    JavaCameraView javaCameraView;
    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    Mat gray;
    Mat result;
    Mat skindetected;

    Button sendBtn;
    static int flag=0;

private static String TAG="MainActivity";
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                   // Mat img1 = new Mat();
                    Mat img1 = Imgcodecs.imread(getResources().getDrawable(R.drawable.hello).toString());

                    //Bitmap bmp32 = bmpGallery.copy(Bitmap.Config.ARGB_8888, true);
                    //Utils.bitmapToMat(bmp32, );
                    //Bitmap bmp;
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hello);
                    Mat tmp = new Mat (bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC1);
                    Utils.bitmapToMat(bmp, tmp);

                    // Mat img2 = Imgcodecs.imread("mnt/sdcard/IMG-20121228-1.jpg");
                    Scalar blah= Core.sumElems(tmp);
                    Scalar blah1=Core.sumElems(img1);
                    String b=blah.toString();
                    String b1=blah1.toString();
                    //System.out.println(b+" "+b1);
                    double comp=b.compareTo(b1);
                    txtMessage.setText(""+comp);

                } break;
                default:
                {        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, getActivity(), mLoaderCallback);

                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2gtt, container, false);
        btn = (Button) rootView.findViewById(R.id.button3);
   //     mimageView = (ImageView) rootView.findViewById(R.id.image_view);
        txtMessage=(EditText) rootView.findViewById(R.id.editText4);
        txtphoneNo=(EditText) rootView.findViewById(R.id.editText5);
        Button process1 = (Button) rootView.findViewById(R.id.process);
        Button speakButton = (Button) rootView.findViewById(R.id.button2);
        final EditText enteredText = (EditText) rootView.findViewById(R.id.editText4);
        sendBtn=(Button) rootView.findViewById(R.id.button);
        //listen for clicks
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String no=txtphoneNo.getText().toString();
                String msg=txtMessage.getText().toString();

                //Getting intent and PendingIntent instance
                Intent intent=new Intent(getActivity(),MainActivity.class);
                PendingIntent pi= PendingIntent.getActivity(getActivity(), 0, intent,0);

                //Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms= SmsManager.getDefault();
                sms.sendTextMessage(no, null, msg, pi,null);
                Log.i(TAG,no);
                Log.i(TAG,msg);
                Toast.makeText(getActivity(), "Message Sent successfully!",
                        Toast.LENGTH_LONG).show();

                //  Intent intent = new Intent(getActivity(), cameraview.class);
                //    startActivity(intent);

            }


        });
     //   process1.setOnClickListener(new View.OnClickListener() {

         /*   @Override
            public void onClick(View v) {

                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
/*
               Mat img1 = Imgcodecs.imread("/storage/emulated/0/Download/Camera_app/cam_image.jpg");
                //Bitmap bmp32 = bmpGallery.copy(Bitmap.Config.ARGB_8888, true);
                //Utils.bitmapToMat(bmp32, );
                //Bitmap bmp;
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hello);
                Mat tmp = new Mat (bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC1);
                Utils.bitmapToMat(bmp, tmp);
*/

        //    }


     //   });


        //listen for clicks
  /*      speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String words = enteredText.getText().toString();
                speakWords(words);
            }});
*/
        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String words = enteredText.getText().toString();
                speakWords(words);
                Intent checkTTSIntent = new Intent();
                checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);

                startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
                //  Intent intent = new Intent(getActivity(), cameraview.class);
                //    startActivity(intent);

            }


        });


        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                  //Intent intent = new Intent(getActivity(), cameraview.class);
                 //   startActivity(intent);
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               // Intent processing = new Intent(getActivity(), processing.class);
              // startActivity(processing);
               File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
             //   txtMessage.setText( txtMessage.getText() + "Hello " + btn.getText() );
                startActivityForResult(camera_intent, CAM_REQUEST);

              //  File file1 = getFile1();
                //camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file1));
                //startActivityForResult(camera_intent, 0);

            }


        });

        return rootView;
    }

    private File getFile() {
      //  File folder = new File("/storage/emulated/0/Download/Camera_app");
        File folder = new File("/storage/emulated/0/Download/Camera_app");

        if (!folder.exists()) {
            folder.mkdir();
        }
        File image_file = new File(folder, "cam_image.jpg");

   //     Mat imgGray=new Mat(height,width,CvType.CV_8UC1);



        return image_file;
    }

 /*   private File getFile1() {
        File folder = new File("/storage/emulated/0/Download/Camera_app");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File image_file = new File(folder, "cam_image1.jpg");

        //     Mat imgGray=new Mat(height,width,CvType.CV_8UC1);



        return image_file;
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==CAM_REQUEST) {
           String path = "/storage/emulated/0/Download/Camera_app/cam_image.jpg";
      /*     Mat image;

           //=new Mat(new Size(500,500),CvType.CV_8U);
           image=Imgcodecs.imread("/storage/emulated/0/Download/Camera_app/cam_image.jpg");
           // gray= imread(image, IMREAD_GRAYSCALE);
           adaptiveThreshold(image, result, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, 40);
           Imgproc.cvtColor(result, skindetected , Imgproc.COLOR_RGB2HSV_FULL);

           //  Imgproc.cvtColor(image, result, Imgproc.COLOR_RGB2RGBA);
           Bitmap bm=Bitmap.createBitmap(image.cols(),image.rows(),Bitmap.Config.ARGB_8888);
           Utils.matToBitmap(result,bm);
           mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
*/
         if(flag==0) {
             txtMessage.setText(txtMessage.getText() + "Hello " + btn.getText());
             flag++;
         }
else if(flag==1){
             txtMessage.setText(txtMessage.getText() + " Good " + btn.getText());
             flag++;
         }
         else if(flag==2){
             txtMessage.setText(txtMessage.getText() + " Morning " + btn.getText());
             flag++;
         }



           // Mat m= Imgcodecs.imread("/storage/emulated/0/Download/Camera_app/cam_image.jpg");
           // gray= imread("/storage/emulated/0/Download/Camera_app/cam_image.jpg", IMREAD_GRAYSCALE);
           //adaptiveThreshold(gray, result, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, 40);
         //  Imgproc.cvtColor(m,gray,Imgproc.COLOR_RGB2GRAY);
        //   Imgcodecs.imwrite("/storage/emulated/0/Download/Camera_app/cam_image1.jpg",gray);
         //  Toast.makeText(getActivity(), "Threshold done", Toast.LENGTH_LONG).show();
/*if(OpenCVLoader.initDebug()) {
    Log.i(TAG, "Opencv loaded successfully");
mRgba=new Mat(height,width,CvType.CV_8UC4);
    File f = new File("/storage/emulated/0/Download/Camera_app/cam_image.jpg");
    Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
    mimageView.setImageBitmap(bmp);
   // mimageView.setImageDrawable(Drawable.createFromPath(path));


*/
//mimageView.setImageDrawable(Drawable.createFromPath(path));

       }
        else
                if(requestCode==MY_DATA_CHECK_CODE)
       {
           if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
               //the user has the necessary data - create the TTS
               { myTTS = new TextToSpeech(getActivity(),this);}
           }
           else
           {
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
