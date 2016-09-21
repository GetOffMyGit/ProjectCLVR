package com.agile.dawndev.projectclvr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowTestsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.testsLayout);
                //Loop through companies
                for(DataSnapshot companySnapshot : companies.getChildren()) {
                    TextView companyName = new TextView(ShowTestsActivity.this);
                    companyName.setText(companySnapshot.getKey().toString());
                    linearLayout.addView(companyName);
                    //Loop through that company for their tests.
                    for(DataSnapshot testSnapshot : companySnapshot.getChildren()) {
                        Button testButton = new Button(ShowTestsActivity.this);
                        testButton.setText(testSnapshot.getValue().toString());
                        linearLayout.addView(testButton);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                Log.e("Count " ,""+snapshot.getChildrenCount());
//                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
//                    Log.e("Get Data", postSnapshot.toString());
//                    Boolean exists = (Boolean) postSnapshot.getValue();
//                    if (exists ==  true ) {
//                        mTests.add(Integer.parseInt(postSnapshot.getKey()));
//                        Log.e("testNumber", postSnapshot.getKey());
//                        mTest = Integer.parseInt(postSnapshot.getKey());
//                        Log.e("mTest", Integer.toString(mTest));

//
//                    }
////                    Question question = postSnapshot.getValue(Question.class);
////                    Log.d("nikhil", question.getQuestionText());
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError firebaseError) {
//                Log.e("The read failed: " ,firebaseError.getMessage());
//            }
        });
    }
}
