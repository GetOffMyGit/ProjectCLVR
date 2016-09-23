package com.agile.dawndev.projectclvr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NikhilActivity extends AppCompatActivity {

    private TextView nikhil;
    private DatabaseReference mDatabase;
    private String mCompanyKey;
    private String mTestKey;
    private HashMap<String, String> mInstructionAndAnswerMap = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nikhil);

        nikhil = (TextView) findViewById(R.id.first);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Get the key for the test in the database from the sent intent.
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mCompanyKey = extras.getString("companyKey");
                mTestKey = extras.getString("testKey");
                Log.d("AHHHHHHHHH", mCompanyKey);
            }
        } else {
            mCompanyKey = (String) savedInstanceState.getSerializable("companyKey");
            mTestKey = (String) savedInstanceState.getSerializable("testKey");
            Log.d("AHHHHHHHHH", mCompanyKey);
        }

        paulsucksmore();

    }

    public void paulsucksmore() {
        //DatabaseReference df = mDatabase;
        mDatabase.child("companies").child(mCompanyKey).child("tests").child(mTestKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {

                    nikhil.setText(questionSnapshot.getValue().toString());
                    Log.d("cj", questionSnapshot.getValue().toString());
                    mInstructionAndAnswerMap.put(questionSnapshot.getKey(), questionSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

}
