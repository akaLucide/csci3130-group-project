package com.example.csci3130groupproject.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;

public class ApplicationReviewActivity extends AppCompatActivity {

    String jobRef;
    Button backbtn, favbtn;
    TextView numApps;
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
        numApps = findViewById(R.id.numApplicantsTextView);
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

    }
    private void addApplicantRow(String details){

    }
}
