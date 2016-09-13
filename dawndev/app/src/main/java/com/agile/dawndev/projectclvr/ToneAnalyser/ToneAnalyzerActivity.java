package com.agile.dawndev.projectclvr.ToneAnalyser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.agile.dawndev.projectclvr.R;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;

public class ToneAnalyzerActivity extends AppCompatActivity implements AsyncResponse{
    private ToneAnalyzer toneAnalyzerService;
    private EditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone_analyzer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toneAnalyzerService = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        toneAnalyzerService.setUsernameAndPassword("345d437c-b0d0-4f07-8b0e-3a5bb21a4931", "qsWAYzFipvWy");
        inputEditText = (EditText) findViewById(R.id.inputText_et);
        inputEditText.setText("I know the times are difficult! Our sales have been "
                + "disappointing for the past three quarters for our data analytics "
                + "product suite. We have a competitive data analytics product "
                + "suite in the industry. But we need to do our job selling it! "
                + "We need to acknowledge and fix our sales challenges. "
                + "We can’t blame the economy for our lack of execution! "
                + "We are missing critical sales opportunities. "
                + "Our product is in no way inferior to the competitor products. "
                + "Our clients are hungry for analytical tools to improve their "
                + "business outcomes. Economy has nothing to do with it.");

        Button analyzeTextButton = (Button) findViewById(R.id.analyze_text_button);

        analyzeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = inputEditText.getText().toString();

                ToneAnalyzerAsync toneAnalyser = new ToneAnalyzerAsync(ToneAnalyzerActivity.this);
                toneAnalyser.delegate = ToneAnalyzerActivity.this;
                toneAnalyser.execute(toneAnalyzerService, text);
            }
        });

    }

    @Override
    public void processFinish(String result){
        // Give text to Graph view
        Intent intent = new Intent(ToneAnalyzerActivity.this, ToneAnalyserGraph.class);
        intent.putExtra("toneResult", result);
        startActivity(intent);
    }
}