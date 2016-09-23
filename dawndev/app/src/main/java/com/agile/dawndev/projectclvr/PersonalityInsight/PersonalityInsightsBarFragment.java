package com.agile.dawndev.projectclvr.PersonalityInsight;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agile.dawndev.projectclvr.R;
import com.agile.dawndev.projectclvr.ToneAnalyser.AsyncResponse;

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
 * Created by Zoe on 21/09/16.
 */
public class PersonalityInsightsBarFragment extends android.support.v4.app.Fragment implements AsyncResponse {

//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;

    private ColumnChartView personalityView;
    private ColumnChartView consumerView;
    private ColumnChartView valuesView;
    private View inflatedView;

    PersonalityTabActivity personalityTabActivity;

    public PersonalityInsightsBarFragment(){
    }

    public static PersonalityInsightsBarFragment newInstance(String param1, String param2) {
        PersonalityInsightsBarFragment fragment = new PersonalityInsightsBarFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            this.text = this.getArguments().getString("text");
//        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_bar_graph, container, false);

        personalityView = (ColumnChartView) inflatedView.findViewById(R.id.firstGraph);
        consumerView = (ColumnChartView) inflatedView.findViewById(R.id.secondGraph);
        valuesView = (ColumnChartView) inflatedView.findViewById(R.id.thirdGraph);

        return inflatedView;
    }


    public void createGraphs(PersonalityTabActivity activity) {
        JSONObject reader = null;
        try {
//            PersonalityTabActivity activity = (PersonalityTabActivity) getActivity();
            this.personalityTabActivity = activity;

            String jsonResult = activity.getJsonResult();
            reader = new JSONObject(jsonResult);
            JSONArray results = reader.getJSONObject("tree").getJSONArray("children");

            // Personality Graph
            String[] personalityLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Neuroticism"};
            JSONArray personalityCategories = results.getJSONObject(0).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            System.out.println(personalityCategories);
            addColumns(personalityView, personalityCategories, personalityLabels);

            // Personality Graph
            String[] needsLabels = new String[]{"Challenge", "Closeness", "Curiosity", "Excitement", "Harmony", "Ideal", "Liberty", "Love", "Practicality", "Self-expression", "Stability", "Structure"};
            JSONArray needsCategories = results.getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            System.out.println(needsCategories);
            addColumns(consumerView, needsCategories, needsLabels);

            // Values Graph
            String[] valuesLabels = new String[]{"Conservation", "Openness to change", "Hedonism", "Self-enhancement", "Self-transcendence"};
            JSONArray valuesCategories = results.getJSONObject(2).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            System.out.println(valuesCategories);
            addColumns(valuesView, valuesCategories, valuesLabels);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
        Adds personality scores as bars to the bar graph
    */
    private void addColumns(ColumnChartView view, JSONArray dataArray, String[] dataLabels){
        List<Column> columns = new ArrayList<Column>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for(int i = 0; i<dataArray.length(); i++){
            List<SubcolumnValue> subColumn = new ArrayList<SubcolumnValue>();
            try {
                subColumn.add(new SubcolumnValue((float)dataArray.getJSONObject(i).getDouble("percentage"), ChartUtils.pickColor()));
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

    @Override
    public void processFinish(String result){
        createGraphs(personalityTabActivity);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
