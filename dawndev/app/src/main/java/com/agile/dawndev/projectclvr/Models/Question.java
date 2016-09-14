package com.agile.dawndev.projectclvr.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cjpark and Nikhil on 26/08/16.
 */
@IgnoreExtraProperties
public class Question {
    private String id;
    private String questionText;

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public Question () {

    }

    public String getQuestionText() {
        return this.questionText;
    }
}
