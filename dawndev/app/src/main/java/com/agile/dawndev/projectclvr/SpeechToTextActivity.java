/**
 Deals with Speech to Text Functionality
 **/

package com.agile.dawndev.projectclvr;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.ToneAnalyser.AnalyserTabActivity;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.dto.SpeechConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/*
    Activity that converts a recording to text and displays it. Uses the IBM Watson SDK.
 */
public class SpeechToTextActivity extends Activity {

    private static final String TAG = "SpeechToTextActivity";
    private static String message;

    public static class FragmentTabSTT extends Fragment implements ISpeechDelegate {

        // session recognition results
        private static String mRecognitionResults = "";
        private ItemModel mDefaultLanguageItem;
        private enum ConnectionState {
            IDLE, CONNECTING, CONNECTED
        }

        ConnectionState mState = ConnectionState.IDLE;
        public View mView = null;
        public Context mContext = null;
        public JSONObject jsonModels = null;
        private Handler mHandler = null;
        private Button mButton;
        private TextView mText;
        private CountDownTimer countdowntimer;
        private TextView textviewtimer;
        private long timerLimit = 120000;
        private MediaRecorder myAudioRecorder = new MediaRecorder();
        private static String outputFile = null;


        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            //Set the views
            mView = inflater.inflate(R.layout.activity_speech_to_text, container, false);
            mContext = getActivity().getApplicationContext();
            mHandler = new Handler();
            mButton = (Button) mView.findViewById(R.id.continue_button);
            mText = (TextView) mView.findViewById(R.id.isThisRight);
            textviewtimer = (TextView)mView.findViewById(R.id.textViewtimer);

            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
            outputFile += "/clvr.3gp";

            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
           // myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
           // myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4);
            myAudioRecorder.setOutputFile(outputFile);

            //Set the empty text
            setText();

            //Check for connection with IBM Watson API
            if (initSTT() == false) {
                displayResult("Error: no authentication credentials/token available, please enter your authentication information");
                return mView;
            }


            if (jsonModels == null) {
                //Retrieve the json response
                jsonModels = new STTCommands().doInBackground();
                if (jsonModels == null) {

                    //Display Results
                    displayResult("Please, check internet connection.");
                    return mView;
                }
            }


            addItemsOnSpinnerModels();

            displayStatus("please, press the button to start speaking");

            //Start and Stop Record Button
            Button buttonRecord = (Button) mView.findViewById(R.id.buttonRecord);
            buttonRecord.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (mState == ConnectionState.IDLE) {

                        try {
                            myAudioRecorder.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        myAudioRecorder.start();

                        countdowntimer = new CountDownTimerClass(timerLimit, 1000);

                        countdowntimer.start();


                        mButton.setVisibility(View.GONE);
                        mText.setVisibility(View.GONE);
                        mState = ConnectionState.CONNECTING;
                        Log.d(TAG, "onClickRecord: IDLE -> CONNECTING");

                        //Spinner to select language
//                        Spinner spinner = (Spinner) mView.findViewById(R.id.spinnerModels);
//                        spinner.setEnabled(false);

                        //Display results
                        mRecognitionResults = "";
                        displayResult(mRecognitionResults);
                        ItemModel item = (ItemModel) mDefaultLanguageItem;
                        SpeechToText.sharedInstance().setModel(item.getModelName());
                        displayStatus("connecting to the STT service...");
                        // start recognition
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... none) {
                                SpeechToText.sharedInstance().recognize();
                                return null;
                            }
                        }.execute();
                        setButtonLabel(R.id.buttonRecord, "Connecting...");
                        setButtonState(true);
                    } else if (mState == ConnectionState.CONNECTED) {

                        myAudioRecorder.stop();
                        myAudioRecorder.release();
                        myAudioRecorder  = null;

                        mButton.setVisibility(View.VISIBLE);
                        mText.setVisibility(View.VISIBLE);

                        //Initiate Spinner
                        mState = ConnectionState.IDLE;
                        Log.d(TAG, "onClickRecord: CONNECTED -> IDLE");
//                        Spinner spinner = (Spinner) mView.findViewById(R.id.spinnerModels);
//                        spinner.setEnabled(true);
                        SpeechToText.sharedInstance().stopRecognition();
                        setButtonState(false);

                        countdowntimer.cancel();
                        displayResult(mRecognitionResults);
                        message = mRecognitionResults;
                    }
                }
            });



            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.RECORD_AUDIO)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.RECORD_AUDIO},1
                    );

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }

            return mView;
        }

        private String getModelSelected() {

            //Get the language selected

//            Spinner spinner = (Spinner) mView.findViewById(R.id.spinnerModels);
//            ItemModel item = (ItemModel) spinner.getSelectedItem();
            return mDefaultLanguageItem.getModelName();
        }

        public URI getHost(String url) {
            try {
                return new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }

        private boolean initSTT() {
            // initialize the connection to the Watson STT service
            String username = getString(R.string.STTdefaultUsername);
            String password = getString(R.string.STTdefaultPassword);
            String serviceURL = "wss://stream.watsonplatform.net/speech-to-text/api";
            SpeechConfiguration sConfig = new SpeechConfiguration(SpeechConfiguration.AUDIO_FORMAT_OGGOPUS);
            SpeechToText.sharedInstance().initWithContext(this.getHost(serviceURL), getActivity().getApplicationContext(), sConfig);
            // Basic Authentication
            SpeechToText.sharedInstance().setCredentials(username, password);
            SpeechToText.sharedInstance().setModel(getString(R.string.modelDefault));
            SpeechToText.sharedInstance().setDelegate(this);
            return true;
        }


        protected void setText() {
            // title
            TextView viewTitle = (TextView) mView.findViewById(R.id.title);
            viewTitle.setText(R.string.sttTitle);

            // instructions
            TextView viewInstructions = (TextView) mView.findViewById(R.id.instructions);
            viewInstructions.setText(R.string.sttInstructions);
        }



        public class ItemModel {

            private JSONObject mObject = null;

            public ItemModel(JSONObject object) {
                mObject = object;
            }

            //get the description
            public String toString() {
                try {
                    return mObject.getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            //get the model name
            public String getModelName() {
                try {
                    return mObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        protected void addItemsOnSpinnerModels() {

            //Spinner spinner = (Spinner) mView.findViewById(R.id.spinnerModels);
            int iIndexDefault = 0;

            JSONObject obj = jsonModels;
            ItemModel[] items = null;
            try {
                JSONArray models = obj.getJSONArray("models");

                // count the number of Broadband models (narrowband models will be ignored since they are for telephony data)
                Vector<Integer> v = new Vector<>();
                for (int i = 0; i < models.length(); ++i) {
                    if (models.getJSONObject(i).getString("name").indexOf("Broadband") != -1) {
                        v.add(i);
                    }
                }
                items = new ItemModel[v.size()];
                int iItems = 0;
                for (int i = 0; i < v.size(); ++i) {
                    items[iItems] = new ItemModel(models.getJSONObject(v.elementAt(i)));
                    if (models.getJSONObject(v.elementAt(i)).getString("name").equals(getString(R.string.modelDefault))) {
                        iIndexDefault = iItems;
                    }
                    ++iItems;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mDefaultLanguageItem = items[iIndexDefault];

//            if (items != null) {
//                ArrayAdapter<ItemModel> spinnerArrayAdapter = new ArrayAdapter<ItemModel>(getActivity(), android.R.layout.simple_spinner_item, items);
//                spinner.setAdapter(spinnerArrayAdapter);
//                spinner.setSelection(iIndexDefault);
//            }
        }
        /**
         * Change the display's result
         */
        public void displayResult(final String result) {
            final Runnable runnableUi = new Runnable() {
                @Override
                public void run() {
                    TextView textResult = (TextView) mView.findViewById(R.id.textResult);
                    textResult.setText(result);
                }
            };

            new Thread() {
                public void run() {
                    mHandler.post(runnableUi);
                }
            }.start();
        }

        public void displayStatus(final String status) {
            /*final Runnable runnableUi = new Runnable(){
                @Override
                public void run() {
                    TextView textResult = (TextView)mView.findViewById(R.id.sttStatus);
                    textResult.setText(status);
                }
            };
            new Thread(){
                public void run(){
                    mHandler.post(runnableUi);
                }
            }.start();*/
        }

        /**
         * Change the button's label
         */
        public void setButtonLabel(final int buttonId, final String label) {
            final Runnable runnableUi = new Runnable() {
                @Override
                public void run() {
                    Button button = (Button) mView.findViewById(buttonId);
                    button.setText(label);
                }
            };
            new Thread() {
                public void run() {
                    mHandler.post(runnableUi);
                }
            }.start();
        }

        /**
         * Change the button's drawable
         */
        public void setButtonState(final boolean bRecording) {

            final Runnable runnableUi = new Runnable() {
                @Override
                public void run() {
                    int iDrawable = bRecording ? R.drawable.button_record_stop : R.drawable.button_record_start;
                    Button btnRecord = (Button) mView.findViewById(R.id.buttonRecord);
                    btnRecord.setBackground(getResources().getDrawable(iDrawable));
                }
            };
            new Thread() {
                public void run() {
                    mHandler.post(runnableUi);
                }
            }.start();
        }

        // delegages ----------------------------------------------

        public void onOpen() {
            Log.d(TAG, "onOpen");
            displayStatus("successfully connected to the STT service");
            setButtonLabel(R.id.buttonRecord, "Stop recording");
            mState = ConnectionState.CONNECTED;
        }

        public void onError(String error) {

            Log.e(TAG, error);
            displayResult(error);
            mState = ConnectionState.IDLE;
        }

        public void onClose(int code, String reason, boolean remote) {
            Log.d(TAG, "onClose, code: " + code + " reason: " + reason);
            displayStatus("connection closed");
            setButtonLabel(R.id.buttonRecord, "Record");
            mState = ConnectionState.IDLE;
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
                        // remove whitespaces if the language requires it
                        String model = this.getModelSelected();
                        if (model.startsWith("ja-JP") || model.startsWith("zh-CN")) {
                            str = str.replaceAll("\\s+", "");
                        }
                        String strFormatted = Character.toUpperCase(str.charAt(0)) + str.substring(1);
                        if (obj.getString("final").equals("true")) {
                            String stopMarker = (model.startsWith("ja-JP") || model.startsWith("zh-CN")) ? "。" : ". ";
                            mRecognitionResults += strFormatted.substring(0, strFormatted.length() - 1) + stopMarker;

                            displayResult(mRecognitionResults);
                        } else {
                            displayResult(mRecognitionResults + strFormatted);
                        }
                        break;
                    }
                } else {
                    displayResult("unexpected data coming from stt server: \n" + message);
                }

            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON");
                e.printStackTrace();
            }
        }

        public void onAmplitude(double amplitude, double volume) {
            //Logger.e(TAG, "amplitude=" + amplitude + ", volume=" + volume);
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
                mButton.setVisibility(View.VISIBLE);
                mText.setVisibility(View.VISIBLE);

                //Initiate Spinner
                mState = ConnectionState.IDLE;
                Log.d(TAG, "onClickRecord: CONNECTED -> IDLE");
//                        Spinner spinner = (Spinner) mView.findViewById(R.id.spinnerModels);
//                        spinner.setEnabled(true);
                SpeechToText.sharedInstance().stopRecognition();
                setButtonState(false);

                //countdowntimer.cancel();
                displayResult(mRecognitionResults);
                message = mRecognitionResults;
                textviewtimer.setText(" Count Down Finish ");

            }
        }
    }

    public void toneResults(View view){
        Intent intent = new Intent(SpeechToTextActivity.this, AnalyserTabActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }



    //Async Class that handles the Speech To Text Command
    public static class STTCommands extends AsyncTask<Void, Void, JSONObject> {

        protected JSONObject doInBackground(Void... none) {

            return SpeechToText.sharedInstance().getModels();
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Strictmode needed to run the http/wss request for devices > Gingerbread
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //set the content view
        setContentView(R.layout.content_activity_speech_to_text);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }






}