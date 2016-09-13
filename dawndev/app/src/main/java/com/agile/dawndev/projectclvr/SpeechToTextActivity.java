package com.agile.dawndev.projectclvr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;

public class SpeechToTextActivity extends AppCompatActivity implements ISpeechDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_speech_to_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onAmplitude(double v, double v1) {

    }
}
