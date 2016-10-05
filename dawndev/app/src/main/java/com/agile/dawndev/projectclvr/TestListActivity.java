package com.agile.dawndev.projectclvr;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.Models.Test;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TestListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mRef;
    private DatabaseReference mCompanyRef;
    private String mCompanyKey;
    private FirebaseRecyclerAdapter<Test, TestHolder> mRecyclerViewAdapter;
    private TextView mCompanyName;
    private String mCompanyEmail;
    private ImageView mLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Regular.otf");


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                mCompanyKey = extras.getString("id");
            }
        } else {
            mCompanyKey = (String) savedInstanceState.getSerializable("id");
        }

        mLogo = (ImageView) findViewById(R.id.logo);


        mAuth = FirebaseAuth.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_tests);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);
        mRef.child("companies").child(mCompanyKey).child("logo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String URL = dataSnapshot.getValue(String.class);
                Glide.with(TestListActivity.this.getApplicationContext()).load(URL).into(mLogo);
                mLogo.setAlpha(0.5f);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mCompanyName = (TextView) findViewById(R.id.company_name);
        mCompanyName.setTypeface(custom_font);
        mCompanyRef = mRef.child("companies").child(mCompanyKey).child("tests");
        mRef.child("companies").child(mCompanyKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCompanyName.setText(dataSnapshot.child("name").getValue().toString());
                mCompanyEmail = dataSnapshot.child("email").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        attachRecyclerViewAdapter();
    }



    private void attachRecyclerViewAdapter() {

        Query lastFifty = mCompanyRef.limitToLast(50);
        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<Test, TestHolder>(
                Test.class, R.layout.card_test_layout, TestHolder.class, lastFifty) {


            @Override
            public void populateViewHolder(TestHolder testView, final Test test, int position) {

                DatabaseReference ref = getRef(position);
                final String itemKey = ref.getKey();


                testView.setName(itemKey, mCompanyKey);
                testView.setNumberOfQuestions(itemKey, mCompanyKey);

                testView.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), TestInformationActivity.class);
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra("companyName", mCompanyName.getText());
                        intent.putExtra("companyKey", mCompanyKey);
                        intent.putExtra("companyEmail", mCompanyEmail);
                        intent.putExtra("testKey", itemKey);
                        intent.setType("text/plain");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        };

        mRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mLayoutManager.smoothScrollToPosition(mRecyclerView, null, mRecyclerViewAdapter.getItemCount());
            }
        });

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

    }



    public static class TestHolder extends RecyclerView.ViewHolder {
        View mView;
        DatabaseReference db;


        public TestHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String key, String companyKey) {
            db = FirebaseDatabase.getInstance().getReference();
            db.child("companies").child(companyKey).child("tests").child(key).child("name").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView field = (TextView) mView.findViewById(R.id.test_name);
                    Typeface custom_font = Typeface.createFromAsset(mView.getContext().getAssets(),  "fonts/Montserrat-Regular.otf");
                    field.setText(dataSnapshot.getValue().toString());
                    field.setTypeface(custom_font);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

        }


        public void setNumberOfQuestions(String key, String companyKey) {

            db = FirebaseDatabase.getInstance().getReference();
            db.child("companies").child(companyKey).child("tests").child(key).child("Questions").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView field = (TextView) mView.findViewById(R.id.number_of_questions);
                    Typeface custom_font = Typeface.createFromAsset(mView.getContext().getAssets(),  "fonts/Montserrat-Regular.otf");
                    field.setText(Long.toString(dataSnapshot.getChildrenCount()) + " Questions");
                    field.setTypeface(custom_font);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
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
