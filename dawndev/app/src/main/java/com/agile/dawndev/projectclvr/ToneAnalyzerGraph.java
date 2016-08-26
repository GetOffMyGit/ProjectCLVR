package com.agile.dawndev.projectclvr;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.RadarChart;

import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ToneAnalyzerGraph extends AppCompatActivity {
    private RadarChart mChart;
    private JSONObject jsonObj;
    private JSONObject loudScreaming;
    private JSONArray loudScreaminng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone_analyzer_graph);

        mChart = (RadarChart) findViewById(R.id.chart1);

        try {
             jsonObj = new JSONObject(Constants.ToneAnalyzerResult).getJSONObject("document_tone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
             loudScreaming = jsonObj.getJSONObject("document_tone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
             loudScreaminng = loudScreaming.getJSONArray("tone_categories");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        List<RadarEntry> entries = new ArrayList<RadarEntry>();

      //  for (YourData data : dataObjects) {

            // turn your data into Entry objects
            entries.add(new RadarEntry((float)8.0,(float)9.0));
        entries.add(new RadarEntry((float)1.0,(float)3.0));
        entries.add(new RadarEntry((float)5.0,(float)5.0));
        RadarDataSet dataSet = new RadarDataSet(entries, "Label"); // add entries to dataset
//        dataSet.setColor(...);
//        dataSet.setValueTextColor(...);
        RadarData radarData = new RadarData(dataSet);
        mChart.setData(radarData);
        mChart.invalidate();
        //  }
    }
}
