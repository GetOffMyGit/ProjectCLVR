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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agile.dawndev.projectclvr.Models.UsersCompany;
import com.bumptech.glide.Glide;
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
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mRef;
    private DatabaseReference mCompanyRef;
    private FirebaseRecyclerAdapter<Boolean, CompanyHolder> mRecyclerViewAdapter;
    private static final String TAG = "ShowTestsActivity";
    private ProgressBar mProgressBar;
    private TextView mWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tests2);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Regular.otf");

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
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);
        mCompanyRef = mRef.child("users").child(mAuth.getCurrentUser().getUid()).child("companies");
        mWelcome = (TextView) findViewById(R.id.welcome);
        mWelcome.setTypeface(custom_font);
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

        Log.d(TAG, "STEP 1: loading data");
        Query lastFifty = mCompanyRef.limitToLast(50);
        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<Boolean, CompanyHolder>(
                Boolean.class, R.layout.company_card_layout, CompanyHolder.class, lastFifty) {

            @Override
            public void populateViewHolder(CompanyHolder companyView, final Boolean company, int position) {


                DatabaseReference ref = getRef(position);
                final String itemKey = ref.getKey();
                Log.d(TAG, "STEP 2: company " +  position + " : " +  itemKey);


                companyView.setName(itemKey);
                companyView.setAvailable(itemKey);
                companyView.setImage(itemKey);

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
            // http://stackoverflow.com/a/38986252 from
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mRecyclerViewAdapter.getItemCount();

                int lastVisiblePosition =   mLayoutManager.findLastVisibleItemPosition();

                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mLayoutManager.scrollToPosition(positionStart);
                }
                if (lastVisiblePosition == -1) {
                    // first time load - remove loading
                    mProgressBar.setVisibility(View.GONE);
                }
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
                    Typeface custom_font = Typeface.createFromAsset(mView.getContext().getAssets(),  "fonts/Montserrat-Regular.otf");
                    field.setTypeface(custom_font);
                    field.setText(dataSnapshot.getValue().toString());
                    Log.d(TAG, "STEP 3: " + dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }

        public void setImage(String key) {
            db = FirebaseDatabase.getInstance().getReference();
            db.child("companies").child(key).child("logo").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String URL = dataSnapshot.getValue(String.class);
                    ImageView imageView = (ImageView) mView.findViewById(R.id.company_logo);
                    Glide.with(mView.getContext()).load(URL).into(imageView);

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
                    Typeface custom_font = Typeface.createFromAsset(mView.getContext().getAssets(),  "fonts/Montserrat-Regular.otf");
                    field.setTypeface(custom_font);
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