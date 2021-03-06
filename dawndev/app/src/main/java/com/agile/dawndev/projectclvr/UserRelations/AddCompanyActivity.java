package com.agile.dawndev.projectclvr.UserRelations;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agile.dawndev.projectclvr.ListViews.CompanyListActivity;
import com.agile.dawndev.projectclvr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCompanyActivity extends AppCompatActivity {

    private AutoCompleteTextView mEdit;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText mPassword;
    private TextView mWelcome;
    private TextView mWelcomeInfo;
    private Button mDoneButton;
    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);



        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Custom fonts for all the text
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Regular.otf");


        mEdit = (AutoCompleteTextView) findViewById(R.id.editText);
        mPassword = (EditText) findViewById(R.id.companyPassword);
        mWelcome = (TextView) findViewById(R.id.welcome_message);
        mWelcomeInfo = (TextView) findViewById(R.id.welcome_info);
        mDoneButton = (Button) findViewById(R.id.done_button);
        mBackButton = (Button) findViewById(R.id.back_button);

        // Getting instances of firebase auth and firebase database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mWelcome.setTypeface(custom_font);
        mWelcomeInfo.setTypeface(custom_font);
        mPassword.setTypeface(custom_font);
        mEdit.setTypeface(custom_font);
        mDoneButton.setTypeface(custom_font);
        mBackButton.setTypeface(custom_font);
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);


        // Adding the list of companies into the autocomplete arrayadapter
        mDatabase.child("companies").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot companySnapshot : dataSnapshot.getChildren()){
                            String company = companySnapshot.child("name").getValue(String.class);
                            autoComplete.add(company);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        mEdit.setAdapter(autoComplete);
    }

    // Called when adding more companies to the user account
    public void assignCompany(View view) {
        // Checks for no input for either text fields
        if (mEdit.getText().toString().length() > 0 && mPassword.getText().toString().length() > 0) {
            // grabs the password from database
            mDatabase.child("company_names").child(mEdit.getText().toString()).child("password").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // verifies password is correct
                                if (dataSnapshot.getValue().equals(mPassword.getText().toString())) {
                                    addCompany();
                                }
                                else {
                                    // if not, then show a quick message
                                    Context context = getApplicationContext();
                                    CharSequence text = "The password you entered is incorrect";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            }
                            // if company name doesn't exist, show a quick message
                            else {
                                Context context = getApplicationContext();
                                CharSequence text = "Please enter a valid company!";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

        }
        // if any input is empty, show a quick message
        else {
            Context context = getApplicationContext();
            CharSequence text = "Please enter all the required fields!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }

    // Assigns the company to the user account in the firebase database
    private void addCompany() {
        String company = mEdit.getText().toString();
        mDatabase.child("company_names").child(company).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long id = dataSnapshot.child("id").getValue(Long.class);
                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("companies").child(id.toString()).setValue(true);
                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("exists").setValue(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        startActivity(new Intent(this, CompanyListActivity.class));
        finish();
    }

    // back button method
    public void goBack(View view) {
        finish();
    }

    @Override
    public void onResume() {

        // Always call the superclass method first
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
