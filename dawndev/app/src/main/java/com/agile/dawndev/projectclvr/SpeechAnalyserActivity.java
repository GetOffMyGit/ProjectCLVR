package com.agile.dawndev.projectclvr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.ToneAnalyser.ToneAnalyzerAsync;
import com.agile.dawndev.projectclvr.ToneAnalyser.ToneTabActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by CHRUSTINUAS on 22/09/2016.
 */
public class SpeechAnalyserActivity extends Activity {
    private static final String TAG = "SpeechToTextActivity";

    private TextView mText;
    private TextView mResponseText;
    private ProgressBar mProgressBar;

    private CountDownTimer countdowntimer;
    private TextView textviewtimer;

    // miliseconds
    private long timerLimit = 6000;
    private TextView mTitle;
    private TextView mInstruction;

    private WavAudioRecorder mRecorder;
    private Button mButtonRecord = null;
    private Button mContinueButton = null;

    private DatabaseReference mDatabase;
    private String mCompanyName;
    private String mCompanyKey;
    private String mTestKey;
    private String mUsername;
    private int mQuestionNum = 1;
    private TreeMap<String, String> mInstructionAndAnswerMap = new TreeMap<String, String>();
    private int mInstructionCounter = 0;


    private static final int PERMISSION_ALL = 1;
    private static final String[] PERMISSIONS = {android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String mFileName;
    private int numOfTasks;
    private AtomicInteger numCompleted = new AtomicInteger();
    private File audioFile;

    private HashMap<Integer, String> mFileMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> mTranscriptionMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> mToneMap = new HashMap<Integer, String>();

    private ToneAnalyzer mToneAnalyzerService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the content view
        setContentView(R.layout.activity_speech_to_text);
        //Get the key for the test in the database from the sent intent.
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mCompanyKey = extras.getString("companyKey");
                mTestKey = extras.getString("testKey");
                mCompanyName = extras.getString("companyName");
            }
        } else {
            mCompanyKey = (String) savedInstanceState.getSerializable("companyKey");
            mTestKey = (String) savedInstanceState.getSerializable("testKey");
        }


        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/question" + mQuestionNum + ".wav";
        audioFile = new File(mFileName);
        Log.d(TAG, "File name to transcribe: " + mFileName);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mContinueButton = (Button) findViewById(R.id.continue_button);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueButtonOnClick(v);
            }
        });

        mText = (TextView) findViewById(R.id.isThisRight);
        mResponseText = (TextView) findViewById(R.id.textResult);
        textviewtimer = (TextView) findViewById(R.id.textViewtimer);
        mTitle = (TextView) findViewById(R.id.title);
        mInstruction = (TextView) findViewById(R.id.instructions);
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mFileName);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mProgressBar.setVisibility(View.INVISIBLE);

        Log.d("cj", mTestKey);
        Log.d("cj", mCompanyKey);


        populateMap();

        // get user's display name for recording storage purposes
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mUsername = mAuth.getCurrentUser().getDisplayName();
        //updateText();

        // Check appropriate permissions
        if (!hasPermissions(this, PERMISSIONS)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[1]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[2])) {

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

        //Start and Stop Record Button
        mButtonRecord = (Button) findViewById(R.id.buttonRecord);
        mButtonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                recordAudio();
            }
        });

        mToneAnalyzerService = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        Log.d("Swamp monster", "new ad33f03f-e1b7-4963-a444-aef4001c5b7b");
        mToneAnalyzerService.setUsernameAndPassword("ad33f03f-e1b7-4963-a444-aef4001c5b7b", "btNKqDZhXzCY");
    }

    public void recordAudio() {
        if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
            mResponseText.setText("");
            mRecorder.prepare();
            mRecorder.start();
            mButtonRecord.setText("Stop Recording");

            //Start the timer
            countdowntimer = new CountDownTimerClass(timerLimit, 1000);
            countdowntimer.start();
            textviewtimer.setVisibility(View.VISIBLE);

        } else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
            mRecorder.release();
            mRecorder = WavAudioRecorder.getInstanse();
            mRecorder.setOutputFile(mFileName);
            mButtonRecord.setText("Error while recording");
        } else {
            finishRecording();
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

    private void speechRecognition(final File recordedResponse, final int questionNum) {
        Log.d(TAG, "START");
        new AsyncTask<Void, SpeechResults, SpeechResults>() {
            @Override
            protected SpeechResults doInBackground(Void... none) {
                SpeechToText service = new SpeechToText();
                service.setUsernameAndPassword(getString(R.string.STTdefaultUsername), getString(R.string.STTdefaultPassword));

                // options when recognizing speech, continuous - dont stop when pauses appear, timeout after no voice is recognize
                RecognizeOptions options = new RecognizeOptions.Builder()
                        .continuous(true)
                        .inactivityTimeout(600)
                        .contentType(HttpMediaType.AUDIO_WAV)
                        .model("en-US_BroadbandModel").build();

                // recognize audio file
                SpeechResults transcript = service.recognize(recordedResponse, options).execute();

                return transcript;
            }

            @Override
            protected void onPostExecute(SpeechResults result) {
                String finalTranscript = "";
                if (result.getResults().size() == 0) {
                    return;
                }
                for (Transcript t : result.getResults()) {
                    String trans = t.getAlternatives().get(0).getTranscript();
                    finalTranscript += trans;
                }
                mTranscriptionMap.put(questionNum, finalTranscript);
                mResponseText.setText(finalTranscript);
                Log.d(TAG, "TRANSCRIPT " + result);
                toneAnalysis(finalTranscript, questionNum);
                whenDone();
            }
        }.execute();

    }

    private void toneAnalysis(final String stringInput, final int questionNum) {
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... input) {
                Log.d("Swamp monster", " " + stringInput);
                ToneAnalyzer service = (ToneAnalyzer) input[0];
                //String text = (String) input[1];

                ToneAnalysis tone = service.getTone(stringInput, null).execute();
                Log.d("Swamp monster", " NAAAAASABEEENAAAACHHEE CCEEHAHAZHABBAA" + stringInput);
                return tone.getDocumentTone().toString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    mToneMap.put(questionNum, result);
                    Log.d("Swamp monster", " " + mToneMap.size());
                    Log.d("Swamp monster", result);
                }
            }
        }.execute(mToneAnalyzerService);
    }

    private void personalityInsight() {

    }

    public void uploadRecording(File recordedResponse, int questionNum) {
        Log.d(TAG, " start uploading");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://projectclvr.appspot.com");
        StorageReference companyRef = storageRef.child(mCompanyName + "/" + mTestKey + "/" + mUsername);

        StorageReference recordingRef = companyRef.child("Question" + questionNum + ".wav");

        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder().setContentType("audio/wav").build();

        try {
            InputStream stream = new FileInputStream(recordedResponse);

            UploadTask uploadTask = recordingRef.putStream(stream, metadata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d(TAG, " fail to upload");
                    whenDone();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.d(TAG, " Recording download " + downloadUrl);

                    whenDone();
                }
            });
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found; " + mFileName);
        }
    }

    public class CountDownTimerClass extends CountDownTimer {

        public CountDownTimerClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // converts the timer to a readable clock format
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis)
                    - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            textviewtimer.setText(hms);
        }

        @Override
        public void onFinish() {
            // when timer finishes - stop recording
            finishRecording();
        }
    }

    private void finishRecording() {
        countdowntimer.cancel();
        mRecorder.stop();
        mRecorder.reset();
        textviewtimer.setVisibility(View.INVISIBLE);
        //speechRecognition();
        //uploadRecording();

        mButtonRecord.setText("Done");
        mButtonRecord.setEnabled(false);
        // set the text color to grey
        mButtonRecord.setTextColor(Color.parseColor("#737373"));
        mContinueButton.setVisibility(View.VISIBLE);
    }

    public void updateText() {
        String[] instructionSet = mInstructionAndAnswerMap.keySet().toArray(new String[mInstructionAndAnswerMap.size()]);
        String instructionKey = instructionSet[mInstructionCounter];
        mTitle.setText(instructionKey);
        mInstruction.setText(mInstructionAndAnswerMap.get(instructionKey));
    }


    public void continueButtonOnClick(View view) {
        // if there are no questions left
        if (mInstructionCounter == (mInstructionAndAnswerMap.size() - 1)) {
            mInstructionCounter++;
            mFileMap.put(mInstructionCounter, mFileName);
            doUploadingAndRecognition();
        } else {
            mContinueButton.setVisibility(View.INVISIBLE);

            // show next question for user and allow recording again
            mInstructionCounter++;
            mQuestionNum++;
            updateText();
            mButtonRecord.setText("Start Recording");
            mButtonRecord.setEnabled(true);
            mButtonRecord.setTextColor(Color.WHITE);

            mFileMap.put(mInstructionCounter, mFileName);

            // prepare for recording next question
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/question" + mQuestionNum + ".wav";
            audioFile = new File(mFileName);
            mRecorder.setOutputFile(mFileName);
        }
    }

    private void doUploadingAndRecognition() {
        mProgressBar.setVisibility(View.VISIBLE);
        for(int questionNum : mFileMap.keySet()) {
            File theFile = new File(mFileMap.get(questionNum));
            speechRecognition(theFile, questionNum);
            uploadRecording(theFile, questionNum);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // checked that all the results have been retrieved, and if so, moves to the main activity
    private void whenDone() {
        int num = numCompleted.incrementAndGet();
        if (num == numOfTasks) {
            // when only tasks done, set button to enable
            mButtonRecord.setText("Start Recording");
            mButtonRecord.setEnabled(true);
            mButtonRecord.setTextColor(Color.WHITE);
            mContinueButton.setVisibility(View.VISIBLE);
            mText.setVisibility(View.VISIBLE);

            for(int questionNum : mFileMap.keySet()) {
                File theFile = new File(mFileMap.get(questionNum));
                boolean deleted = theFile.delete();
                if (deleted) {
                    Log.d(TAG, "Deleted file");
                }
            }

            //Order the map

            //Send maps

            mProgressBar.setVisibility(View.INVISIBLE);
            //Intent intent = new Intent(SpeechAnalyserActivity.this, ToneTabActivity.class);
            //startActivity(intent);
        }
    }

    public void populateMap() {
        mDatabase.child("companies").child(mCompanyKey).child("tests").child(mTestKey).child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    mInstructionAndAnswerMap.put(questionSnapshot.getKey(), questionSnapshot.getValue().toString());
                }
                updateText();
                numOfTasks = mInstructionAndAnswerMap.size() * 2;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }


}
