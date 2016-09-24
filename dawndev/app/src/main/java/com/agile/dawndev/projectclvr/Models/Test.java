package com.agile.dawndev.projectclvr.Models;

import java.util.HashMap;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Test {
    private HashMap<String,Object> questions;
    private String name;

    public Test() {
    }

    public Test(HashMap<String, Object> questions, String name) {
        this.questions = questions;
        this.name = name;
    }

    public HashMap<String,Object> getQuestions() {
        return this.questions;
    }

    public String getName() {
        return this.name;
    }
}
