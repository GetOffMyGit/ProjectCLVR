package com.agile.dawndev.projectclvr.ToneAnalyser;

/*
    Interface for the tone analyser async to pass the output to the main tone analyser activity
 */
public interface AsyncResponse {
    void processFinish(String output);
}