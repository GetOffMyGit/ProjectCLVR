package com.agile.dawndev.projectclvr.ToneAnalyser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.agile.dawndev.projectclvr.AsyncResponse;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import static android.app.PendingIntent.getActivity;

/**
 * Makes an Async call to the Watson Tone Analyser API and returns the result
 */
public class ToneAnalyzerAsync extends AsyncTask<Object, Void, String> {
    private Context context;

    public AsyncResponse delegate = null;

    boolean useAPI = false;

    public ToneAnalyzerAsync(Context context){
        this.context = context;
    }

    //retrieves the input text and sends it to the API to be analyzed
    @Override
    protected String doInBackground(Object... input) {
        ToneAnalyzer service = (ToneAnalyzer) input[0];
        String text = (String) input[1];

        if(this.useAPI){
            ToneAnalysis tone = service.getTone(text, null).execute();
            return tone.getDocumentTone().toString();
        } else{
            Log.d("ToneAnalyser: ", "Just using saved output");
            return this.output;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            delegate.processFinish(result);
        }
    }

    //Storing the tone analyser output so we don't have to waste API calls for testing
    // This is for the bar graph
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