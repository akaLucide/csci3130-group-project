package com.example.csci3130groupproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.core.JobMatcher;
import com.example.csci3130groupproject.core.JobSearchFilter;
import com.example.csci3130groupproject.core.LogoutHelper;
import com.example.csci3130groupproject.util.JobDetailsFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private static final String DB_URL = "https://csci3130groupproject-c46e6-default-rtdb.firebaseio.com/";
    private static final int REQUEST_SEARCH = 1;

    private Button btnProfile, btnMapView, btnSearch, btnLogout;
    private LinearLayout layoutPostedJobs;
    private TextView tvPostedJobsTitle;

    private JobSearchFilter activeFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        btnProfile = findViewById(R.id.btnProfile);
        btnMapView = findViewById(R.id.btnMapView);
        btnSearch = findViewById(R.id.btnSearch);
        btnLogout = findViewById(R.id.btnLogout);
        layoutPostedJobs = findViewById(R.id.layoutPostedJobs);
        tvPostedJobsTitle = findViewById(R.id.tvPostedJobsTitle);

        LogoutHelper.setupLogoutButton(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Session expired. Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnMapView.setOnClickListener(v -> {
            // placeholder for map view
        });

        btnSearch.setOnClickListener(v -> {
            Intent searchIntent = new Intent(EmployeeDashboardActivity.this, JobSearchPageActivity.class);
            startActivityForResult(searchIntent, REQUEST_SEARCH);
        });

        loadJobs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadJobs();
    }

    // Receives search filter results from JobSearchPageActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK && data != null) {
            activeFilter = new JobSearchFilter();
            activeFilter.titleQuery = data.getStringExtra("titleQuery");
            activeFilter.minSalary = data.getDoubleExtra("minSalary", 0.0);
            activeFilter.maxSalary = data.getDoubleExtra("maxSalary", Double.MAX_VALUE);
            activeFilter.maxDuration = data.getDoubleExtra("maxDuration", Double.MAX_VALUE);
            activeFilter.vicinityKm = data.getDoubleExtra("vicinityKm", Double.MAX_VALUE);
            loadJobs();
        }
    }

    // Fetches all jobs from Firebase and applies the active filter if one exists
    private void loadJobs() {
        layoutPostedJobs.removeAllViews();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference jobsRef = FirebaseDatabase.getInstance(DB_URL).getReference("jobs");
        final JobMatcher matcher = new JobMatcher();

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layoutPostedJobs.removeAllViews();

                if (!snapshot.exists()) {
                    tvPostedJobsTitle.setText("Results: 0");
                    TextView emptyView = new TextView(EmployeeDashboardActivity.this);
                    emptyView.setText("No jobs available yet.");
                    emptyView.setTextSize(16f);
                    layoutPostedJobs.addView(emptyView);
                    return;
                }

                int count = 0;
                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    String jobId = jobSnap.getKey();

                    // Create a Job object and load all fields from Firebase
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

                    // Filter jobs if a filter is active
                    if (activeFilter != null && !matcher.matches(job, activeFilter)) {
                        continue;
                    }

                    // Send full job object to UI row
                    addJobRow(jobId, job);
                    count++;
                }

                if (activeFilter != null) {
                    tvPostedJobsTitle.setText("Search Results: " + count);
                } else {
                    tvPostedJobsTitle.setText("Newest Posted Jobs (" + count + ")");
                }

                if (count == 0) {
                    TextView emptyView = new TextView(EmployeeDashboardActivity.this);
                    emptyView.setText("No matching jobs found.");
                    emptyView.setTextSize(16f);
                    layoutPostedJobs.addView(emptyView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeDashboardActivity.this,
                        "Failed to load jobs: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Updated method to accept full job object
    private void addJobRow(String jobId, Job job) {
        LinearLayout jobContainer = new LinearLayout(this);
        jobContainer.setOrientation(LinearLayout.VERTICAL);
        jobContainer.setPadding(0, 0, 0, 24);

        // Line 1: Job title
        TextView tvJobTitle = new TextView(this);
        // Format job title using helper formatter
        tvJobTitle.setText(JobDetailsFormatter.dashboardTitle(job));
        tvJobTitle.setTextSize(18f);
        tvJobTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        // Line 2: Category - Date
        String category = (job.category != null && !job.category.trim().isEmpty())
                ? job.category : "No Category";
        String date = (job.date != null && !job.date.trim().isEmpty())
                ? job.date : "No Date";
        TextView tvJobSubtitle = new TextView(this);
        tvJobSubtitle.setText(category + " - " + date);
        tvJobSubtitle.setTextSize(14f);
        tvJobSubtitle.setTextColor(getResources().getColor(android.R.color.darker_gray));
        tvJobSubtitle.setPadding(0, 0, 0, 8);

        LinearLayout buttonRow = new LinearLayout(this);
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);

        Button btnDetails = new Button(this);
        btnDetails.setText("Details");

        Button btnApply = new Button(this);
        btnApply.setText("Apply");

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
        btnApply.setLayoutParams(buttonParams2);

        //Job Details button
        btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, JobDetailsActivity.class);
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

        // Application Review button
        btnApply.setOnClickListener(v -> {
            // Check if user has already applied before navigating to JobSubmissionActivity
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference applicantRef = FirebaseDatabase.getInstance(DB_URL)
                    .getReference("jobs")
                    .child(jobId)
                    .child("applicants")
                    .child(uid);

            applicantRef.get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    Toast.makeText(EmployeeDashboardActivity.this,
                            "You have already applied to this job.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(EmployeeDashboardActivity.this, JobSubmissionActivity.class);
                    intent.putExtra("jobId", jobId);
                    intent.putExtra("jobTitle", JobDetailsFormatter.dashboardTitle(job));
                    startActivity(intent);
                }
            });
        });

        buttonRow.addView(btnDetails);
        buttonRow.addView(btnApply);

        View divider = new View(this);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
        );
        dividerParams.setMargins(0, 16, 0, 0);
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        jobContainer.addView(tvJobTitle);
        jobContainer.addView(tvJobSubtitle);
        jobContainer.addView(buttonRow);
        jobContainer.addView(divider);

        layoutPostedJobs.addView(jobContainer);
    }
}