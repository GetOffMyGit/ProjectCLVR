package com.agile.dawndev.projectclvr.ListViews;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agile.dawndev.projectclvr.UserRelations.AddCompanyActivity;
import com.agile.dawndev.projectclvr.Auth.LoginActivity;
import com.agile.dawndev.projectclvr.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;


/*
    Activity that shows the list of companies available for a user
 */
public class CompanyListActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener
{
    private HashMap<String,TextView> textViews = new HashMap<String,TextView>();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private DatabaseReference mRef;
    private DatabaseReference mCompanyRef;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Boolean, CompanyHolder> mRecyclerViewAdapter;

    private ProgressBar mProgressBar;

    private TextView mWelcome;
    private TextView mName;
    private TextView mSignOut;

    private GoogleApiClient mGoogleApiClient;

    private static final int PERMISSION_ALL = 1;
    private FloatingActionButton mFab;
    private static final String[] PERMISSIONS = {android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final String TAG = "CompanyListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tests2);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Regular.otf");

        // Check appropriate permissions
        if (!hasPermissions(this, PERMISSIONS)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[1]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[2])) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSignOut = (TextView) findViewById(R.id.sign_out);
        mName = (TextView) findViewById(R.id.name);
        mWelcome = (TextView) findViewById(R.id.welcome);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);
        mCompanyRef = mRef.child("users").child(mAuth.getCurrentUser().getUid()).child("companies");

        // Setting custom fonts
        mWelcome.setTypeface(custom_font);
        mSignOut.setTypeface(custom_font);
        mName.setTypeface(custom_font);

        // Set the name of the user
        mName.setText(mAuth.getCurrentUser().getDisplayName().toString());

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyListActivity.this, AddCompanyActivity.class);
                startActivity(intent);
            }
        });


        attachRecyclerViewAdapter();


        // Listener for the signout button
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CompanyListActivity.this)
                        .setTitle("Sign Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                signout();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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

                // set the variables within each of the cards
                companyView.setName(itemKey);
                companyView.setAvailable(itemKey);
                companyView.setImage(itemKey);


                // clicking a card takes you to the activity containing the list of tests relevant to that company
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

    // Method that is called to sign out of google and firebase
    private void signout() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();
                if(mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d(TAG, "User Logged out");
                                Intent intent = new Intent(CompanyListActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "Google API Client Connection Suspended");
            }
        });
    }


    // Class for each card
    public static class CompanyHolder extends RecyclerView.ViewHolder {
        View mView;
        DatabaseReference db;
        String name;


        public CompanyHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        // Sets the name of the company in the card
        public void setName(String key) {
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

        // sets the company logo
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



        // finds the number of tests available and displays this
        public void setAvailable(String key) {

            db = FirebaseDatabase.getInstance().getReference();
            db.child("companies").child(key).child("tests").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView field = (TextView) mView.findViewById(R.id.number_of_tests);
                    Typeface custom_font = Typeface.createFromAsset(mView.getContext().getAssets(),  "fonts/Montserrat-Regular.otf");
                    field.setTypeface(custom_font);
                    field.setText(Long.toString(dataSnapshot.getChildrenCount()) + " Tests Available");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();



        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

}