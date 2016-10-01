package com.agile.dawndev.projectclvr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.Models.UsersCompany;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShowTestsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private HashMap<String,TextView> textViews = new HashMap<String,TextView>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mRef;
    private DatabaseReference mCompanyRef;
    private FirebaseRecyclerAdapter<UsersCompany, CompanyHolder> mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tests2);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);
        mCompanyRef = mRef.child("users").child("1").child("companies");

        attachRecyclerViewAdapter();









        //mAuth.getCurrentUser().getUid()
//        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot companies = dataSnapshot.child("companies");
//                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.showTestsLayout);
//                //Loop through companies
//                for(final DataSnapshot companySnapshot : companies.getChildren()) {
//                    TextView companyName = new TextView(ShowTestsActivity.this);
//                    String companyID = companySnapshot.getKey();
//                    companyName.setText(companyID);
//                    linearLayout.addView(companyName);
//                    textViews.put(companyID, companyName);
//                    //Loop through that company for their tests.
//                    for(final DataSnapshot testSnapshot : companySnapshot.getChildren()) {
//                        Button testButton = new Button(ShowTestsActivity.this);
//                        testButton.setText(testSnapshot.getValue().toString());
//
//                        testButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(ShowTestsActivity.this, SpeechAnalyserActivity.class);
//                                intent.setAction(Intent.ACTION_SEND);
//                                intent.putExtra("companyKey", companySnapshot.getKey());
//                                intent.putExtra("testKey", testSnapshot.getKey());
//                                intent.setType("text/plain");
//                                startActivity(intent);
//                            }
//                        });
//                        linearLayout.addView(testButton);
//                    }
//
//                }
//                nameCompanies();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }


    private void attachRecyclerViewAdapter() {

        Query lastFifty = mCompanyRef.limitToLast(50);
        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<UsersCompany, CompanyHolder>(
                UsersCompany.class, R.layout.company_card_layout, CompanyHolder.class, lastFifty) {


            @Override
            public void populateViewHolder(CompanyHolder companyView, final UsersCompany company, int position) {

                DatabaseReference ref = getRef(position);
                final String itemKey = ref.getKey();


                companyView.setName(itemKey);
                companyView.setAvailable(itemKey);

                Log.d("cj", itemKey);
                companyView.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), TestListActivity.class);
                        intent.putExtra("id", itemKey);
                        startActivity(intent);
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

    public static class CompanyHolder extends RecyclerView.ViewHolder {
        View mView;
        DatabaseReference db;


        public CompanyHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String key) {
            Log.d("cjKey", key);
            db = FirebaseDatabase.getInstance().getReference();
            db.child("companies").child(key).child("name").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView field = (TextView) mView.findViewById(R.id.company_name);
                    field.setText(dataSnapshot.getValue().toString());
                    Log.d("cjsetName", dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

        }


        public void setAvailable(String key) {

            db = FirebaseDatabase.getInstance().getReference();
            db.child("companies").child(key).child("tests").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView field = (TextView) mView.findViewById(R.id.number_of_tests);
                    field.setText(Long.toString(dataSnapshot.getChildrenCount()) + " Tests Available");
                    Log.d("cjavailable", Long.toString(dataSnapshot.getChildrenCount()));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
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

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first



        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

}