package com.agile.dawndev.projectclvr.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjpark on 21/09/16.
 */
public class Company {
    private long id;
    private String name;
    private int available;
    private List<Test> tests;

    public Company() {
        name = null;
        tests = new ArrayList<Test>();
    }

    public String getName() {
        return this.name;
    }

    public int getAvailable() {
        return this.available;
    }

    public List<Test> getTests() {
        return this.tests;
    }
}
