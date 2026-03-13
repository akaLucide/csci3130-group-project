package com.example.csci3130groupproject.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApplicationReviewActivity extends AppCompatActivity {

    private static final String DB_URL = "https://csci3130groupproject-c46e6-default-rtdb.firebaseio.com/";

    String jobRef;
    Button backbtn, favbtn;
    TextView numAppsTextNotifier;
    Boolean fav;
    LinearLayout layoutApplicants;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_review);

        // setup components
        initComponents();
        buttonSetup();

        // load applicants
        loadApplicants();
    }

    // sets up UI components
    private void initComponents(){
        // retrieve key from job posting based on what job you clicked
        jobRef = getIntent().getStringExtra("jobRef");
        // fav toggle for searching
        fav = false;
        // ui components
        backbtn = findViewById(R.id.backToEmployerDashboardBtn);
        favbtn = findViewById(R.id.favouritesBtn);
        numAppsTextNotifier = findViewById(R.id.numApplicantsTextView);
        // set layout
        layoutApplicants = findViewById(R.id.layoutApplicants);
    }

    // sets up buttons
    private void buttonSetup(){
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(ApplicationReviewActivity.this, EmployerDashboardActivity.class);
            startActivity(intent);
        });
        favbtn.setOnClickListener(v -> {
            // toggles the favourite search and loads the applicants
            fav = !fav;
            loadApplicants();
        });
    }

    private void loadApplicants(){
        layoutApplicants.removeAllViews(); // remove all boxes

        // reference to the job applicants that we are loading applicants for
        DatabaseReference appRef = FirebaseDatabase.getInstance(DB_URL).getReference("jobs/" + jobRef + "/applicants");

        // take snapshot of applicants to load
        appRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layoutApplicants.removeAllViews();

                String appMessage; // screen message that displays number of applicants, if 0 we in following if statement
                if (!snapshot.exists()) {
                    // if no applicants return
                    appMessage = "0 applicants";
                    numAppsTextNotifier.setText(appMessage);
                    return;
                }else{
                    // if applicants count number and display
                    appMessage = snapshot.getChildrenCount() + " applicants";
                    numAppsTextNotifier.setText(appMessage);
                }

                // for each applicant in snapshot
                for (DataSnapshot applicant : snapshot.getChildren()) {
                    // get applicant identity for details
                    String id = applicant.getKey();

                    // if the applicant does not have a field that displays whether it is a favourite, add the field and set it false
                    if(applicant.child("favourite").getValue(Boolean.class) == null){
                        appRef.child(id).child("favourite").setValue(false);
                    }

                    // if the favourite filter is on and the applicant is a favourite then we display (displays all favourited applicants)
                    if(fav && applicant.child("favourite").getValue(Boolean.class)) {

                        // grab details from applicant to display in layout
                        String name = applicant.child("name").getValue(String.class);
                        String email = applicant.child("email").getValue(String.class);
                        String title = name + " - " + email;
                        // call row to be created, pass applicant title and applicant id (to be used in favourite button)
                        addApplicantRow(title, id);

                    // if not favourited display all applicants
                    }else if(!fav){
                        // grab details for loading layout row
                        String name = applicant.child("name").getValue(String.class);
                        String email = applicant.child("email").getValue(String.class);
                        String title = name + " - " + email;
                        addApplicantRow(title, id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ApplicationReviewActivity.this,"Failed to load applicants: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addApplicantRow(String details, String appID){
        LinearLayout jobContainer = new LinearLayout(this);
        jobContainer.setOrientation(LinearLayout.VERTICAL);
        jobContainer.setPadding(0, 0, 0, 24);

        TextView appDetails = new TextView(this);
        appDetails.setText(details);
        appDetails.setTextSize(18f);
        appDetails.setPadding(0, 0, 0, 8);

        LinearLayout buttonRow = new LinearLayout(this);
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);

        Button btnContact = new Button(this);
        btnContact.setText("Contact");

        Button btnFavourite = new Button(this);
        btnFavourite.setText("Favourite");

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        buttonParams.setMargins(0, 0, 16, 0);

        btnContact.setLayoutParams(buttonParams);

        LinearLayout.LayoutParams buttonParams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        btnFavourite.setLayoutParams(buttonParams2);
        btnContact.setOnClickListener(v -> {
            // placeholder, add page/contact logic here if necessary later
        });

        btnFavourite.setOnClickListener(v -> {
            // if favourited get reference to applicant that was clicked
            DatabaseReference appRef = FirebaseDatabase.getInstance(DB_URL).getReference("jobs/" + jobRef + "/applicants/" + appID);
            appRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // set favourite to false if it does not exist
                    Boolean favourited = snapshot.child("favourite").getValue(Boolean.class);
                    if (favourited == null){
                        favourited = false;
                    }

                    // set favourite to opposite of what it is (false -> true, true -> false)
                    favourited = !favourited;
                    appRef.child("favourite").setValue(favourited);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ApplicationReviewActivity.this,"Failed to favourite", Toast.LENGTH_SHORT).show();
                }
            });
        });

        buttonRow.addView(btnContact);
        buttonRow.addView(btnFavourite);

        View divider = new View(this);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
        );
        dividerParams.setMargins(0, 16, 0, 0);
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        jobContainer.addView(appDetails);
        jobContainer.addView(buttonRow);
        jobContainer.addView(divider);

        layoutApplicants.addView(jobContainer);
    }
}
