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

import com.agile.dawndev.projectclvr.AsyncResponse;
import com.agile.dawndev.projectclvr.R;

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

//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;

    View inflatedView;

    private ColumnChartView emotionToneView;
    private ColumnChartView languageToneView;
    private ColumnChartView socialToneView;

    ToneTabActivity toneTabActivity;

//    private OnFragmentInteractionListener mListener;

    public ToneAnalyserBarFragment() {
        // Required empty public constructor
    }

    public static ToneAnalyserBarFragment newInstance(String param1, String param2) {
        ToneAnalyserBarFragment fragment = new ToneAnalyserBarFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_bar_graph, container, false);

        emotionToneView = (ColumnChartView) inflatedView.findViewById(R.id.firstGraph);
        languageToneView = (ColumnChartView) inflatedView.findViewById(R.id.secondGraph);
        socialToneView = (ColumnChartView) inflatedView.findViewById(R.id.thirdGraph);

        return inflatedView;
    }

    public void createGraphs(ToneTabActivity activity) {
        Log.d("screenshot", "inside create view");

        JSONObject reader = null;
        try {
            this.toneTabActivity = activity;

            String jsonResult = activity.getJsonResult();
            reader = new JSONObject(jsonResult);

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

    /*
        Adds tone scores as bars to the bar graph
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

    @Override
    public void processFinish(String result){
//        mCallback.onTextSelected(result);
        createGraphs(toneTabActivity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
//            mCallback = (OnTextSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
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



    /*
        Screenshot taking for email
     */

    public void makePDF(View rootView){
        Log.d("screenshot", rootView.toString());

        //Create a directory for your PDF
        //make a new clvr directory if it doesnt already exist
        File pdfDir = new File(Environment.getExternalStorageDirectory() +  "/CLVR");

        if (!pdfDir.exists()){
            pdfDir.mkdir();
        }

        //Then take the screen shot
        Log.d("screenshot", rootView.toString() );
        Bitmap screen;
        View v1 = rootView.getRootView();
        Log.d("screenshot", v1.toString());
        //converting the current root view to a bitmap (image)
        v1.setDrawingCacheEnabled(true);
        screen = Bitmap.createBitmap(v1.getDrawingCache());
        Log.d("screenshot", screen.toString() );
        v1.setDrawingCacheEnabled(false);

        //Now create the name of your PDF file that you will generate
        //pdf file that is supposed to load the image on
        File pdfFile = new File(pdfDir, "myPdfFile.pdf");
        Log.d("screenshot", pdfFile.toString());

        //the file that contains the screenshot image
        OutputStream fout = null;
        File imageFile = new File(pdfDir.getPath() + File.separator
                + "hi"  + ".jpg");

        try {
            Log.d("screenshot", "inside try agian" );
            //writing to hi.jpg
            fout = new FileOutputStream(imageFile);
            Log.d("screenshot", fout.toString() );
            screen.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();

            Log.d("screenshot", "before open screenshot");
            //openScreenshot(imageFile);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //TODO add the image file hi.jpg to pdfFile
//
//        try {
//            DocumentsContract.Document document = new DocumentsContract.Document();
//
//            PdfWriter.getInstance(document, new FileOutputStream(file));
//            document.open();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//            //addImage(document,byteArray);
//            document.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }


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
