package com.agile.dawndev.projectclvr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.agile.dawndev.projectclvr.Models.CLVRQuestion;
import com.agile.dawndev.projectclvr.Models.CLVRResults;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Creates the fragment that contains the radar graph which displays the results from the
 * tone analyzer activity
 */
public class RadarGraphFragment extends Fragment {

    private RadarChart firstChart;
    private RadarChart secondChart;
    private RadarChart thirdChart;
    public AtomicInteger pdfcounter = new AtomicInteger();
    private String TAG = "RadarGraphFragment";
    private LinearLayoutCompat screenshotArea;
    private HashMap<Integer, CLVRQuestion> testResult;

    public RadarGraphFragment() {
        // Required empty public constructor
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
        final View inflatedView = inflater.inflate(R.layout.fragment_radar, container, false);
        screenshotArea = (LinearLayoutCompat) inflatedView.findViewById(R.id.linearLayoutGraphs);

        //find the graphs in the fragment
        firstChart = (RadarChart) inflatedView.findViewById(R.id.firstChart);
        secondChart = (RadarChart) inflatedView.findViewById(R.id.secondChart);
        thirdChart = (RadarChart) inflatedView.findViewById(R.id.thirdChart);

        firstChart.setNoDataText("");
        secondChart.setNoDataText("");
        thirdChart.setNoDataText("");

        return inflatedView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                // empty async task to delay computation with ui view
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                //create the graphs
                createAllToneGraphs();
                createPersonalityGraphs();
                String overallTone = CLVRResults.getInstance().getmOverallToneAnalysis();

                createToneGraph(overallTone, -2);
                //hide the graphs so the user cannot view the graphs
                firstChart.setVisibility(View.GONE);
                secondChart.setVisibility(View.GONE);
                thirdChart.setVisibility(View.GONE);

                //Generate PDF for company - with graphs
                GeneratePDFAsyncTask generatePDFAsyncTask = new GeneratePDFAsyncTask(true, "graphResult", getContext()) {
                    @Override
                    protected void onPostExecute(Long result) {


                        //Create SendGridSendEmail object for company pdf. Send context and email content.
                        SendGridSendEmail task = new SendGridSendEmail(getActivity()) {
                            @Override
                            protected void onPostExecute(Void result) {
                                //When both PDFs are sent, delete the files from device
                                whenDone();
                            }

                        };
                        //Execute async task.
                        task.execute();

                    }

                };
                generatePDFAsyncTask.execute();


                //Generate PDF for user - only transcription
                GeneratePDFAsyncTask generateTranscript = new GeneratePDFAsyncTask(false, "transcript", getContext()) {
                    @Override
                    protected void onPostExecute(Long result) {

                        //Create SendGridSendEmail object for user pdf. Send context and email content.
                        TranscribeAnswerEmail task2 = new TranscribeAnswerEmail(getActivity()) {
                            @Override
                            protected void onPostExecute(Void result) {
                                //When both PDFs are sent, delete the files from device
                                whenDone();
                            }

                        };
                        //Execute async task.
                        task2.execute();

                    }

                };
                generateTranscript.execute();

            }
        }.execute();

    }
    /*
    Create graphs showing the results recieved from tone analysing the users answers
     */
    public void createToneGraph(String jsonResult, int question) {
        JSONObject reader = null;
        try {
            reader = new JSONObject(jsonResult);
            JSONArray results = reader.getJSONArray("tone_categories");

            // Emotion Tone Graph
            String[] emotionLabels = new String[]{"Anger", "Disgust", "Fear", "Joy", "Sadness"};
            JSONArray emotionToneCategories = results.getJSONObject(0).getJSONArray("tones");
            makeRadar(firstChart, emotionToneCategories, emotionLabels, true);

            // language Tone Graph
            String[] languageToneLabels = new String[]{"Analytical", "Confident", "Tentative"};
            JSONArray languageToneCategories = results.getJSONObject(0).getJSONArray("tones");
            makeRadar(secondChart, languageToneCategories, languageToneLabels, true);

            // Social Tone Graph
            String[] socialToneLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Emotional Range"};
            JSONArray socialToneCategories = results.getJSONObject(0).getJSONArray("tones");
            makeRadar(thirdChart, socialToneCategories, socialToneLabels, true);

            takeScreenShot(screenshotArea, question);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    Create individual tone graphs for each question answered
    */
    public void createAllToneGraphs() {
        this.testResult = CLVRResults.getInstance().getClvrQuestionHashMap();

        for (int question : testResult.keySet()) {
            String jsonResult = testResult.get(question).getmToneAnalysis();
            createToneGraph(jsonResult, question);
        }
    }
    /*
    Create graphs showing the results recieved from analysing the users answers regarding personality
     */
    public void createPersonalityGraphs() {
        JSONObject reader = null;
        try {
            String jsonResult = CLVRResults.getInstance().getmOverallPersonalityInsights();
            reader = new JSONObject(jsonResult);
            JSONArray results = reader.getJSONObject("tree").getJSONArray("children");

            // Personality Graph
            String[] personalityLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Neuroticism"};
            JSONArray personalityCategories = results.getJSONObject(0).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            makeRadar(firstChart, personalityCategories, personalityLabels, false);

            // Personality Graph
            String[] needsLabels = new String[]{"Challenge", "Closeness", "Curiosity", "Excitement", "Harmony", "Ideal", "Liberty", "Love", "Practicality", "Self-expression", "Stability", "Structure"};
            JSONArray needsCategories = results.getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            makeRadar(secondChart, needsCategories, needsLabels, false);

            // Values Graph
            String[] valuesLabels = new String[]{"Conservation", "Openness to change", "Hedonism", "Self-enhancement", "Self-transcendence"};
            JSONArray valuesCategories = results.getJSONObject(2).getJSONArray("children").getJSONObject(0).getJSONArray("children");
            makeRadar(thirdChart, valuesCategories, valuesLabels, false);

            takeScreenShot(screenshotArea, -1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
        Generates the graph using the given inputs
     */
    public void makeRadar(RadarChart chart, JSONArray array, final String[] labels, boolean isTone) {

        List<RadarEntry> entries = new ArrayList<RadarEntry>();

        //retrieve the entry values
        if (isTone) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    entries.add(new RadarEntry(Float.parseFloat(array.getJSONObject(i).get("score").toString()), array.getJSONObject(0).get("tone_name")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < array.length(); i++) {
                try {
                    entries.add(new RadarEntry(Float.parseFloat(array.getJSONObject(i).get("percentage").toString()), array.getJSONObject(0).get("name")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // add entries to dataset
        RadarDataSet dataSet = new RadarDataSet(entries, "");

        // change the appearance of the resultant graph
        dataSet.setColor(Color.rgb(103, 110, 129));
        dataSet.setFillColor(Color.rgb(103, 110, 129));
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(180);
        dataSet.setLineWidth(2f);
        dataSet.setDrawHighlightCircleEnabled(true);
        dataSet.setDrawHighlightIndicators(false);

        RadarData radarData = new RadarData(dataSet);
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(6f);
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
        chart.getYAxis().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void takeScreenShot(View rootView, int questionNum) {
        Bitmap screen;
        View v1 = rootView;

        //converting the current root view to a bitmap (image)
        v1.setDrawingCacheEnabled(true);

        v1.layout(0, 0, v1.getMeasuredWidth(), v1.getMeasuredHeight());

        v1.buildDrawingCache(true);

        screen = Bitmap.createBitmap(v1.getDrawingCache());

        v1.setDrawingCacheEnabled(false);

        //Create a directory for your PDF
        //make a new clvr directory if it doesnt already exist
        File pdfDir = new File(Environment.getExternalStorageDirectory() + "/CLVR");

        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        // save this bitmap somewhere
        File imageFile = new File(pdfDir + "/graphScreenShot" + questionNum + ".png");
        OutputStream foutImage = null;

        try {
            foutImage = new FileOutputStream(imageFile);
            screen.compress(Bitmap.CompressFormat.PNG, 90, foutImage);
            foutImage.flush();
            foutImage.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
        When both PDFs are sent, delete the files from device
     */
    public void whenDone() {

        int count = pdfcounter.incrementAndGet();

        // both tasks are done
        if (count == 2) {
            File pdfDir = new File(Environment.getExternalStorageDirectory() + "/CLVR");

            if (pdfDir.isDirectory()) {
                String[] children = pdfDir.list();

                // delete each file in the directory
                for (int i = 0; i < children.length; i++) {
                    new File(pdfDir, children[i]).delete();
                }
            }

            // go to end of test activity
            Intent intent = new Intent(getActivity(), EndOfTestActivity.class);
            startActivity(intent);
            getActivity().finish();
        }


    }
}
