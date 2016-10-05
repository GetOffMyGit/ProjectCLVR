package com.agile.dawndev.projectclvr.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * This model holds all the question related data fetched from FireBase
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
