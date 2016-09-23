package com.agile.dawndev.projectclvr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.ToneAnalyser.AnalyserTabActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Christina on 22/09/2016.
 */
public class SpeechAnalyserActivity extends Activity  {
    private static final String TAG = "SpeechToTextActivity";
    private static String message;
    private String mCompanyName = "CrimsonJelly";
    private String mTestNum = "test_1";
    private String mUser = "Zelda";
    private String mQuestionNum = "Q1";
    // session recognition results
    private static String mRecognitionResults = "";
    private enum ConnectionState {
        IDLE, CONNECTING, CONNECTED
    }

    private ConnectionState mState = ConnectionState.IDLE;
    public JSONObject jsonModels = null;
    private Handler mHandler = null;
    private Button mContinueButton;
    private Button mRecordButton;
    private TextView mText;
    private CountDownTimer countdowntimer;
    private TextView textviewtimer;
    private long timerLimit = 120000;
    private MediaRecorder myAudioRecorder = new MediaRecorder();
    private Button buttonRecord = null;

    private static final int PERMISSION_ALL = 1;
    private static final String[] PERMISSIONS = {android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String mFileName;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1.wav";
        Log.d(TAG, "File name to transcribe: " + mFileName);
        //set the content view
        setContentView(R.layout.activity_speech_to_text);

        //Set the views
        mHandler = new Handler();
        mContinueButton = (Button) findViewById(R.id.continue_button);
        mRecordButton = (Button) findViewById(R.id.buttonRecord);
        mText = (TextView) findViewById(R.id.isThisRight);
        textviewtimer = (TextView)findViewById(R.id.textViewtimer);

        // Strictmode needed to run the http/wss request for devices > Gingerbread
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Check appropriate permissions
        if(!hasPermissions(this, PERMISSIONS)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,PERMISSIONS[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,PERMISSIONS[1]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,PERMISSIONS[2])) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }

        //Check for connection with IBM Watson API
        if (!isNetworkAvailable()) {
            Log.d(TAG, "Please, check internet connection.");
            return;
        }

        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        Log.d(TAG, "please, press the button to start speaking");

        //Start and Stop Record Button
       buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {

                //speechRecognition();

                recordAudio();
                //uploadRecording();

                }
        });
    }
    public void recordAudio(){

        if (mState == ConnectionState.IDLE) {
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
//            myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            myAudioRecorder.setOutputFile(mFileName);
                        try {
                            myAudioRecorder.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        myAudioRecorder.start();
                        Log.d("RECORDING", "start");

                        //Start the timer
                        countdowntimer = new CountDownTimerClass(timerLimit, 1000);
                        countdowntimer.start();


                        mContinueButton.setVisibility(View.GONE);
                        mText.setVisibility(View.GONE);
                        mState = ConnectionState.CONNECTED;
                        Log.d(TAG, "onClickRecord: IDLE -> CONNECTING");



                        //Display results
                        mRecognitionResults = "";


                        buttonRecord.setText("Stop Recording");
                    } else if (mState == ConnectionState.CONNECTED) {
                        mContinueButton.setVisibility(View.VISIBLE);
                        mText.setVisibility(View.VISIBLE);
//
                        mState = ConnectionState.IDLE;
                        Log.d(TAG, "onClickRecord: CONNECTED -> IDLE");
                        buttonRecord.setText("Start Recording");

                        countdowntimer.cancel();

                        //speechRecognition();

                        myAudioRecorder.stop();
                        myAudioRecorder.release();
                        myAudioRecorder  = null;
                        Log.d("RECORDING", "stop");

                        speechRecognition();
                    }


    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void speechRecognition(){

        Log.d(TAG, "START");
        new AsyncTask<Void, SpeechResults, SpeechResults>() {
            @Override
            protected SpeechResults doInBackground(Void... none) {
                SpeechToText service = new SpeechToText();
                service.setUsernameAndPassword(getString(R.string.STTdefaultUsername), getString(R.string.STTdefaultPassword));
                RecognizeOptions options = new RecognizeOptions.Builder()
                        .continuous(true)
                        .inactivityTimeout(600)
                        //.wordAlternativesThreshold(0.001)
                        //.maxAlternatives(3)
                        .contentType(HttpMediaType.AUDIO_WAV)
                        .model("en-US_BroadbandModel").build();
                File audio = new File(mFileName);

                SpeechResults transcript = service.recognize(audio, options).execute();

                return transcript;
              }

            @Override
            protected void onPostExecute(SpeechResults result) {
                Log.d(TAG, "TRANSCRIPT " + result);
                Log.d(TAG, "DONE");
            }
            }.execute();


    }

    public void uploadRecording() {
        Log.d(TAG, " start uploading");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://projectclvr.appspot.com");
        StorageReference companyRef = storageRef.child(mCompanyName + "/" + mTestNum + "/" + mUser);

        StorageReference recordingRef = companyRef.child("clvr" + ".3gp");

        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder().setContentType("video/3gpp").build();

        try {
            InputStream stream = new FileInputStream(new File(mFileName));

            UploadTask uploadTask = recordingRef.putStream(stream, metadata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    Log.d(TAG, " fail to upload");
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.d(TAG, " Recording download " + downloadUrl);

                }
            });
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found; " + mFileName);
        }
    }


    public void onMessage(String message) {

        Log.d(TAG, "onMessage, message: " + message);
        try {
            JSONObject jObj = new JSONObject(message);
            // state message
            if (jObj.has("state")) {
                Log.d(TAG, "Status message: " + jObj.getString("state"));
            }
            // results message
            else if (jObj.has("results")) {
                //if has result
                Log.d(TAG, "Results message: ");
                JSONArray jArr = jObj.getJSONArray("results");
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject obj = jArr.getJSONObject(i);
                    JSONArray jArr1 = obj.getJSONArray("alternatives");
                    String str = jArr1.getJSONObject(0).getString("transcript");

                    String strFormatted = Character.toUpperCase(str.charAt(0)) + str.substring(1);
                    if (obj.getString("final").equals("true")) {
                        mRecognitionResults += strFormatted.substring(0, strFormatted.length() - 1);

                        Log.d(TAG, mRecognitionResults);
                    } else {
                        Log.d(TAG, mRecognitionResults + strFormatted);
                    }
                    break;
                }
            } else {
                Log.d(TAG, "unexpected data coming from stt server: \n" + message);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }
    }

    public class CountDownTimerClass extends CountDownTimer {

        public CountDownTimerClass(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);

        }
        @Override
        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished; String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            textviewtimer.setText(hms);
        }

        @Override
        public void onFinish() {
            mContinueButton.setVisibility(View.VISIBLE);
            mText.setVisibility(View.VISIBLE);

            mState = ConnectionState.IDLE;
            Log.d(TAG, "onClickRecord: CONNECTED -> IDLE");
            //SpeechToText.sharedInstance().stopRecognition();

            //countdowntimer.cancel();
            //displayResult(mRecognitionResults);
            //message = mRecognitionResults;
            textviewtimer.setText(" Count Down Finish ");

        }
    }


    public void toneResults(View view){
        Intent intent = new Intent(SpeechAnalyserActivity.this, AnalyserTabActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
