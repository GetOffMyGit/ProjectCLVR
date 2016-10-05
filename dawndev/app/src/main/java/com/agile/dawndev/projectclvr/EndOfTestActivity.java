package com.agile.dawndev.projectclvr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EndOfTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_test);

    }

    public void goToHome(View view) {
        Intent intent = new Intent(EndOfTestActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

