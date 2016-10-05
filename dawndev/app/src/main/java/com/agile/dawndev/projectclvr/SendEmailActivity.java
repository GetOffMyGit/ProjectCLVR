package com.agile.dawndev.projectclvr;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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
                //Create SendGridSendEmail object for company pdf. Send context and email content.
                SendGridSendEmail task = new SendGridSendEmail(SendEmailActivity.this);
                //Execute async task.
                task.execute();

                //Create SendGridSendEmail object for user pdf. Send context and email content.
                TranscribeAnswerEmail task2 = new TranscribeAnswerEmail(SendEmailActivity.this);
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
