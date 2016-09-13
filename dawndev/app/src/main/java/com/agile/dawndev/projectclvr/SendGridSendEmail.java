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

    private String mSendTo;
    private String mSentFrom;
    private String mSubject;
    private String mBody;

    public SendGridSendEmail(Context context, String sentTo, String sentFrom, String subject, String body) {
        this.mContext = context;

        mSendTo = sentTo;
        mSentFrom = sentFrom;
        mSubject = subject;
        mBody = body;
    }

    @Override
    protected Void doInBackground(Void... params) {
        SendGrid sendGrid = new SendGrid(mContext.getResources().getString((R.string.sendGrid_apiKey)));

        SendGrid.Email email = new SendGrid.Email();

        email.addTo(mSendTo);
        email.setFrom(mSentFrom);
        email.setSubject(mSubject);
        email.setText(mBody);

        try {
            SendGrid.Response response = sendGrid.send(email);
        } catch (SendGridException e) {
            e.printStackTrace();
        }

        return null;
    }
}
