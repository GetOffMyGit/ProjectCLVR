package com.agile.dawndev.projectclvr.PersonalityInsight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agile.dawndev.projectclvr.R;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates the fragment that contains the radar graph which displays the results from the
 * tone analyzer activity
 */
public class PersonalityInsightsRadarFragment extends Fragment {

    private RadarChart personalityChart;
    private RadarChart consumerChart;
    private RadarChart valuesChart;

    private JSONArray personalityInsights;
    private JSONArray consumerInsights;
    private  JSONArray valuesInsights;

    PersonalityTabActivity personalityTabActivity;

    public PersonalityInsightsRadarFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the radar fragment
     */
    public static PersonalityInsightsRadarFragment newInstance() {
        PersonalityInsightsRadarFragment fragment = new PersonalityInsightsRadarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_radar, container, false);

        //find the graphs in the fragment
        personalityChart = (RadarChart) inflatedView.findViewById(R.id.firstChart);
        consumerChart = (RadarChart) inflatedView.findViewById(R.id.secondChart);
        valuesChart = (RadarChart) inflatedView.findViewById(R.id.thirdChart);

        return inflatedView;
    }

    public void createGraphs(PersonalityTabActivity activity) {
        JSONObject reader = null;
        try {
            this.personalityTabActivity = activity;

            String jsonResult = activity.getJsonResult();
            reader = new JSONObject(jsonResult);
            JSONArray results = reader.getJSONObject("tree").getJSONArray("children");

            // Personality Graph
            String[] personalityLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Neuroticism"};
            JSONArray personalityCategories = results.getJSONObject(0).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            System.out.println(personalityCategories);
            makeRadar(personalityChart, personalityCategories, personalityLabels);

            // Personality Graph
            String[] needsLabels = new String[]{"Challenge", "Closeness", "Curiosity", "Excitement", "Harmony", "Ideal", "Liberty", "Love", "Practicality", "Self-expression", "Stability", "Structure"};
            JSONArray needsCategories = results.getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            System.out.println(needsCategories);
            makeRadar(consumerChart, needsCategories, needsLabels);

            // Values Graph
            String[] valuesLabels = new String[]{"Conservation", "Openness to change", "Hedonism", "Self-enhancement", "Self-transcendence"};
            JSONArray valuesCategories = results.getJSONObject(2).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            System.out.println(valuesCategories);
            makeRadar(valuesChart, valuesCategories, valuesLabels);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
      Generates the graph using the given inputs
    */
    public void makeRadar(RadarChart chart, JSONArray array, final String[] labels){
        List<RadarEntry> entries = new ArrayList<RadarEntry>();
        //retrieve the entry values
        for ( int i = 0; i<array.length();i++){
            try {
                entries.add(new RadarEntry(Float.parseFloat(array.getJSONObject(i).get("percentage").toString()) ,array.getJSONObject(0).get("name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // add entries to dataset
        RadarDataSet dataSet = new RadarDataSet(entries, "Personality Insights Analyzer");

        //set how the graph looks
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
            //set the labels
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels[(int) value % labels.length];
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a=(Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     *  Mandatory interface
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
