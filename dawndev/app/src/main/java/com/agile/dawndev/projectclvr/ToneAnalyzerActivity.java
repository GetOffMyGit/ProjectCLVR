package com.agile.dawndev.projectclvr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

public class ToneAnalyzerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone_analyzer);
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

        ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        service.setUsernameAndPassword("345d437c-b0d0-4f07-8b0e-3a5bb21a4931", "qsWAYzFipvWy");

        String text =
                "I know the times are difficult! Our sales have been "
                        + "disappointing for the past three quarters for our data analytics "
                        + "product suite. We have a competitive data analytics product "
                        + "suite in the industry. But we need to do our job selling it! "
                        + "We need to acknowledge and fix our sales challenges. "
                        + "We can’t blame the economy for our lack of execution! "
                        + "We are missing critical sales opportunities. "
                        + "Our product is in no way inferior to the competitor products. "
                        + "Our clients are hungry for analytical tools to improve their "
                        + "business outcomes. Economy has nothing to do with it.";

        // Call the service and get the tone
        new ToneAnalyzerAsync().execute(service, text);
//        ToneAnalysis tone = service.getTone(text, null).execute();
//        System.out.println(tone);
    }

}
