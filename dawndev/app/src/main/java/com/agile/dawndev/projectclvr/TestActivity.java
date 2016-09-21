package com.agile.dawndev.projectclvr;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String mCompanyKey;
    private String mTestKey;

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
            }
        } else {
            mCompanyKey = (String) savedInstanceState.getSerializable("companyKey");
            mTestKey = (String) savedInstanceState.getSerializable("testKey");
        }

        mDatabase.child("users").child("1").child("companies").child(mCompanyKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot test = dataSnapshot.child(mTestKey);

                for(DataSnapshot questionSnapshot : test.getChildren()) {
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
