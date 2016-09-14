package com.agile.dawndev.projectclvr;

import android.content.Context;
import android.os.AsyncTask;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

/**
 * Created by Paul Joo on 13/09/2016.
 */
public class SendGridSendEmail extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    //Fields for content to put into the email.
    private String mSendTo;
    private String mSentFrom;
    private String mSubject;
    private String mBody;

    //Constructor providing context and content for the email.
    public SendGridSendEmail(Context context, String sentTo, String sentFrom, String subject, String body) {
        this.mContext = context;

        //Set content for email from constructor.
        mSendTo = sentTo;
        mSentFrom = sentFrom;
        mSubject = subject;
        mBody = body;
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

        //Send email.
        try {
            SendGrid.Response response = sendGrid.send(email);
        } catch (SendGridException e) {
            e.printStackTrace();
        }

        return null;
    }
}