package com.agile.dawndev.projectclvr.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * This model holds all the company related data fetched from FireBase
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
