package com.agile.dawndev.projectclvr;

import android.os.AsyncTask;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

/**
 * Created by Zoe on 19/08/16.
 */
public class ToneAnalyzerAsync extends AsyncTask<Object, Void, String>{
    private TextView tv;
    @Override
    protected String doInBackground(Object... input) {
        ToneAnalyzer service = (ToneAnalyzer) input[0];
        String text = (String) input[1];
tv = (TextView) input[2];
        ToneAnalysis tone = service.getTone(text, null).execute();
        return tone.toString();
    }

    @Override
    protected  void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            tv.setText(s);
        }

    }
    interface Tone {
         void getText(String string);
    }
}
