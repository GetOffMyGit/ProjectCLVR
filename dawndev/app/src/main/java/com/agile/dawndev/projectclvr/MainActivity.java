package com.agile.dawndev.projectclvr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText mToText;
    private EditText mFromText;
    private EditText mSubjectText;
    private EditText mBodyText;
    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToText = (EditText) findViewById(R.id.toEditText);
        mFromText = (EditText) findViewById(R.id.fromEditText);
        mSubjectText = (EditText) findViewById(R.id.subjectEditText);
        mBodyText = (EditText) findViewById(R.id.bodyEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendGridSendEmail task = new SendGridSendEmail(MainActivity.this, mToText.getText().toString(), mFromText.getText().toString(), mSubjectText.getText().toString(), mBodyText.getText().toString());
                task.execute();
            }
        });
    }
}
