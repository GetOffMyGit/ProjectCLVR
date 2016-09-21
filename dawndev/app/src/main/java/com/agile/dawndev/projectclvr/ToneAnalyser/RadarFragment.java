package com.agile.dawndev.projectclvr.ToneAnalyser;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the fragment that contains the radar graph which displays the results from the
 * tone analyzer activity
 */
public class RadarFragment extends Fragment {

    private RadarChart emotionChart;
    private RadarChart languageChart;
    private RadarChart socialChart;

    private JSONArray emotionTones;
    private JSONArray languageTones;
    private  JSONArray socialTones;

    private OnFragmentInteractionListener mListener;

    public RadarFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the radar fragment
     */
    public static RadarFragment newInstance() {
        RadarFragment fragment = new RadarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //called when the fragment is created
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
        emotionChart = (RadarChart) inflatedView.findViewById(R.id.emotionGraph);
        languageChart = (RadarChart) inflatedView.findViewById(R.id.languageGraph);
        socialChart = (RadarChart) inflatedView.findViewById(R.id.socialToneGraph);

        //define the labels for the graph
        String[]emotionLabels = new String[]{"Anger", "Disgust", "Fear", "Joy", "Sadness"};
        String[] languageToneLabels = new String[]{"Analytical", "Confident", "Tentative"};
        String[] socialToneLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Emotional Range"};


        try {
            //parse the result from the JSON result
            JSONObject reader = new JSONObject(Constants.ToneAnalyzerResult);

            // Emotion Tone Graph
            JSONArray emotionToneCategories  = reader.getJSONArray("tone_categories");
            emotionTones = emotionToneCategories.getJSONObject(0).getJSONArray("tones");
            // language Tone Graph
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

        return inflatedView;
    }

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

    /**
     *  Mandatory interface
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /*
        Generates the graph using the given inputs
     */
    public void setUpGraph(RadarChart chart, JSONArray array, final String[] labels){
        List<RadarEntry> entries = new ArrayList<RadarEntry>();
        //retrieve the entry values
        for ( int i = 0; i<array.length();i++){
            try {
                entries.add(new RadarEntry(Float.parseFloat(array.getJSONObject(i).get("score").toString()) ,array.getJSONObject(0).get("tone_name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // add entries to dataset
        RadarDataSet dataSet = new RadarDataSet(entries, "Tone Analyzer");

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

        //converting the current root view to a bitmap (image)
        v1.setDrawingCacheEnabled(true);


        v1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v1.layout(0,0,v1.getMeasuredWidth(), v1.getMeasuredHeight());
        v1.buildDrawingCache(true);


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
