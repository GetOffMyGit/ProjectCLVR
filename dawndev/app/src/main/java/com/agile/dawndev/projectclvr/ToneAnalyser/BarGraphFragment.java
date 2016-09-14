package com.agile.dawndev.projectclvr.ToneAnalyser;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agile.dawndev.projectclvr.R;

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

/* This fragment renders three bar graphs for each of the tone analyser outputs:
    emotion, language, and social
 */
public class BarGraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ColumnChartView emotionToneView;
    private ColumnChartView languageToneView;
    private ColumnChartView socialToneView;

    public BarGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarGraphFragment newInstance(String param1, String param2) {
        BarGraphFragment fragment = new BarGraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /*
    Initialises the graphs and adds the tone analyser JSON results to the graphs
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_bar_graph, container, false);


        emotionToneView = (ColumnChartView) inflatedView.findViewById(R.id.emotion_tone);
        languageToneView = (ColumnChartView) inflatedView.findViewById(R.id.language_tone);
        socialToneView = (ColumnChartView) inflatedView.findViewById(R.id.social_tone);


        JSONObject reader = null;
        try {
            String result;
            AnalyserTabActivity activity = (AnalyserTabActivity) getActivity();

            result = activity.getBarGraphString();

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

        // Inflate the layout for this fragment
        return inflatedView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        mListener = null;
    }

    /*
        Adds scores as bars to the bar graph
     */
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
