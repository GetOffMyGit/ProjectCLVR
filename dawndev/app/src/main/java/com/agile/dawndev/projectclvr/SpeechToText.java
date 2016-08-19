package com.agile.dawndev.projectclvr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.speech.RecognizerIntent;


public class SpeechToText extends AppCompatActivity {

    private Button record;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        record = (Button) findViewById(R.id.record);
        text = (TextView) findViewById(R.id.text);

       final  int counter =0;

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("button", "clicked");
                text.append(counter+"");


            }
        });

    }
    public void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
