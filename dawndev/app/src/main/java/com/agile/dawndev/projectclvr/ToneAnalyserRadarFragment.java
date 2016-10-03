package com.agile.dawndev.projectclvr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
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

        Log.d("ZOE", screenshotArea.toString());

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
                //for(int i =0; i<10; i++){

                    //graphScrollView.setVisibility(View.VISIBLE);
                    createGraphs();
                    makePDF(screenshotArea);
                    //graphScrollView.setVisibility(View.INVISIBLE);
                    //nextQuestion.setVisibility(View.VISIBLE);

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

    private void goTo() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void createGraphs() {
        JSONObject reader = null;
        try {
            this.graphGenActivity = (GraphGenActivity)getActivity();
            String jsonResult = graphGenActivity.getJsonResult();

            Log.d("ZOE", jsonResult);

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    /*
        Screenshot taking for emailing graph results
     */

    public void makePDF(View rootView) {
        Log.d("screenshot", rootView.toString());
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
        Bitmap screen;
        View v1 = rootView;

        //converting the current root view to a bitmap (image)
        v1.setDrawingCacheEnabled(true);
//        v1.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,
//                ScrollView.LayoutParams.WRAP_CONTENT));
//
//        v1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        v1.layout(0, 0, v1.getMeasuredWidth(), v1.getMeasuredHeight());

        v1.buildDrawingCache(true);


        screen = Bitmap.createBitmap(v1.getDrawingCache());


        Log.d("screenshot", screen.toString());

        v1.setDrawingCacheEnabled(false);

        OutputStream foutPdf = null;
        OutputStream foutImage = null;

        try {
            File imageFile = new File(pdfDir + "/graphScreenShot.png");
            foutImage = new FileOutputStream(imageFile);
            foutPdf = new FileOutputStream(new File(pdfDir + "/graphResult.pdf"));
            screen.compress(Bitmap.CompressFormat.PNG, 90, foutImage);
            foutImage.flush();
            foutImage.close();

            Document document = new Document();
            PdfWriter.getInstance(document, foutPdf);
            document.open();

            //for each question, call this method and pass the right imageFile to get the graphs
            for(int i =0; i<5; i++) {
                addQuestionAnswerAndGraph(document, imageFile);
            }

            document.close();
            foutPdf.close();

            // openScreenshot(imageFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    public void addQuestionAnswerAndGraph(Document document, File imageFile) throws DocumentException, IOException {
        Image graph = Image.getInstance(imageFile.getAbsolutePath());
        graph.scaleAbsolute(500, 500);

        document.add(new Paragraph("Question "+counter+ " "+new Date()));
        counter++;

        //add question and answer from db
        document.add(new Paragraph("How are you feeling today?"));
        document.add(new Paragraph("answer: like shit"));
        document.add(graph);
    }


    public void fetchQuestion(){
//       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        mDatabase.child("companies").child(mCompanyKey).child("tests").child(mTestKey).child("Questions").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
//                    mInstructionAndAnswerMap.put(questionSnapshot.getKey(), questionSnapshot.getValue().toString());
//                }
//                updateText();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//
//        });
    }

//    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
//    {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // create a matrix for the manipulation
//        Matrix matrix = new Matrix();
//        // resize the bit map
//        matrix.postScale(scaleWidth, scaleHeight);
//        // recreate the new Bitmap
//        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
//        return resizedBitmap;
//    }


    private static void addImage(Document document, Bitmap bitmap) {
        try {
            Log.d("Generating pdf...", "Generating");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Image image = Image.getInstance(byteArray);
            ///Here i set byte array..you can do bitmap to byte array and set in image...
            try {
                document.add(image);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
//        try
//        {
//            document.add(image);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
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


    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);


                }
            }
        }
    }

}