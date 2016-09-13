package com.agile.dawndev.projectclvr;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.RadarChart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ToneAnalyzerGraph extends AppCompatActivity {
    private RadarChart emotionChart;
    private RadarChart languageChart;
    private RadarChart socialChart;

    private JSONArray emotionTones;
    private JSONArray languageTones;
    private  JSONArray socialTones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone_analyzer_graph);

        emotionChart = (RadarChart) findViewById(R.id.emotionGraph);
        languageChart = (RadarChart) findViewById(R.id.languageGraph);
        socialChart = (RadarChart) findViewById(R.id.socialToneGraph);
        String[]emotionLabels = new String[]{"Anger", "Disgust", "Fear", "Joy", "Sadness"};

        String[] languageToneLabels = new String[]{"Analytical", "Confident", "Tentative"};
        String[] socialToneLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Emotional Range"};
        try {
            JSONObject reader = new JSONObject(Constants.ToneAnalyzerResult);

            // Emotion Tone Graph
            JSONArray emotionToneCategories  = reader.getJSONArray("tone_categories");
             emotionTones = emotionToneCategories.getJSONObject(0).getJSONArray("tones");
            // Emotion Tone Graph
              JSONArray languageToneCategories  = reader.getJSONArray("tone_categories");
             languageTones = languageToneCategories.getJSONObject(1).getJSONArray("tones");
            // Social Tone Graph
            JSONArray socialToneCategories  = reader.getJSONArray("tone_categories");
             socialTones = languageToneCategories.getJSONObject(2).getJSONArray("tones");

        } catch (JSONException e) {
            e.printStackTrace();
        }

setUpGraph(emotionChart,emotionTones,emotionLabels);
        setUpGraph(languageChart,languageTones,languageToneLabels);
        setUpGraph(socialChart,socialTones,socialToneLabels);

    }
    public void setUpGraph(RadarChart chart, JSONArray array,String[] labels){
        List<RadarEntry> entries = new ArrayList<RadarEntry>();

//entries.add(new RadarEntry(emotionTones.get(0)))

        for ( int i = 0; i<array.length();i++){
            try {
                entries.add(new RadarEntry(Float.parseFloat(array.getJSONObject(i).get("score").toString()) ,array.getJSONObject(0).get("tone_name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        RadarDataSet dataSet = new RadarDataSet(entries, "Tone Analyzer"); // add entries to dataset

        dataSet.setColor(Color.rgb(103, 110, 129));
        dataSet.setFillColor(Color.rgb(103, 110, 129));
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(180);
        dataSet.setLineWidth(2f);
        dataSet.setDrawHighlightCircleEnabled(true);
        dataSet.setDrawHighlightIndicators(false);

        RadarData radarData = new RadarData(dataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new AxisValueFormatter() {

            private String[] mActivities = new String[]{"Anger", "Disgust", "Fear", "Joy", "Sadness"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        chart.setData(radarData);
        chart.setDescription("");
        chart.invalidate();
    }
}
