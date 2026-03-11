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
            fav = !fav;
        });
    }

    private void loadApplicants(){
        layoutApplicants.removeAllViews();

        DatabaseReference appRef = FirebaseDatabase.getInstance(DB_URL).getReference("jobs/" + jobRef + "/applicants");

        appRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layoutApplicants.removeAllViews();

                String appMessage;
                if (!snapshot.exists()) {
                    appMessage = "0 applicants";
                    numAppsTextNotifier.setText(appMessage);
                    return;
                }else{
                    appMessage = snapshot.getChildrenCount() + " applicants";
                    numAppsTextNotifier.setText(appMessage);
                }

                for (DataSnapshot applicant : snapshot.getChildren()) {
                    // add other identifiers to be displayed from applicants

                    // use a check here for favourite clicked to filter the results, if fav and applicant is favourite...

                    String id = applicant.getKey();
                    String name = applicant.child("name").getValue(String.class);
                    String date = applicant.child("date").getValue(String.class);

                    String title = name + " - " + date;
                    addApplicantRow(title, id);
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
            // read snapshot and set favourite to opposite itself
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
