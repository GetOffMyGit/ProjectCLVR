package com.agile.dawndev.projectclvr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.Models.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mQuestion;
    private TextView mQuestion2;

    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private ArrayList<Integer> mTests;
    private Integer mTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mQuestion = (TextView) findViewById(R.id.question_name);
        mQuestion2 = (TextView) findViewById(R.id.question_name2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTests = new ArrayList<Integer>();


        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.e("Get Data", postSnapshot.toString());
                    Boolean exists = (Boolean) postSnapshot.getValue();
                    if (exists ==  true ) {
                        mTests.add(Integer.parseInt(postSnapshot.getKey()));
                        Log.e("testNumber", postSnapshot.getKey());
                        mTest = Integer.parseInt(postSnapshot.getKey());
                        Log.e("mTest", Integer.toString(mTest));

                    }
//                    Question question = postSnapshot.getValue(Question.class);
//                    Log.d("nikhil", question.getQuestionText());
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });


    }


}
