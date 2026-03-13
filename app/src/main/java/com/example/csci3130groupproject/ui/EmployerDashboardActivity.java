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
import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.core.LogoutHelper;
import com.example.csci3130groupproject.util.JobDetailsFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployerDashboardActivity extends AppCompatActivity {

    private static final String DB_URL = "https://csci3130groupproject-c46e6-default-rtdb.firebaseio.com/";

    private Button btnProfile, btnPostJob, btnLogout;
    private LinearLayout layoutPostedJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_dashboard);

        btnProfile = findViewById(R.id.btnProfile);
        btnPostJob = findViewById(R.id.btnPostJob);
        btnLogout = findViewById(R.id.btnLogout);
        layoutPostedJobs = findViewById(R.id.layoutPostedJobs);

        LogoutHelper.setupLogoutButton(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Session expired. Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerDashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnPostJob.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerDashboardActivity.this, PostJobActivity.class);
            startActivity(intent);
        });

        loadEmployerJobs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEmployerJobs();
    }

    private void loadEmployerJobs() {
        layoutPostedJobs.removeAllViews();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();

        DatabaseReference jobsRef = FirebaseDatabase.getInstance(DB_URL)
                .getReference("jobs");

        jobsRef.orderByChild("employerId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        layoutPostedJobs.removeAllViews();

                        if (!snapshot.exists()) {
                            TextView emptyView = new TextView(EmployerDashboardActivity.this);
                            emptyView.setText("No jobs posted yet.");
                            emptyView.setTextSize(16f);
                            layoutPostedJobs.addView(emptyView);
                            return;
                        }

                        for (DataSnapshot jobSnap : snapshot.getChildren()) {
                            // Load full job data instead of only category and date
                            Job job = new Job();
                            job.title = jobSnap.child("title").getValue(String.class);
                            job.category = jobSnap.child("category").getValue(String.class);
                            job.description = jobSnap.child("description").getValue(String.class);
                            job.locationAddress = jobSnap.child("locationAddress").getValue(String.class);
                            job.urgency = jobSnap.child("urgency").getValue(String.class);
                            job.date = jobSnap.child("date").getValue(String.class);

                            Double salary = jobSnap.child("salaryPerHour").getValue(Double.class);
                            job.salaryPerHour = salary != null ? salary : 0.0;

                            Double duration = jobSnap.child("expectedDurationHours").getValue(Double.class);
                            job.expectedDurationHours = duration != null ? duration : 0.0;

                            if (job.category == null || job.category.trim().isEmpty()) {
                                job.category = "Untitled Job";
                            }
                            if (job.date == null || job.date.trim().isEmpty()) {
                                job.date = "No Date";
                            }

                            // Pass full Job object to the row
                            addJobRow(job);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EmployerDashboardActivity.this,
                                "Failed to load jobs: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method now accepts Job instead of only job title
    private void addJobRow(Job job) {
        LinearLayout jobContainer = new LinearLayout(this);
        jobContainer.setOrientation(LinearLayout.VERTICAL);
        jobContainer.setPadding(0, 0, 0, 24);

        TextView tvJobTitle = new TextView(this);
        // Use formatter for dashboard title
        tvJobTitle.setText(JobDetailsFormatter.dashboardTitle(job));
        tvJobTitle.setTextSize(18f);
        tvJobTitle.setPadding(0, 0, 0, 8);

        LinearLayout buttonRow = new LinearLayout(this);
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);

        Button btnDetails = new Button(this);
        btnDetails.setText("Details");

        Button btnApplicants = new Button(this);
        btnApplicants.setText("Applicants");
        // set description to grab label for tests
        btnApplicants.setContentDescription(jobTitle + "-applicants");

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        buttonParams.setMargins(0, 0, 16, 0);

        btnDetails.setLayoutParams(buttonParams);

        LinearLayout.LayoutParams buttonParams2 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        btnApplicants.setLayoutParams(buttonParams2);

        btnDetails.setOnClickListener(v -> {
            //open job details page and send full job info
            Intent intent = new Intent(EmployerDashboardActivity.this, JobDetailsActivity.class);
            intent.putExtra("title", job.title);
            intent.putExtra("category", job.category);
            intent.putExtra("description", job.description);
            intent.putExtra("locationAddress", job.locationAddress);
            intent.putExtra("salaryPerHour", job.salaryPerHour);
            intent.putExtra("expectedDurationHours", job.expectedDurationHours);
            intent.putExtra("urgency", job.urgency);
            intent.putExtra("date", job.date);
            startActivity(intent);
        });

        btnApplicants.setOnClickListener(v -> {
            // open application review
            Intent intent = new Intent(EmployerDashboardActivity.this, ApplicationReviewActivity.class);
            intent.putExtra("jobRef", jobRef);
            startActivity(intent);
        });

        buttonRow.addView(btnDetails);
        buttonRow.addView(btnApplicants);

        View divider = new View(this);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
        );
        dividerParams.setMargins(0, 16, 0, 0);
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        jobContainer.addView(tvJobTitle);
        jobContainer.addView(buttonRow);
        jobContainer.addView(divider);

        layoutPostedJobs.addView(jobContainer);
    }
}