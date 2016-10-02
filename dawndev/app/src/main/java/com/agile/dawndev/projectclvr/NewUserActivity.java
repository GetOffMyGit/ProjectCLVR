package com.agile.dawndev.projectclvr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewUserActivity extends AppCompatActivity {

    private AutoCompleteTextView mEdit;
    private DatabaseReference mDatabase;
    private DatabaseReference mCompanyRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        mEdit = (AutoCompleteTextView) findViewById(R.id.editText);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
