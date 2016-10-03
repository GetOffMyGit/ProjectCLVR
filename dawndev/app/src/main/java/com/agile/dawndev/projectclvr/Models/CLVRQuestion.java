package com.agile.dawndev.projectclvr.Models;

/**
 * Created by Paul Joo on 3/10/2016.
 */

public class CLVRQuestion {
    private String mQuestion;
    private String mAnswer;
    private String mMediaURL;
    private String mToneAnalysis;

    public CLVRQuestion(String mQuestion, String mAnswer, String mMediaURL, String mToneAnalysis) {
        this.mQuestion = mQuestion;
        this.mAnswer = mAnswer;
        this.mMediaURL = mMediaURL;
        this.mToneAnalysis = mToneAnalysis;
    }
}
