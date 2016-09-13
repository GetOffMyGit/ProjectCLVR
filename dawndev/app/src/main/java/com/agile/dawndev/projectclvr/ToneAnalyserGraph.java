package com.agile.dawndev.projectclvr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Zoe on 13/09/16.
 */
public class ToneAnalyserGraph  extends AppCompatActivity {
    private ColumnChartView emotionToneView;
    private ColumnChartView languageToneView;
    private ColumnChartView socialToneView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone_analyser_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emotionToneView = (ColumnChartView) findViewById(R.id.emotion_tone);
        languageToneView = (ColumnChartView) findViewById(R.id.language_tone);
        socialToneView = (ColumnChartView) findViewById(R.id.social_tone);

        JSONObject reader = null;
        try {
            String result;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    result = null;
                } else {
                    result = extras.getString("toneResult");
                }
            } else {
                result = (String) savedInstanceState.getSerializable("toneResult");
            }

            reader = new JSONObject(result);

            // Emotion Tone Graph
            String[] emotionToneLabels = new String[]{"Anger", "Disgust", "Fear", "Joy", "Sadness"};
            JSONArray emotionToneCategories  = reader.getJSONArray("tone_categories");
            JSONArray emotionTones = emotionToneCategories.getJSONObject(0).getJSONArray("tones");
            addColumns(emotionToneView, emotionTones, emotionToneLabels);

            // Emotion Tone Graph
            String[] languageToneLabels = new String[]{"Analytical", "Confident", "Tentative"};
            JSONArray languageToneCategories  = reader.getJSONArray("tone_categories");
            JSONArray languageTones = languageToneCategories.getJSONObject(1).getJSONArray("tones");
            addColumns(languageToneView, languageTones, languageToneLabels);

            // Social Tone Graph
            String[] socialToneLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Emotional Range"};
            JSONArray socialToneCategories  = reader.getJSONArray("tone_categories");
            JSONArray socialTones = languageToneCategories.getJSONObject(2).getJSONArray("tones");
            addColumns(socialToneView, socialTones, socialToneLabels);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void addColumns(ColumnChartView view, JSONArray dataArray, String[] dataLabels){
        List<Column> columns = new ArrayList<Column>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for(int i = 0; i<dataArray.length(); i++){
            List<SubcolumnValue> subColumn = new ArrayList<SubcolumnValue>();
            try {
                subColumn.add(new SubcolumnValue((float)dataArray.getJSONObject(i).getDouble("score"), ChartUtils.pickColor()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            columns.add(new Column(subColumn));
            axisValues.add(new AxisValue(i).setLabel(dataLabels[i]));
        }

        ColumnChartData socialColumnChart = new ColumnChartData(columns);
        socialColumnChart.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        socialColumnChart.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));
        view.setColumnChartData(socialColumnChart);
    }

}