package com.agile.dawndev.projectclvr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ScrollView;

import com.agile.dawndev.projectclvr.Models.CLVRQuestion;
import com.agile.dawndev.projectclvr.Models.CLVRResults;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Creates the fragment that contains the radar graph which displays the results from the
 * tone analyzer activity
 */
public class ToneAnalyserRadarFragment extends Fragment {

    private RadarChart emotionChart;
    private RadarChart languageChart;
    private RadarChart socialChart;
    private int counter = 1;

    private ScrollView graphScrollView;

    private JSONArray emotionTones;
    private JSONArray languageTones;
    private JSONArray socialTones;

    LinearLayoutCompat screenshotArea;

    private ProgressBar mProgress;

    private Button nextQuestion;
    GraphGenActivity graphGenActivity;

    HashMap<Integer, CLVRQuestion> testResult;

    public ToneAnalyserRadarFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the radar fragment
     */
    public static ToneAnalyserRadarFragment newInstance() {
        ToneAnalyserRadarFragment fragment = new ToneAnalyserRadarFragment();
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
        final View inflatedView = inflater.inflate(R.layout.fragment_radar, container, false);
        LinearLayout graphCoverUpLayout = (LinearLayout) inflatedView.findViewById(R.id.graphCoverUpLayout);
        graphCoverUpLayout.bringToFront();

        screenshotArea = (LinearLayoutCompat) inflatedView.findViewById(R.id.linearLayoutGraphs);

        //graphScrollView = (ScrollView) inflatedView.findViewById(R.id.graphScrollView);
        //graphScrollView.setVisibility(View.INVISIBLE);
        mProgress = (ProgressBar) inflatedView.findViewById(R.id.progress_bar);

        //find the graphs in the fragment

        emotionChart = (RadarChart) inflatedView.findViewById(R.id.firstChart);
        languageChart = (RadarChart) inflatedView.findViewById(R.id.secondChart);
        socialChart = (RadarChart) inflatedView.findViewById(R.id.thirdChart);
        nextQuestion = (Button) inflatedView.findViewById(R.id.getResult);
//        graphScrollView.setVisibility(View.INVISIBLE);

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nextQuestion.setVisibility(View.INVISIBLE);

                //for(int i =0; i<10; i++){

                    //graphScrollView.setVisibility(View.VISIBLE);
                    createGraphs();
                    makePDF(true, "graphResult");
                    makePDF(false, "transcript");
                    //graphScrollView.setVisibility(View.INVISIBLE);

                //}
                mProgress.setVisibility(View.GONE);
                nextQuestion.setVisibility(View.VISIBLE);


            }
        });

        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
//        createGraphs();
    }

    public void createGraphs() {
        JSONObject reader = null;
        try {
            this.graphGenActivity = (GraphGenActivity)getActivity();
            this.testResult = graphGenActivity.getJsonResult();

            for(int question : testResult.keySet()){
                String jsonResult = testResult.get(question).getmToneAnalysis();
                Log.d("zoe-chan", question + " result" + jsonResult);

                reader = new JSONObject(jsonResult);
                JSONArray results = reader.getJSONArray("tone_categories");

                // Emotion Tone Graph
                String[] emotionLabels = new String[]{"Anger", "Disgust", "Fear", "Joy", "Sadness"};
                JSONArray emotionToneCategories = results.getJSONObject(0).getJSONArray("tones");
                makeRadar(emotionChart, emotionToneCategories, emotionLabels);

                // language Tone Graph
                String[] languageToneLabels = new String[]{"Analytical", "Confident", "Tentative"};
                JSONArray languageToneCategories = results.getJSONObject(0).getJSONArray("tones");
                makeRadar(languageChart, languageToneCategories, languageToneLabels);

                // Social Tone Graph
                String[] socialToneLabels = new String[]{"Openness", "Conscientiousness", "Extraversion", "Agreeableness", "Emotional Range"};
                JSONArray socialToneCategories = results.getJSONObject(0).getJSONArray("tones");
                makeRadar(socialChart, socialToneCategories, socialToneLabels);

                takeScreenShot(screenshotArea, question);
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
        Generates the graph using the given inputs
     */
    public void makeRadar(RadarChart chart, JSONArray array, final String[] labels) {

        List<RadarEntry> entries = new ArrayList<RadarEntry>();
        //retrieve the entry values
        for (int i = 0; i < array.length(); i++) {
            try {
                entries.add(new RadarEntry(Float.parseFloat(array.getJSONObject(i).get("score").toString()), array.getJSONObject(0).get("tone_name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // add entries to dataset
        RadarDataSet dataSet = new RadarDataSet(entries,"");

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
//        mListener = null;
    }

    public void takeScreenShot(View rootView, int questionNum) {
        Bitmap screen;
        View v1 = rootView;

        //converting the current root view to a bitmap (image)
        v1.setDrawingCacheEnabled(true);

        v1.layout(0, 0, v1.getMeasuredWidth(), v1.getMeasuredHeight());

        v1.buildDrawingCache(true);

        screen = Bitmap.createBitmap(v1.getDrawingCache());

        Log.d("screenshot", screen.toString());

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

        Log.d("zoe chan", imageFile.getAbsolutePath() + " downloaded");


    }
    /*
        Screenshot taking for emailing graph results
     */

    public void makePDF(boolean haveImage, String fileName) {
        boolean success = false;

        nextQuestion.setVisibility(View.INVISIBLE);
        //Create a directory for your PDF
        //make a new clvr directory if it doesnt already exist
        File pdfDir = new File(Environment.getExternalStorageDirectory() + "/CLVR");

        if (!pdfDir.exists()) {
            success = pdfDir.mkdirs();
        }

        if (!success) {
            Log.d("screesnhot", "folder not created");
        } else {
            Log.d("screenshot", "folder created");
        }

        // Log.d("screenshot", rootView.toString() );

        OutputStream foutPdf = null;

        try {
            foutPdf = new FileOutputStream(new File(pdfDir + "/" + fileName + ".pdf"));

            Document document = new Document();
            PdfWriter.getInstance(document, foutPdf);
            document.open();

            document.add(new Chunk(""));
//            document.add(new Paragraph("Company:"));
//            document.add(new Paragraph("Candidate:"));
//            document.add(new Paragraph("Candidate Email:"));

            CLVRResults results = CLVRResults.getInstance();
            HashMap<Integer, CLVRQuestion> testResult = results.getClvrQuestionHashMap();

            //final graphs

            // iterate through all bitmaps or file location and call this
            for(int question : testResult.keySet()){
                File imageFile = new File(pdfDir + "/graphScreenShot" + question + ".png");
                CLVRQuestion clvrQuestion = testResult.get(question);

                addQuestionAnswerAndGraph(document, imageFile, clvrQuestion, haveImage);
            }

            document.close();
            foutPdf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Log.d("zoe-chan", "PDF generated");

    }
    public void addQuestionAnswerAndGraph(Document document, File imageFile, CLVRQuestion clvrQuestion, boolean addImage) throws DocumentException, IOException {

        document.add(new Paragraph("Question "+counter+ " "+new Date()));
        counter++;

        //add question and answer from db
        document.add(new Paragraph("Question: " + clvrQuestion.getmQuestion()));
        document.add(new Paragraph("Answer: " + clvrQuestion.getmAnswer()));
        document.add(new Paragraph("Voice Note: " + clvrQuestion.getmMediaURL()));

        if(addImage){
            Image graph = Image.getInstance(imageFile.getAbsolutePath());
            graph.scaleAbsolute(500, 500);
            document.add(graph);
            Log.d("zoe-chan", "new page added");
            document.newPage();
        }

    }
}
