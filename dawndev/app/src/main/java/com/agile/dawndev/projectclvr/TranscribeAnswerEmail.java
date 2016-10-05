package com.agile.dawndev.projectclvr;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.agile.dawndev.projectclvr.Models.CLVRResults;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Paul Joo on 13/09/2016.
 */
public class TranscribeAnswerEmail extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    //Fields for content to put into the email.
    private String mSendTo;
    private String mSentFrom;
    private String mSubject;
    private String mBody;

    //Constructor providing context and content for the email.
    public TranscribeAnswerEmail(Context context) {
        this.mContext = context;

        //Set content for email from constructor.
        mSendTo = CLVRResults.getInstance().getmUserEmail();
        //mSendTo = "ccha504@aucklanduni.ac.nz";
        mSentFrom = CLVRResults.getInstance().getmUserEmail();
        mSubject = "Your transcript";
        mBody = "Please see the attached PDF for your transcript.";
    }

    //Async task
    @Override
    protected Void doInBackground(Void... params) {
        //Create SendGrid object from SendGrid API key.
        SendGrid sendGrid = new SendGrid(mContext.getResources().getString((R.string.sendGrid_apiKey)));

        //Create a SendGrid email.
        SendGrid.Email email = new SendGrid.Email();

        //Set email contents from the fields.
        email.addTo(mSendTo);
        email.setFrom(mSentFrom);
        email.setSubject(mSubject);
        email.setText(mBody);

        //add attachment with only the transcrip answers
        File pdfDir = new File(Environment.getExternalStorageDirectory() + "/CLVR");
        try {
            email.addAttachment("transcript.pdf", new File(pdfDir + "/transcript.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Send email.
        try {
            SendGrid.Response response = sendGrid.send(email);
        } catch (SendGridException e) {
            e.printStackTrace();
        }

        return null;
    }
}
