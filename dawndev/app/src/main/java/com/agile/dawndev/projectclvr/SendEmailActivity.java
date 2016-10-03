package com.agile.dawndev.projectclvr;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agile.dawndev.projectclvr.Models.CLVRResults;


/*
    Activity that contains sending email functionality. Currently has mock data for prototype
 */
public class SendEmailActivity extends AppCompatActivity {
    private EditText mToText;
    private EditText mFromText;
    private EditText mSubjectText;
    private EditText mBodyText;
    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        mToText = (EditText) findViewById(R.id.toEditText);
        mFromText = (EditText) findViewById(R.id.fromEditText);
        mSubjectText = (EditText) findViewById(R.id.subjectEditText);
        mBodyText = (EditText) findViewById(R.id.bodyEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        //Send email on button click.
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create SendGridSendEmail object. Send context and email content.

                //Log.d("email", CLVRResults.getInstance().getmCompanyEmail());
                SendGridSendEmail task = new SendGridSendEmail(SendEmailActivity.this, CLVRResults.getInstance().getmCompanyEmail(), mFromText.getText().toString(), mSubjectText.getText().toString(), mBodyText.getText().toString());
                //Execute async task.
                task.execute();

//              Log.d("email", CLVRResults.getInstance().getmUserEmail());
                TranscribeAnswerEmail task2 = new TranscribeAnswerEmail(SendEmailActivity.this, CLVRResults.getInstance().getmUserEmail(), mFromText.getText().toString(), mSubjectText.getText().toString(), mBodyText.getText().toString());
                //Execute async task.
                task2.execute();

                Context context = getApplicationContext();
                CharSequence text = "Email Sent!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            }
        });

    }
}
