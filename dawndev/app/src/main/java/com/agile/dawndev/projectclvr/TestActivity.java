package com.agile.dawndev.projectclvr;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String mCompanyKey;
    private String mTestKey;
    //private HashMap<String, String> mTestQuestions = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.testLayout);

        //Get the key for the test in the database from the sent intent.
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                mCompanyKey = extras.getString("companyKey");
                mTestKey = extras.getString("testKey");
                Log.v("EXTRAAAAAAAAAAAA", mCompanyKey);
                Log.v("EXTRAAAAAAAAAAAA", mTestKey);
            }
        } else {
            mCompanyKey = (String) savedInstanceState.getSerializable("companyKey");
            mTestKey = (String) savedInstanceState.getSerializable("testKey");
            Log.v("ELSEEEEEEEEEEEEEEEEE", mCompanyKey);
            Log.v("ELSEEEEEEEEEEEEEEEEE", mTestKey);
        }

        mDatabase.child("companies").child("1").child("tests").child(mTestKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot test = dataSnapshot.child(mTestKey);
                  Log.v("AYYYYYYYYYYYYYYYYYEEE", dataSnapshot.getKey());
//                Log.v("AYYYYYYYYYYYYYYYYYEEE", dataSnapshot.getValue().toString());

                for(DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    TextView question = new TextView(TestActivity.this);
                    question.setText(questionSnapshot.getKey());
                    linearLayout.addView(question);

                    TextView answer = new TextView(TestActivity.this);
                    answer.setText(questionSnapshot.getValue().toString());
                    linearLayout.addView(answer);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
