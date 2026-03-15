package com.example.csci3130groupproject.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.util.JobDetailsFormatter;

public class JobDetailsActivity extends AppCompatActivity {

    private TextView tvJobDetails;
    private Button btnBackToResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        tvJobDetails = findViewById(R.id.tvJobDetails);
        btnBackToResults = findViewById(R.id.btnBackToResults);

        // Reconstruct Job object from intent extras passed by the dashboard
        Job job = new Job();
        job.title = getIntent().getStringExtra("title");
        job.category = getIntent().getStringExtra("category");
        job.description = getIntent().getStringExtra("description");
        job.locationAddress = getIntent().getStringExtra("locationAddress");
        job.salaryPerHour = getIntent().getDoubleExtra("salaryPerHour", 0.0);
        job.expectedDurationHours = getIntent().getDoubleExtra("expectedDurationHours", 0.0);
        job.urgency = getIntent().getStringExtra("urgency");
        job.date = getIntent().getStringExtra("date");

        // Format and display all job details using the shared formatter
        tvJobDetails.setText(JobDetailsFormatter.format(job));

        btnBackToResults.setOnClickListener(v -> finish());
    }
}