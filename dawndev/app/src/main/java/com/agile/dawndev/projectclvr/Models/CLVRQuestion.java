package com.agile.dawndev.projectclvr.Models;

/**
 * This model holds all the question related data fetched from FireBase
 */

public class CLVRQuestion {
    private String mQuestion;
    private String mAnswer;
    private String mMediaURL;
    private String mToneAnalysis;

    public String getmQuestion() {
        return mQuestion;
    }

    public String getmAnswer() {
        return mAnswer;
    }

    public String getmMediaURL() {
        return mMediaURL;
    }

    public String getmToneAnalysis() {
        return mToneAnalysis;
    }


    public CLVRQuestion(String mQuestion, String mAnswer, String mMediaURL, String mToneAnalysis) {
        this.mQuestion = mQuestion;
        this.mAnswer = mAnswer;
        this.mMediaURL = mMediaURL;
        this.mToneAnalysis = mToneAnalysis;
    }
}
