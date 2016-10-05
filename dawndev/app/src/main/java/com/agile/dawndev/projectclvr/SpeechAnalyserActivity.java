package com.agile.dawndev.projectclvr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.Models.CLVRQuestion;
import com.agile.dawndev.projectclvr.Models.CLVRResults;
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
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This activity handles all the functionality related to taking a test.
 */
public class SpeechAnalyserActivity extends Activity {
    private static final String TAG = "SpeechToTextActivity";

    private ProgressBar mProgressBar;

    private RelativeLayout mCoverUp;

    private CountDownTimer countdowntimer;
    private TextView textviewtimer;

    // miliseconds
    private long timerLimit = 6000;
    private TextView mTitle;
    private TextView mInstruction;
    private TextView mContinueText;
    private ScrollView mScroll;


    private WavAudioRecorder mRecorder;
    private Button mButtonRecord = null;
    private FloatingActionButton mContinueButton = null;

    private DatabaseReference mDatabase;
    private String mCompanyName;
    private String mCompanyKey;
    private String mCompanyEmail;
    private String mTestKey;
    private String mUsername;
    private String mUserEmail;
    private int mQuestionNum = 1;
    private TreeMap<String, String> mInstructionAndAnswerMap = new TreeMap<String, String>();
    private int mInstructionCounter = 0;
    private String mFileName;
    private int numOfTasks;
    private int totalNumTasks;
    private AtomicInteger numCompleted = new AtomicInteger();
    private AtomicInteger totalCompleted = new AtomicInteger(1);

    private HashMap<Integer, String> mFileMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> mTranscriptionMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> mToneMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> mRecordingURLs = new HashMap<Integer, String>();
    private HashMap<String, Integer> mqTimes = new HashMap<String, Integer>();
    private String mOverallToneAnalysis;
    private String mPersonalityAnalysis;

    private ToneAnalyzer mToneAnalyzerService;
    private PersonalityInsights mPersonalityInsightsService;
    private String mAllTextAnswers = "";
    private HashMap<Integer, CLVRQuestion> mQuestionResults = new HashMap<Integer, CLVRQuestion>();
    private String[] mQuestionTitles;
    private boolean mErrorOccurred = false;

    private ConcurrentHashMap<Integer, Integer> mBadQuestions = new ConcurrentHashMap<Integer, Integer>();
    private boolean isBadQuestionRun = false;
    private ArrayList<Integer> mBadQuestionNumbers = new ArrayList<Integer>();
    private int mBadRunCounter = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the content view
        setContentView(R.layout.activity_speech_to_text);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Regular.otf");
        //Get the key for the test in the database from the sent intent.
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mCompanyKey = extras.getString("companyKey");
                mTestKey = extras.getString("testKey");
                mCompanyName = extras.getString("companyName");
                mCompanyEmail = extras.getString("companyEmail");
            }
        } else {
            mCompanyKey = (String) savedInstanceState.getSerializable("companyKey");
            mTestKey = (String) savedInstanceState.getSerializable("testKey");
            mCompanyName = (String) savedInstanceState.getSerializable("companyName");
            mCompanyEmail = (String) savedInstanceState.getSerializable("companyEmail");
        }


        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/question" + mQuestionNum + ".wav";
        Log.d(TAG, "File name to transcribe: " + mFileName);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mContinueButton = (FloatingActionButton) findViewById(R.id.continue_button);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueButtonOnClick(v);
            }
        });
        mContinueText = (TextView) findViewById(R.id.continue_text);
        textviewtimer = (TextView) findViewById(R.id.textViewtimer);
        mScroll = (ScrollView) findViewById(R.id.scroll);

        mTitle = (TextView) findViewById(R.id.title);
        mInstruction = (TextView) findViewById(R.id.instructions);
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mFileName);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mCoverUp = (RelativeLayout) findViewById(R.id.cover_layout);

        mProgressBar.setVisibility(View.INVISIBLE);

        mContinueText.setTypeface(custom_font);
        textviewtimer.setTypeface(custom_font);
        mTitle.setTypeface(custom_font);
        mInstruction.setTypeface(custom_font);


        populateMap();

        // get user's display name for recording storage purposes
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mUsername = mAuth.getCurrentUser().getDisplayName();
        mUserEmail = mAuth.getCurrentUser().getEmail();
        //updateText();
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
        mButtonRecord.setTypeface(custom_font);

        mToneAnalyzerService = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        mToneAnalyzerService.setUsernameAndPassword("8079d59a-3f9d-445f-ab62-05a278b36a79", "WNVh4LVVeEYh");

        mPersonalityInsightsService = new PersonalityInsights();
        mPersonalityInsightsService.setUsernameAndPassword("4db983b0-c24b-4d4a-85e0-686138ddb872", "TJBxIlpWtull");

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void recordAudio() {
        if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
            mRecorder.prepare();
            mRecorder.start();
            mButtonRecord.setText("Stop Recording");
            mButtonRecord.setAlpha(1.0f);

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

    //Perform speech recognition. Detect whether voice input was sufficient in length.
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
                SpeechResults transcript = null;
                //Attempt to perform speech recognition. If the voice input is less than 100 bytes cancel the operation.
                try {
                    transcript = service.recognize(recordedResponse, options).execute();
                } catch(BadRequestException e) {
                    cancel(false);
                }
                return transcript;
            }

            @Override
            protected void onPostExecute(SpeechResults result) {
                String finalTranscript = "";
                //If the voice input has no words cancel the operation. Signal that this question has to be redone.
                //Put the question number in the map that contains all questions that must be redone.
                if (result.getResults().size() == 0) {
                    mBadQuestions.put(questionNum, questionNum);
                    isBadQuestionRun = true;
                    whenDone();
                    return;
                }
                for (Transcript t : result.getResults()) {
                    String trans = t.getAlternatives().get(0).getTranscript();
                    finalTranscript += trans;
                }

                //Replace ASCII to apostrophe
                finalTranscript.replace("\u0027", "'");
                Log.d("TRANSCRIPT", finalTranscript);
                //Put the question number along with the answer into the map.
                mTranscriptionMap.put(questionNum, finalTranscript);
                Log.d(TAG, "TRANSCRIPT " + result);
                toneAnalysis(finalTranscript, questionNum, false);
                whenDone();
            }

            //Signal that this question has to be redone. Put the question number in the map that contains all questions that must be redone.
            @Override
            protected void onCancelled() {
                mBadQuestions.put(questionNum, questionNum);
                isBadQuestionRun = true;
                whenDone();
            }
        }.execute();

    }

    //Perform tone analysis.
    private void toneAnalysis(final String stringInput, final int questionNum, final boolean forCombinedText) {
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... input) {
                ToneAnalyzer service = (ToneAnalyzer) input[0];

                ToneAnalysis tone = service.getTone(stringInput, null).execute();
                return tone.getDocumentTone().toString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (!forCombinedText) {
                        mToneMap.put(questionNum, result);
                        doDoneDone();
                    } else {
                        mOverallToneAnalysis = result;
                        doDoneDone();
                    }
                }
            }
        }.execute(mToneAnalyzerService);
    }

    //Performs personality insights.
    private void personalityInsight(final String stringInput) {
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... input) {
                PersonalityInsights service = (PersonalityInsights) input[0];
                return service.getProfile(stringInput).execute().toString();
            }

            //When completed indicate that results have been received.
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    mPersonalityAnalysis = result;
                    doDoneDone();
                }
            }
        }.execute(mPersonalityInsightsService);
    }

    //Uploads the specified file to the database.
    public void uploadRecording(File recordedResponse, final int questionNum) {
        Log.d(TAG, " start uploading");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://projectclvr.appspot.com");
        StorageReference companyRef = storageRef.child(mCompanyName + "/" + mTestKey + "/" + mUsername);

        StorageReference recordingRef = companyRef.child("Question" + questionNum + ".wav");
        //store location of recording
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
                    mRecordingURLs.put(questionNum, downloadUrl.toString());

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

        mButtonRecord.setText("Done");
        mButtonRecord.setEnabled(false);
        // set the text color to grey
        mButtonRecord.setAlpha(.5f);
        mContinueButton.setVisibility(View.VISIBLE);
        mContinueText.setVisibility(View.VISIBLE);
    }

    //Updates the text on the page for a particular question.
    public void updateText() {
        //If the current run is a bad run, update the page from the array of bad questions.
        if(isBadQuestionRun) {
            String[] instructionSet = mInstructionAndAnswerMap.keySet().toArray(new String[mInstructionAndAnswerMap.size()]);
            String instructionKey = instructionSet[mBadQuestionNumbers.get(mBadRunCounter) - 1];
            mTitle.setText(instructionKey);
            mInstruction.setText(mInstructionAndAnswerMap.get(instructionKey));

            // change recording time for this question
            timerLimit = mqTimes.get(instructionKey) * 1000;

            mButtonRecord.setAlpha(1.0f);
            mContinueText.setVisibility(View.INVISIBLE);

        } else { //If the current run is not a bad run, update the page from the normal array of questions.
            String[] instructionSet = mInstructionAndAnswerMap.keySet().toArray(new String[mInstructionAndAnswerMap.size()]);
            String instructionKey = instructionSet[mInstructionCounter];
            mTitle.setText(instructionKey);
            mInstruction.setText(mInstructionAndAnswerMap.get(instructionKey));

            // change recording time for this question
            timerLimit = mqTimes.get(instructionKey) * 1000;
        }
    }

    //Called when the continue button is pressed.
    //Checks whether to continue to the next question or start computation (uploading, speech recognition, tone analysis and personality insights).
    public void continueButtonOnClick(View view) {
        // if there are no questions left
        if(isBadQuestionRun) {
            //If the last question on the bad run has been reached, reset the bad question flags and start computation.
            if(mBadRunCounter == mBadQuestionNumbers.size() - 1) {
                mFileMap.put(mBadQuestionNumbers.get(mBadRunCounter), mFileName);
                isBadQuestionRun = false;
                mBadQuestionNumbers = new ArrayList<Integer>();
                mBadQuestions = new ConcurrentHashMap<Integer, Integer>();
                mBadRunCounter = 0;
                showLoadingDisplay();
                doUploadingAndRecognition();
            } else { //If the last question on the bad run has not been reached, prepare the page for the next bad question.
                mContinueButton.setVisibility(View.INVISIBLE);

                mButtonRecord.setText("Start Recording");
                mButtonRecord.setEnabled(true);
                mButtonRecord.setTextColor(Color.WHITE);

                mFileMap.put(mBadQuestionNumbers.get(mBadRunCounter), mFileName);
                mBadRunCounter++;
                updateText();
                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/question" + mBadQuestionNumbers.get(mBadRunCounter) + ".wav";
                mRecorder.setOutputFile(mFileName);
            }
        } else {
            //Check if the last question for the normal run has been reached. If so start computation.
            if (mInstructionCounter == (mInstructionAndAnswerMap.size() - 1)) {
                mInstructionCounter++;
                mFileMap.put(mInstructionCounter, mFileName);

                showLoadingDisplay();
                doUploadingAndRecognition();
            } else { //If the last question on the normal run has not been reached, prepare the page for the next question.
                mContinueButton.setVisibility(View.INVISIBLE);
                mContinueText.setVisibility(View.INVISIBLE);

                // show next question for user and allow recording again
                mInstructionCounter++;
                mQuestionNum++;
                updateText();
                mButtonRecord.setText("Start Recording");
                mButtonRecord.setEnabled(true);
                mButtonRecord.setTextColor(Color.WHITE);
                mButtonRecord.setAlpha(1.0f);

                mFileMap.put(mInstructionCounter, mFileName);

                // prepare for recording next question
                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/question" + mQuestionNum + ".wav";
                mRecorder.setOutputFile(mFileName);
            }
        }
    }

    //Show the loading screen when the uploading, speech recognition, tone analysis and personality insights is being completed.
    private void showLoadingDisplay() {
        mCoverUp.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.GONE);
        mInstruction.setVisibility(View.GONE);
        mButtonRecord.setVisibility(View.GONE);
        mContinueButton.setVisibility(View.GONE);
        mContinueText.setVisibility(View.GONE);
        mScroll.setVisibility(View.GONE);
    }

    //Disable the loading screen.
    private void undoLoadingDisplay() {
        mCoverUp.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        mInstruction.setVisibility(View.VISIBLE);
        mButtonRecord.setVisibility(View.VISIBLE);
        mButtonRecord.setAlpha(1.0f);
        mScroll.setVisibility(View.VISIBLE);
    }

    //Carry out the uploading and speech recognition on all the files in the file hashmap.
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

    // checked that all the results have been retrieved.
    private void whenDone() {
        int num = numCompleted.incrementAndGet();
        if (num >= numOfTasks) {
            //Check if there are questions that must be redone.
            //If so show the message indicating that questions must be redone.
            //Start the redo run where users must redo insufficient questions.
            if(isBadQuestionRun) {
                undoLoadingDisplay();
                new android.support.v7.app.AlertDialog.Builder(SpeechAnalyserActivity.this)
                        .setTitle("Insufficient Dialogue at Question(s)")
                        .setMessage("Length of some questions are insufficient. Please redo.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                numCompleted = new AtomicInteger(0);
                badQuestionRun();
            } else {
                // when only tasks done, set button to enable
                mButtonRecord.setText("Start Recording");
                mButtonRecord.setEnabled(true);
                mButtonRecord.setTextColor(Color.WHITE);
                mContinueButton.setVisibility(View.VISIBLE);

                for(int questionNum : mFileMap.keySet()) {
                    File theFile = new File(mFileMap.get(questionNum));
                    boolean deleted = theFile.delete();
                    if (deleted) {
                        Log.d(TAG, "Deleted file");
                    }
                }
            }
        }
    }

    //Starts and organises the bad question run.
    private void badQuestionRun() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mButtonRecord.setText("Start Recording");
        mButtonRecord.setEnabled(true);
        mButtonRecord.setTextColor(Color.WHITE);
        mContinueButton.setVisibility(View.INVISIBLE);

        //Get all the bad questions from the concurrent hashmap and put them in an array.
        for(Integer question : mBadQuestions.keySet()) {
            mBadQuestionNumbers.add(question);
        }

        //Sort the array to order the questions.
        Collections.sort(mBadQuestionNumbers);

        //Prepare for recording for the first bad question.
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/question" + mBadQuestionNumbers.get(0) + ".wav";
        mRecorder.setOutputFile(mFileName);

        updateText();
    }

    //Checks that all results from the tone analysis and personality insights have been received.
    private void doDoneDone() {
        int num = totalCompleted.getAndIncrement();
        if(num == totalNumTasks - 2) {
            //Order the maps by key
            //Get and concatenate all transcribed answers
            //At the same time prepare a map of results per question for pdf generation
            for (int i = 0; i < mTranscriptionMap.size(); i++) {
                mAllTextAnswers += mTranscriptionMap.get(i+1) + "\n";
                CLVRQuestion questionStuff = new CLVRQuestion(mInstructionAndAnswerMap.get(mQuestionTitles[i]), mTranscriptionMap.get(i+1),
                        mRecordingURLs.get(i+1), mToneMap.get(i+1));
                mQuestionResults.put(i+1, questionStuff);
            }

            // execute tone analysis and personality analysis on combined text
            toneAnalysis(mAllTextAnswers, -1, true);
            String[] textArray = mAllTextAnswers.split(" ");
            //Check that the voice input has atleast 100 words. If not, show the insufficient dialogue message and redirect to the CompanyListActivity.
            //If there are 100 or more words progress with personality insights.
            if(textArray.length < 100) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SpeechAnalyserActivity.this);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SpeechAnalyserActivity.this, CompanyListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialogBuilder.setTitle("Insufficient dialogue").setMessage("Combined length of all your answers did not reach sufficient length for accurate analysis.");
                dialogBuilder.create().show();
                //signal that an error has occurred so that execution will not continue
                mErrorOccurred = true;
            } else {
                personalityInsight(mAllTextAnswers);
            }
        }
        if(num == totalNumTasks && !mErrorOccurred) {

            //Send all data for PDF generation in encapsulating object
            CLVRResults finalResults = CLVRResults.getInstance();
            finalResults.setmCompanyName(mCompanyName);
            finalResults.setmCompanyEmail(mCompanyEmail);
            finalResults.setmUsername(mUsername);
            finalResults.setmUserEmail(mUserEmail);
            finalResults.setmTestnumber(mTestKey);
            finalResults.setmOverallToneAnalysis(mOverallToneAnalysis);
            finalResults.setmOverallPersonalityInsights(mPersonalityAnalysis);
            finalResults.setClvrQuestionHashMap(mQuestionResults);

            Intent intent = new Intent(SpeechAnalyserActivity.this, GraphGenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Get question information from the database. But the questions in the question hashmap and record the number of tasks (voice recognition and uploading) that must be completed.
    public void populateMap() {
        mDatabase.child("companies").child(mCompanyKey).child("tests").child(mTestKey).child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    mInstructionAndAnswerMap.put(questionSnapshot.getKey(), questionSnapshot.child("Question").getValue().toString());
                    mqTimes.put(questionSnapshot.getKey(), Integer.parseInt(questionSnapshot.child("Time").getValue().toString()));
                }
                updateText();
                numOfTasks = mInstructionAndAnswerMap.size() * 2;
                totalNumTasks = mInstructionAndAnswerMap.size() + 2;
                mQuestionTitles = Arrays.copyOf(mInstructionAndAnswerMap.keySet().toArray(), mInstructionAndAnswerMap.size(), String[].class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


}
