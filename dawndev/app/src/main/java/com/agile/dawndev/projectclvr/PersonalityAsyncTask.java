package com.agile.dawndev.projectclvr;


import android.content.Context;
import android.os.AsyncTask;

import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

/**
 * Created by Obvio on 19/08/2016.
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

            mService = new PersonalityInsights();
            mService.setUsernameAndPassword(mContext.getString(R.string.personality_username), mContext.getString(R.string.personality_password));

            return mService.getProfile(params[0]).toString();

        }

    interface Personality {
        public void getText(String string);
    }

}
