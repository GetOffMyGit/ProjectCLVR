package com.agile.dawndev.projectclvr.UserRelations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.agile.dawndev.projectclvr.ListViews.CompanyListActivity;
import com.agile.dawndev.projectclvr.R;

/**
 * This activity handles all the functionality related to the end of test screen
 */

public class EndOfTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_test);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void goToHome(View view) {
        // return back to home screen
        Intent intent = new Intent(EndOfTestActivity.this, CompanyListActivity.class);
        startActivity(intent);
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

