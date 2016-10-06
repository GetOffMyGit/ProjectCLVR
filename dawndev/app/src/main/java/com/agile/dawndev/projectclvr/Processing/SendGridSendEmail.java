package com.agile.dawndev.projectclvr.Processing;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.agile.dawndev.projectclvr.Models.CLVRResults;
import com.agile.dawndev.projectclvr.R;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

import java.io.File;
import java.io.IOException;

/**
 * This is the async task that handles the email functionality.
 * This email is sent to the company/client by CLVR.
 * The attachment includes the questions, answers and the analysed graph results for a test.
 */
public class SendGridSendEmail extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private String TAG = "SendGridSendEmail";

    //Fields for content to put into the email.
    private String mSendTo;
    private String mSentFrom;
    private String mSubject;
    private String mBody;

    //Constructor providing context and content for the email.
    public SendGridSendEmail(Context context) {
        this.mContext = context;

        //Set content for email from constructor.
        mSendTo = CLVRResults.getInstance().getmCompanyEmail();
        mSentFrom = "clvrapplication@gmail.com";
        mSubject = "Results from "+ CLVRResults.getInstance().getmUsername()+"'s test";
        mBody = "Please review attached PDF.";
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

        //add attachment with graphs
        File pdfDir = new File(Environment.getExternalStorageDirectory() + "/CLVR");
        try {
            email.addAttachment("clvr.pdf", new File(pdfDir + "/graphResult.pdf"));
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
