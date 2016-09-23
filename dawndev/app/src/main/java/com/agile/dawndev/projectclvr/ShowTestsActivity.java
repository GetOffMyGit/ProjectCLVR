package com.agile.dawndev.projectclvr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.PersonalityInsight.PersonalityTabActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowTestsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private HashMap<String,TextView> textViews = new HashMap<String,TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tests2);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //mAuth.getCurrentUser().getUid()
        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot companies = dataSnapshot.child("companies");
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.showTestsLayout);
                //Loop through companies
                for(final DataSnapshot companySnapshot : companies.getChildren()) {
                    TextView companyName = new TextView(ShowTestsActivity.this);
                    String companyID = companySnapshot.getKey();
                    companyName.setText(companyID);
                    linearLayout.addView(companyName);
                    textViews.put(companyID, companyName);
                    //Loop through that company for their tests.
                    for(final DataSnapshot testSnapshot : companySnapshot.getChildren()) {
                        Button testButton = new Button(ShowTestsActivity.this);
                        testButton.setText(testSnapshot.getValue().toString());

                        testButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ShowTestsActivity.this, SpeechAnalyserActivity.class);
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra("companyKey", companySnapshot.getKey());
                                intent.putExtra("testKey", testSnapshot.getKey());
                                intent.setType("text/plain");
                                startActivity(intent);
                            }
                        });
                        linearLayout.addView(testButton);
                    }

                }
                nameCompanies();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void nameCompanies() {
        for (final String companyID : textViews.keySet()) {
            mDatabase.child("companies").child(companyID).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView companyToName = textViews.get(companyID);
                    companyToName.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}