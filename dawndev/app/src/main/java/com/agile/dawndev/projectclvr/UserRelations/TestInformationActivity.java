package com.agile.dawndev.projectclvr.UserRelations;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.R;
import com.agile.dawndev.projectclvr.Processing.SpeechAnalyserActivity;

public class TestInformationActivity extends AppCompatActivity {


    private String mCompanyName;
    private String mCompanyKey;
    private String mCompanyEmail;
    private String mTestKey;
    private String mTestString;
    private TextView mPrivacyPolicy;
    private TextView mTermsghggghg;
    private LayoutInflater mLayoutInflater;
    private PopupWindow mPopupWindow;
    private RelativeLayout mLayout;
    private TextView mTitle;
    private TextView mInformation;
    private TextView mExtraInfo;
    private TextView mTestName;
    private Button mAccept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_information);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Regular.otf");

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mCompanyKey = extras.getString("companyKey");
                mTestKey = extras.getString("testKey");
                mCompanyName = extras.getString("companyName");
                mCompanyEmail = extras.getString("companyEmail");
                mTestString = extras.getString("testName");
            }
        } else {
            mCompanyKey = (String) savedInstanceState.getSerializable("companyKey");
            mTestKey = (String) savedInstanceState.getSerializable("testKey");
            mCompanyName = (String) savedInstanceState.getSerializable("companyName");
            mCompanyEmail = (String) savedInstanceState.getSerializable("companyEmail");
            mTestString = (String) savedInstanceState.getSerializable("testName");

        }

        mTestName = (TextView) findViewById(R.id.test_name);
        mInformation = (TextView) findViewById(R.id.testInformation);
        mExtraInfo = (TextView) findViewById(R.id.extraInformation);
        mTitle = (TextView) findViewById(R.id.textView2);
        mTermsghggghg = (TextView) findViewById(R.id.viewTermsAndConditions);
        mPrivacyPolicy = (TextView) findViewById(R.id.viewPrivacyPolicy);
        mLayout = (RelativeLayout) findViewById(R.id.layout);
        mAccept = (Button) findViewById(R.id.accept);
//        layout_MainMenu.getForeground().setAlpha( 0); // restore


        mTestName.setTypeface(custom_font);
        mInformation.setTypeface(custom_font);
        mExtraInfo.setTypeface(custom_font);
        mTitle.setTypeface(custom_font);
        mTermsghggghg.setTypeface(custom_font);
        mPrivacyPolicy.setTypeface(custom_font);
        mAccept.setTypeface(custom_font);

        mTestName.setText(mTestString);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int width = dm.widthPixels;
        final int height = dm.heightPixels;



        mTermsghggghg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) mLayoutInflater.inflate(R.layout.terms_conditions_popup,null);

                mPopupWindow = new PopupWindow(container, (int) (width * 0.8), (int) (height * 0.9), true);
                mPopupWindow.showAtLocation(mLayout, Gravity.CENTER, 0, 0);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                mPopupWindow.setOutsideTouchable(true);
            }
        });



        mPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layout_MainMenu.getForeground().setAlpha( 180); // dim

                mLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) mLayoutInflater.inflate(R.layout.privacy_policy_popup,null);

                mPopupWindow = new PopupWindow(container, (int) (width * 0.8), (int) (height * 0.9), true);
                mPopupWindow.showAtLocation(mLayout, Gravity.CENTER, 0, 0);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                mPopupWindow.setOutsideTouchable(true);
            }
        });

    }


    public void startTest(View view) {
        Intent intent = new Intent(TestInformationActivity.this, SpeechAnalyserActivity.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("companyName", mCompanyName);
        intent.putExtra("companyKey", mCompanyKey);
        intent.putExtra("companyEmail", mCompanyEmail);
        intent.putExtra("testKey", mTestKey);
        intent.setType("text/plain");
        startActivity(intent);
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
