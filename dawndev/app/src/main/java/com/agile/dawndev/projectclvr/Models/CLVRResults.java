package com.agile.dawndev.projectclvr.Models;

import java.util.HashMap;

/**
 * This model holds all the result related data fetched from FireBase
 */

public class CLVRResults {
    private static CLVRResults clvrResults;
    private String mCompanyName;
    private String mUsername;
    private String mCompanyEmail;
    private String mUserEmail;
    private String mTestname;
    private String mTestnumber;
    private String mOverallToneAnalysis;
    private String mOverallPersonalityInsights;
    private HashMap<Integer, CLVRQuestion> clvrQuestionHashMap;

    public CLVRResults() {
        clvrQuestionHashMap = new HashMap<Integer, CLVRQuestion>();
    }

    public static CLVRResults getInstance() {
        if(clvrResults == null) {
            clvrResults = new CLVRResults();
        }
        return clvrResults;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }

    public void setmCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmCompanyEmail() {
        return mCompanyEmail;
    }

    public void setmCompanyEmail(String mCompanyEmail) {
        this.mCompanyEmail = mCompanyEmail;
    }

    public String getmUserEmail() {
        return mUserEmail;
    }

    public void setmUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public String getmTestname() {
        return mTestname;
    }

    public void setmTestname(String mTestname) {
        this.mTestname = mTestname;
    }

    public String getmTestnumber() {
        return mTestnumber;
    }

    public void setmTestnumber(String mTestnumber) {
        this.mTestnumber = mTestnumber;
    }

    public String getmOverallToneAnalysis() {
        return mOverallToneAnalysis;
    }

    public void setmOverallToneAnalysis(String mOverallToneAnalysis) {
        this.mOverallToneAnalysis = mOverallToneAnalysis;
    }

    public String getmOverallPersonalityInsights() {
        return mOverallPersonalityInsights;
    }

    public void setmOverallPersonalityInsights(String mOverallPersonalityInsights) {
        this.mOverallPersonalityInsights = mOverallPersonalityInsights;
    }

    public HashMap<Integer, CLVRQuestion> getClvrQuestionHashMap() {
        return clvrQuestionHashMap;
    }

    public void setClvrQuestionHashMap(HashMap<Integer, CLVRQuestion> clvrQuestionHashMap) {
        this.clvrQuestionHashMap = clvrQuestionHashMap;
    }
}
