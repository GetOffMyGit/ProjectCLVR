package com.agile.dawndev.projectclvr;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import org.json.JSONObject;
import org.w3c.dom.Text;

import lecho.lib.hellocharts.view.ColumnChartView;

import static java.security.AccessController.getContext;

public class ToneAnalyzerActivity extends AppCompatActivity implements ToneAnalyzerAsync.Tone {
    private ToneAnalyzer toneAnalyzerService;
    private EditText inputEditText;

//    private LinearLayout graphs;
    private ColumnChartView emotionToneView;
    private ColumnChartView languageToneView;
    private ColumnChartView socialToneView;

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

        emotionToneView = (ColumnChartView) findViewById(R.id.emotion_tone);
        languageToneView = (ColumnChartView) findViewById(R.id.language_tone);
        socialToneView = (ColumnChartView) findViewById(R.id.social_tone);
//        graphs = (LinearLayout) findViewById(R.id.graphs);

        Button analyzeTextButton = (Button) findViewById(R.id.analyze_text_button);


        analyzeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = inputEditText.getText().toString();
                new ToneAnalyzerAsync(ToneAnalyzerActivity.this).execute(toneAnalyzerService, text, emotionToneView, languageToneView, socialToneView);
            }
        });

    }

    @Override
    public void setChart(JSONObject result) {

    }
}
