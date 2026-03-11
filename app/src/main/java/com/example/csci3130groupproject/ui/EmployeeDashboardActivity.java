package com.example.csci3130groupproject.ui;

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
import com.example.csci3130groupproject.core.LogoutHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private static final String DB_URL = "https://csci3130groupproject-c46e6-default-rtdb.firebaseio.com/";

    private Button btnProfile, btnMapView, btnSearch, btnLogout;
    private LinearLayout layoutPostedJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        btnProfile = findViewById(R.id.btnProfile);
        btnMapView = findViewById(R.id.btnMapView);
        btnSearch = findViewById(R.id.btnSearch);
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
            Intent intent = new Intent(EmployeeDashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnMapView.setOnClickListener(v -> {
            // placeholder for map view
        });

        btnSearch.setOnClickListener(v -> {
            // placeholder for search
        });

        loadAllJobs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllJobs();
    }

    private void loadAllJobs() {
        layoutPostedJobs.removeAllViews();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference jobsRef = FirebaseDatabase.getInstance(DB_URL)
                .getReference("jobs");

        // Load ALL jobs — no filter by employerId
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layoutPostedJobs.removeAllViews();

                if (!snapshot.exists()) {
                    TextView emptyView = new TextView(EmployeeDashboardActivity.this);
                    emptyView.setText("No jobs available yet.");
                    emptyView.setTextSize(16f);
                    layoutPostedJobs.addView(emptyView);
                    return;
                }

                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    String jobId = jobSnap.getKey();
                    String category = jobSnap.child("category").getValue(String.class);
                    String date = jobSnap.child("date").getValue(String.class);

                    if (category == null || category.trim().isEmpty()) {
                        category = "Untitled Job";
                    }
                    if (date == null || date.trim().isEmpty()) {
                        date = "No Date";
                    }

                    String title = category + " - " + date;
                    addJobRow(jobId, title);
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

    private void addJobRow(String jobId, String jobTitle) {
        LinearLayout jobContainer = new LinearLayout(this);
        jobContainer.setOrientation(LinearLayout.VERTICAL);
        jobContainer.setPadding(0, 0, 0, 24);

        TextView tvJobTitle = new TextView(this);
        tvJobTitle.setText(jobTitle);
        tvJobTitle.setTextSize(18f);
        tvJobTitle.setPadding(0, 0, 0, 8);

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

        btnDetails.setOnClickListener(v -> {
            // placeholder for job details
        });

        btnApply.setOnClickListener(v -> {
            // placeholder for job application
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
        jobContainer.addView(buttonRow);
        jobContainer.addView(divider);

        layoutPostedJobs.addView(jobContainer);
    }
}