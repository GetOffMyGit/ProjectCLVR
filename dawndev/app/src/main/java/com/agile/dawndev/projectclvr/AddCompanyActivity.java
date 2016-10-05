package com.agile.dawndev.projectclvr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);



        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Regular.otf");


        mEdit = (AutoCompleteTextView) findViewById(R.id.editText);
        mPassword = (EditText) findViewById(R.id.companyPassword);
        mWelcome = (TextView) findViewById(R.id.welcome_message);
        mWelcomeInfo = (TextView) findViewById(R.id.welcome_info);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mWelcome.setTypeface(custom_font);
        mWelcomeInfo.setTypeface(custom_font);
        mPassword.setTypeface(custom_font);
        mEdit.setTypeface(custom_font);
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        mDatabase.child("companies").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot companySnapshot : dataSnapshot.getChildren()){
                            //Get the suggestion by childing the key of the string you want to get.
                            String company = companySnapshot.child("name").getValue(String.class);
                            //Add the retrieved string to the list
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

    public void assignCompany(View view) {
        if (mEdit.getText().toString().length() > 0 && mPassword.getText().toString().length() > 0) {
            mDatabase.child("company_names").child(mEdit.getText().toString()).child("password").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.getValue().equals(mPassword.getText().toString())) {
                                    addCompany();
                                } else {
                                    Context context = getApplicationContext();
                                    CharSequence text = "The password you entered is incorrect";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            }
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
        else {
            Context context = getApplicationContext();
            CharSequence text = "Please enter all the required fields!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }

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
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

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
