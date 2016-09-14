package com.agile.dawndev.projectclvr.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 *  Class for creating test questions
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
