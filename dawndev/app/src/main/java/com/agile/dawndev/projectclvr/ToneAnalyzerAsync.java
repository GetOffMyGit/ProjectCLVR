package com.agile.dawndev.projectclvr;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

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
 * Created by Zoe on 19/08/16.
 */
public class ToneAnalyzerAsync extends AsyncTask<Object, Void, String> {
    private ColumnChartView outputView;
    boolean useAPI = false;

    @Override
    protected String doInBackground(Object... input) {
        ToneAnalyzer service = (ToneAnalyzer) input[0];
        String text = (String) input[1];
        outputView = (ColumnChartView) input[2];
        if(this.useAPI){
            ToneAnalysis tone = service.getTone(text, null).execute();
            System.out.println(tone.getDocumentTone().toString());
            return tone.getDocumentTone().toString();
        } else{
            Log.d("Zoe: ", "Just using saved output");
            return this.output;
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            try {
                JSONObject reader = new JSONObject(result);

                // Emotion Tone Graph
                List<Column> emotionToneColumns = new ArrayList<Column>();
                String[] emotionToneLabels = new String[]{"Anger", "Disgust", "Fear", "Joy", "Sadness"};
                List<AxisValue> emotionAxisValues = new ArrayList<AxisValue>();
                JSONArray emotionToneCategories  = reader.getJSONArray("tone_categories");
                JSONArray emotionTones = emotionToneCategories.getJSONObject(0).getJSONArray("tones");

                for(int i = 0; i<emotionTones.length(); i++){
                    List<SubcolumnValue> subColumn = new ArrayList<SubcolumnValue>();
                    subColumn.add(new SubcolumnValue((float)emotionTones.getJSONObject(i).getDouble("score"), ChartUtils.pickColor()));
                    emotionToneColumns.add(new Column(subColumn));
                    emotionAxisValues.add(new AxisValue(i).setLabel(emotionToneLabels[i]));
                }

                ColumnChartData emotionColumnChart = new ColumnChartData(emotionToneColumns);
                emotionColumnChart.setAxisXBottom(new Axis(emotionAxisValues).setHasLines(true));
                emotionColumnChart.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));
                outputView.setColumnChartData(emotionColumnChart);

                

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    interface Tone {
        void setChart(JSONObject result);
    }

    String output = "{\n" +
            "   \"tone_categories\": [\n" +
            "     {\n" +
            "       \"category_id\": \"emotion_tone\",\n" +
            "       \"category_name\": \"Emotion Tone\",\n" +
            "       \"tones\": [\n" +
            "         {\n" +
            "           \"tone_id\": \"anger\",\n" +
            "           \"tone_name\": \"Anger\",\n" +
            "           \"score\": 0.83414\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"disgust\",\n" +
            "           \"tone_name\": \"Disgust\",\n" +
            "           \"score\": 0.229384\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"fear\",\n" +
            "           \"tone_name\": \"Fear\",\n" +
            "           \"score\": 0.263215\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"joy\",\n" +
            "           \"tone_name\": \"Joy\",\n" +
            "           \"score\": 0.018623\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"sadness\",\n" +
            "           \"tone_name\": \"Sadness\",\n" +
            "           \"score\": 0.153338\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"category_id\": \"language_tone\",\n" +
            "       \"category_name\": \"Language Tone\",\n" +
            "       \"tones\": [\n" +
            "         {\n" +
            "           \"tone_id\": \"analytical\",\n" +
            "           \"tone_name\": \"Analytical\",\n" +
            "           \"score\": 0.665\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"confident\",\n" +
            "           \"tone_name\": \"Confident\",\n" +
            "           \"score\": 0.0\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"tentative\",\n" +
            "           \"tone_name\": \"Tentative\",\n" +
            "           \"score\": 0.0\n" +
            "         }\n" +
            "       ]\n" +
            "     },\n" +
            "     {\n" +
            "       \"category_id\": \"social_tone\",\n" +
            "       \"category_name\": \"Social Tone\",\n" +
            "       \"tones\": [\n" +
            "         {\n" +
            "           \"tone_id\": \"openness_big5\",\n" +
            "           \"tone_name\": \"Openness\",\n" +
            "           \"score\": 0.04\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"conscientiousness_big5\",\n" +
            "           \"tone_name\": \"Conscientiousness\",\n" +
            "           \"score\": 0.076\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"extraversion_big5\",\n" +
            "           \"tone_name\": \"Extraversion\",\n" +
            "           \"score\": 0.648\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"agreeableness_big5\",\n" +
            "           \"tone_name\": \"Agreeableness\",\n" +
            "           \"score\": 0.869\n" +
            "         },\n" +
            "         {\n" +
            "           \"tone_id\": \"emotional_range_big5\",\n" +
            "           \"tone_name\": \"Emotional Range\",\n" +
            "           \"score\": 0.966\n" +
            "         }\n" +
            "       ]\n" +
            "     }\n" +
            "   ]\n" +
            " }\n";
}
