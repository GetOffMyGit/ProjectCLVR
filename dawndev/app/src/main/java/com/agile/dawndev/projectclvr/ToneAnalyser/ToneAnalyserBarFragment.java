package com.agile.dawndev.projectclvr.ToneAnalyser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;

import com.agile.dawndev.projectclvr.R;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;


public class ToneAnalyserBarFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    OnTextSelectedListener mCallback;

    private ColumnChartView emotionToneView;
    private ColumnChartView languageToneView;
    private ColumnChartView socialToneView;

    private ToneAnalyzer toneAnalyzerService;
    private EditText inputEditText;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ToneAnalyserBarFragment() {
        // Required empty public constructor
    }

    public interface OnTextSelectedListener {
        public void onTextSelected(String text);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToneAnalyserBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToneAnalyserBarFragment newInstance(String param1, String param2) {
        ToneAnalyserBarFragment fragment = new ToneAnalyserBarFragment();
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

    public void createGraphs() {
        ViewGroup view = (ViewGroup) this.getView();
        view.removeAllViews();
        Log.d("screenshot", "inside create view");
        View newInflatedView = getActivity().getLayoutInflater().inflate(R.layout.fragment_bar_graph, view, false);

        emotionToneView = (ColumnChartView) newInflatedView.findViewById(R.id.emotion_tone);
        languageToneView = (ColumnChartView) newInflatedView.findViewById(R.id.language_tone);
        socialToneView = (ColumnChartView) newInflatedView.findViewById(R.id.social_tone);
        view.addView(newInflatedView);

        JSONObject reader = null;
        try {
            String result;
            AnalyserTabActivity activity = (AnalyserTabActivity) getActivity();

            // NOT USING BUNDLES NOW
//            if (activity.getBarGraphString() != null) {
            result = activity.getBarGraphString();
//            } else {
//                result = (String) savedInstanceState.getSerializable("toneResult");
//            }

            reader = new JSONObject(result);

            // Emotion Tone Graph
            String[] emotionToneLabels = new String[]{"Anger", "Disgust", "Fear", "Joy", "Sadness"};
            JSONArray emotionToneCategories = reader.getJSONArray("tone_categories");
            JSONArray emotionTones = emotionToneCategories.getJSONObject(0).getJSONArray("tones");
            addColumns(emotionToneView, emotionTones, emotionToneLabels);

            // Emotion Tone Graph
            String[] languageToneLabels = new String[]{"Analytical", "Confident", "Tentative"};
            JSONArray languageToneCategories = reader.getJSONArray("tone_categories");
            JSONArray languageTones = languageToneCategories.getJSONObject(1).getJSONArray("tones");
            addColumns(languageToneView, languageTones, languageToneLabels);

            // Social Tone Graph
            String[] socialToneLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Emotional Range"};
            JSONArray socialToneCategories = reader.getJSONArray("tone_categories");
            JSONArray socialTones = languageToneCategories.getJSONObject(2).getJSONArray("tones");
            addColumns(socialToneView, socialTones, socialToneLabels);

            makePDF(view);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /*
        Sets up the tone analyser API retrieval and sets the default text for tone analysis
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_tone_analyser_bar, container, false);
        toneAnalyzerService = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        toneAnalyzerService.setUsernameAndPassword("345d437c-b0d0-4f07-8b0e-3a5bb21a4931", "qsWAYzFipvWy");
        inputEditText = (EditText) inflatedView.findViewById(R.id.inputText_et);
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

        Button analyzeTextButton = (Button) inflatedView.findViewById(R.id.analyze_text_button);

        analyzeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = inputEditText.getText().toString();

                ToneAnalyzerAsync toneAnalyser = new ToneAnalyzerAsync(getActivity().getBaseContext());
                toneAnalyser.delegate = ToneAnalyserBarFragment.this;
                toneAnalyser.execute(toneAnalyzerService, text);
            }
        });

        return inflatedView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
        Adds tone scores as bars to the bar graph
     */
    private void addColumns(ColumnChartView view, JSONArray dataArray, String[] dataLabels) {
        List<Column> columns = new ArrayList<Column>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for (int i = 0; i < dataArray.length(); i++) {
            List<SubcolumnValue> subColumn = new ArrayList<SubcolumnValue>();
            try {
                subColumn.add(new SubcolumnValue((float) dataArray.getJSONObject(i).getDouble("score"), ChartUtils.pickColor()));
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
    public void processFinish(String result) {
        mCallback.onTextSelected(result);
        createGraphs();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnTextSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void makePDF(View rootView){
        Log.d("screenshot", rootView.toString());
       boolean success =  false;

        //Create a directory for your PDF
        //make a new clvr directory if it doesnt already exist
        File pdfDir = new File(Environment.getExternalStorageDirectory() +  "/CLVR");

        if (!pdfDir.exists()){
            success = pdfDir.mkdirs();
        }

        if(!success){
            Log.d("screesnhot", "folder not created");
        }else{
            Log.d("screenshot", "folder created");
        }

       // Log.d("screenshot", rootView.toString() );
        Bitmap screen;
        View v1 = rootView.getRootView();
        //Log.d("screenshot", v1.toString());

        //converting the current root view to a bitmap (image)
        v1.setDrawingCacheEnabled(true);
        screen = Bitmap.createBitmap(v1.getDrawingCache());
//        Log.d("screenshot", screen.toString() );
        v1.setDrawingCacheEnabled(false);


        OutputStream fout = null;
        File imageFile =  new File(pdfDir+"/hi.jpg" );
        try{
            fout = new FileOutputStream(imageFile);
            screen.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();

           openScreenshot(imageFile);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //function opens the screenshot in a new intent
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        Log.d("screenshot", "inside open screenshot");

        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        Log.d("screenshot", uri.toString());
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}

























