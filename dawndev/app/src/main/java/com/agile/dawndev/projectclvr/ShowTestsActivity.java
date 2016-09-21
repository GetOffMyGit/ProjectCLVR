package com.agile.dawndev.projectclvr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowTestsActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String mCompanyKey;
    private String mTestKey;
    private final ArrayList<String> mCompanyKeyArray = new ArrayList<String>();
    private final ArrayList<String> mTestKeyArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tests);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //mAuth.getCurrentUser().getUid()
        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot companies = dataSnapshot.child("companies");
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.showTestsLayout);
                //Loop through companies
                for(DataSnapshot companySnapshot : companies.getChildren()) {
                    TextView companyName = new TextView(ShowTestsActivity.this);
                    companyName.setText(companySnapshot.getKey());
                    linearLayout.addView(companyName);
                    //Loop through that company for their tests.
                    for(DataSnapshot testSnapshot : companySnapshot.getChildren()) {
                        Button testButton = new Button(ShowTestsActivity.this);
                        testButton.setText(testSnapshot.getValue().toString());
                        mCompanyKey = companySnapshot.getKey();
                        mTestKey = testSnapshot.getKey();
                        testButton.setOnClickListener(ShowTestsActivity.this);


//                        testButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(ShowTestsActivity.this, TestActivity.class);
//                                intent.setAction(Intent.ACTION_SEND);
//                                intent.putExtra("companyKey", companySnapshot.getKey());
//                                intent.putExtra("testKey", testSnapshot.getKey());
//                                intent.setType("text/plain");
//                                startActivity(intent);
//                            }
//                        });


                        linearLayout.addView(testButton);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ShowTestsActivity.this, TestActivity.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("companyKey", mCompanyKey);
        intent.putExtra("testKey", mTestKey);
        intent.setType("text/plain");
        startActivity(intent);
    }
}
