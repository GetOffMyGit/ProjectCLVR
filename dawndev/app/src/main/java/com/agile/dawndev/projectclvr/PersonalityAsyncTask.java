package com.agile.dawndev.projectclvr;


import android.content.Context;
import android.os.AsyncTask;

import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

/**
 * Makes an Async call to the Watson Tone Personality Insights API and returns the result
 */
public class PersonalityAsyncTask extends AsyncTask<String,Void,String> {

    Context mContext;
    Personality mPersonality;

    private PersonalityInsights mService;

    public PersonalityAsyncTask(Context mContext) {
        this.mContext = mContext;
        mPersonality = (Personality) mContext;
    }

        @Override
        protected  void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                mPersonality.getText(s);
            }

        }

        @Override
        protected String doInBackground(String... params) {

            // create personality insight service
            mService = new PersonalityInsights();

            // sets credientials to use personality insight
            mService.setUsernameAndPassword(mContext.getString(R.string.personality_username),
                    mContext.getString(R.string.personality_password));

            // returns json string value of result of personality
            return mService.getProfile(params[0]).execute().toString();
        }

    interface Personality {
        public void getText(String string);
    }

}
