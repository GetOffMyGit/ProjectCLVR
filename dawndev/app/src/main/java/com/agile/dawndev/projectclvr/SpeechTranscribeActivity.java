package com.agile.dawndev.projectclvr;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.dto.SpeechConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


public class SpeechTranscribeActivity extends AppCompatActivity implements ISpeechDelegate{

    private enum ConnectionState {
        IDLE, CONNECTING, CONNECTED
    }
    private static final String TAG = "SpeechTranscribe";
    ConnectionState mState = ConnectionState.IDLE;
    public View mView = null;
    public Context mContext = null;
    public JSONObject jsonModels = null;
    private Handler mHandler = null;
    private Button mButton;
    private TextView mText;
    private static String mRecognitionResults = "";
    private ItemModel mDefaultLanguageItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        mContext = this.getApplicationContext();
        mHandler = new Handler();
        mButton = (Button) findViewById(R.id.continue_button);
        mText = (TextView) findViewById(R.id.isThisRight);

        try {
            URI uri = new URI("wss://stream.watsonplatform.net/speech-to-text/api");
            SpeechToText.sharedInstance().initWithContext(uri, this.getApplicationContext(), new SpeechConfiguration());
        }
        catch (URISyntaxException e) {

        }
        String username = getString(R.string.STTdefaultUsername);
        String password = getString(R.string.STTdefaultPassword);
        SpeechToText.sharedInstance().setCredentials(username,password);
        SpeechToText.sharedInstance().setDelegate(this);
    }


    public void onClose(int code, String reason, boolean remote) {

    }

    public void onAmplitude(double amplitude, double volume) {

    }

    public void onError(String error) {

    }

    public void onMessage(String message) {

    }

    public void onOpen() {

    }

    public void recordSpeech(View arg0) {
        if (mState == ConnectionState.IDLE) {
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
//            ItemModel item = (ItemModel) mDefaultLanguageItem;
//            SpeechToText.sharedInstance().setModel(item.getModelName());
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
            mButton.setVisibility(View.VISIBLE);
            mText.setVisibility(View.VISIBLE);

            //Initiate Spinner
            mState = ConnectionState.IDLE;
            Log.d(TAG, "onClickRecord: CONNECTED -> IDLE");
//                        Spinner spinner = (Spinner) mView.findViewById(R.id.spinnerModels);
//                        spinner.setEnabled(true);
            SpeechToText.sharedInstance().stopRecognition();
            setButtonState(false);
            Log.d("CHRISTINA", mRecognitionResults);
            displayResult(mRecognitionResults);
        }
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

    public void setButtonLabel(final int buttonId, final String label) {
        final Runnable runnableUi = new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(buttonId);
                button.setText(label);
            }
        };
        new Thread() {
            public void run() {
                mHandler.post(runnableUi);
            }
        }.start();
    }

    public void setButtonState(final boolean bRecording) {

        final Runnable runnableUi = new Runnable() {
            @Override
            public void run() {
                int iDrawable = bRecording ? R.drawable.button_record_stop : R.drawable.button_record_start;
                Button btnRecord = (Button) findViewById(R.id.buttonRecord);
                btnRecord.setBackground(getResources().getDrawable(iDrawable));
            }
        };
        new Thread() {
            public void run() {
                mHandler.post(runnableUi);
            }
        }.start();
    }

    public void displayResult(final String result) {
        final Runnable runnableUi = new Runnable() {
            @Override
            public void run() {
                TextView textResult = (TextView) findViewById(R.id.textResult);
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


}
