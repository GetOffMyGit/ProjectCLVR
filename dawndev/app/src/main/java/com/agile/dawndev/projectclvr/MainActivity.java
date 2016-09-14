package com.agile.dawndev.projectclvr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.ToneAnalyser.AnalyserTabActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/*
Launch activity, initialises the database and authenticates the user
 */
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTests = new ArrayList<Integer>();



//        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("tests").addValueEventListener(new ValueEventListener() {
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
//        });
    }

    // Once output from tone analyser is received, move to the tone analyser graph activity
    public void moveToToneResult(View view){
        Intent intent = new Intent(MainActivity.this, AnalyserTabActivity.class);
        startActivity(intent);
    }

    public void moveToSpeech(View view){
        Intent intent = new Intent(MainActivity.this, SpeechToTextActivity.class);
        startActivity(intent);
    }

    public void moveToPersonality(View view){
        Intent intent = new Intent(MainActivity.this, PersonalityActivity.class);
        startActivity(intent);
    }

    public void moveToEmail(View view){
        Intent intent = new Intent(MainActivity.this, SendEmailActivity.class);
        startActivity(intent);
    }
}
