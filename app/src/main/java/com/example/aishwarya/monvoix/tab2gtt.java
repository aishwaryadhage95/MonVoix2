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
import org.opencv.imgcodecs.Imgcodecs;

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
    Mat imageMat;
    Mat comparer;
    Bitmap bamp;
Bitmap bmp;
    Bitmap cmp;
    Button sendBtn;
    static int flag=0;
    Mat kernel;
Mat thresh;
    Double diff;
private static String TAG="MainActivity";
   private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    imageMat=new Mat();
                    kernel=new Mat();
                    thresh = new Mat();
                    comparer=new Mat();


                    /* Mat img1 = Imgcodecs.imread(getResources().getDrawable(R.drawable.hello).toString());

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

                    */
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
        final ImageView imageview = (ImageView) rootView.findViewById(R.id.imageview);
        txtMessage=(EditText) rootView.findViewById(R.id.editText4);
        txtphoneNo=(EditText) rootView.findViewById(R.id.editText5);
        Button process1 = (Button) rootView.findViewById(R.id.process);
        Button speakButton = (Button) rootView.findViewById(R.id.button2);
        final EditText enteredText = (EditText) rootView.findViewById(R.id.editText4);
        sendBtn=(Button) rootView.findViewById(R.id.button);
        //listen for clicks
        //send button
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
            }


        });
        //process button
        process1.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            if (!OpenCVLoader.initDebug()) {
                                                Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                                                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getActivity(), mLoaderCallback);
                                            } else {
                                                Log.d("OpenCV", "OpenCV library found inside package. Using it!");
                                                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
                                                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.camhand2);
                                                cmp=BitmapFactory.decodeResource(getResources(), R.drawable.camhand2);
                                               // bmp = BitmapFactory.decodeFile("/storage/emulated/0/Download/Camera_app/cam_image.jpg");
                                               bamp = ImageProcessing(bmp);
                                                diff=difference(bamp,cmp);
                                                Log.d("OpenCV", "done wid processing");
                                              //  imageview.setImageBitmap(bamp);
                                                Log.d("OpenCV", String.valueOf(diff));
                                            }
                                        }

              // File root = Environment.getExternalStorageDirectory();

//detectregion();

        });

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        //audio button
        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String words = enteredText.getText().toString();
                speakWords(words);
                Intent checkTTSIntent = new Intent();
                checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);

                startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
            }


        });

//camera button
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

               File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
             //   txtMessage.setText( txtMessage.getText() + "Hello " + btn.getText() );
                startActivityForResult(camera_intent, CAM_REQUEST);


            }


        });

        return rootView;
    }
///compare images
    private Double difference(Bitmap bmp, Bitmap cmp) {
        int intColor1 = 0;
        int sum=0;
        int intColor2 = 0;
        for (int x = 0; x < bmp.getWidth(); x++) {
            for (int y = 0; y < bmp.getHeight(); y++) {
                intColor1 = bmp.getPixel(x, y);
                intColor2 = cmp.getPixel(x, y);
                if(intColor1>intColor2)
                    sum=sum+(intColor1-intColor2);
                else
                    sum=sum+(intColor2-intColor1);

            }
        }
        int ncomponents=bmp.getHeight()*bmp.getWidth()*3;
        //now calculate percentage difference
        double diff = (sum/255.0*100)/ncomponents;
        return diff;
    }
//save image
    private File getFile() {
        File folder = new File("/storage/emulated/0/Download/Camera_app");

        if (!folder.exists()) {
            folder.mkdir();
        }
        File image_file = new File(folder, "cam_image.jpg");
        return image_file;
    }
//after saving image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==CAM_REQUEST) {
           String path = "/storage/emulated/0/Download/Camera_app/cam_image.jpg";

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
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(getActivity(), "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
//speak button
    private void speakWords(String speech) {
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }
//process captured image
    public Bitmap ImageProcessing(Bitmap bitmap){
        Log.d("OpenCV", "inside image processing method");
        Utils.bitmapToMat(bitmap, imageMat);
        Log.d("OpenCV", "gray");
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2HSV);
        Log.d("OpenCV", "skin detect");
       Imgproc.GaussianBlur(imageMat, imageMat, new Size(3,3), 0);
//        Imgproc.medianBlur(imageMat,imageMat,new Size(3,3),0);
        Core.inRange(imageMat, new Scalar(30, 30, 30), new Scalar(255, 255, 255), thresh);
        int erosion_size = 5;
        int dilation_size = 5;
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*erosion_size + 1, 2*erosion_size+1));
      //  Imgproc.erode(thresh, thresh, kernel);
        Log.d("OpenCV", "canny edge");
        Imgproc.Canny(thresh, thresh, 80, 100);
        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*dilation_size + 1, 2*dilation_size+1));
        Imgproc.dilate(thresh, thresh, element1);
        Log.d("OpenCV", "dilate");
        Utils.matToBitmap(thresh,bitmap);
        return bitmap;
    }


}
