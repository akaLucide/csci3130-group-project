package com.example.csci3130groupproject.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final String DB_URL = "https://csci3130groupproject-c46e6-default-rtdb.firebaseio.com/";

    private TextView tvName, tvEmail, tvRole, tvAppliedJobsTitle;
    private LinearLayout layoutAppliedJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnBack = findViewById(R.id.btnBackToDashboard);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);
        layoutAppliedJobs = findViewById(R.id.layoutAppliedJobs);

        btnBack.setOnClickListener(v -> finish());

        // Hide the applied jobs section by default until role is confirmed
        layoutAppliedJobs = findViewById(R.id.layoutAppliedJobs);
        tvAppliedJobsTitle = findViewById(R.id.tvAppliedJobsTitle);
        layoutAppliedJobs.setVisibility(android.view.View.GONE);
        tvAppliedJobsTitle.setVisibility(android.view.View.GONE);

        loadProfileSummary();
    }

    private void loadProfileSummary() {
        tvName.setText("Name: Loading...");
        tvEmail.setText("Email: Loading...");
        tvRole.setText("Role: Loading...");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            tvName.setText("Name: Not available");
            tvEmail.setText("Email: Not available");
            tvRole.setText("Role: Not available");
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();
        String authEmail = currentUser.getEmail();

        tvEmail.setText("Email: " + (authEmail != null ? authEmail : "Not available"));

        DatabaseReference userRef = FirebaseDatabase.getInstance(DB_URL)
                .getReference("users")
                .child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    tvName.setText("Name: Not available");
                    tvRole.setText("Role: Not available");
                    Toast.makeText(ProfileActivity.this, "Profile not found in database", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = snapshot.child("name").getValue(String.class);
                String role = snapshot.child("role").getValue(String.class);
                String dbEmail = snapshot.child("email").getValue(String.class);

                tvName.setText("Name: " + (name != null && !name.isEmpty() ? name : "Not available"));
                tvRole.setText("Role: " + (role != null && !role.isEmpty() ? role : "Not available"));

                if ((authEmail == null || authEmail.isEmpty()) && dbEmail != null && !dbEmail.isEmpty()) {
                    tvEmail.setText("Email: " + dbEmail);
                }

                // Only show applied jobs section for employees
                if ("employee".equals(role)) {
                    layoutAppliedJobs.setVisibility(android.view.View.VISIBLE);
                    tvAppliedJobsTitle.setVisibility(android.view.View.VISIBLE);
                    loadAppliedJobs();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvName.setText("Name: Not available");
                tvRole.setText("Role: Not available");
                Toast.makeText(ProfileActivity.this, "DB error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Loads a short list of applied jobs for the current user.
     * This method will only be reached if the user in an employee.
     *
     * @method onDataChange:
     * Nested method that checks if user is listed under applicants for each job posting
     * The jobs that they are listed under will appear under this "Applied Jobs" section
     */
    private void loadAppliedJobs() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        // get current user
        String uid = currentUser.getUid();

        // get jobs from db
        DatabaseReference jobsRef = FirebaseDatabase.getInstance(DB_URL).getReference("jobs");

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layoutAppliedJobs.removeAllViews();

                if (!snapshot.exists()) return;

                boolean hasApplied = false;

                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    // Check if current user's UID exists under this job's applicants
                    if (jobSnap.child("applicants").child(uid).exists()) {
                        hasApplied = true;

                        //get category and date from job
                        String category = jobSnap.child("category").getValue(String.class);
                        String date = jobSnap.child("date").getValue(String.class);

                        if (category == null || category.trim().isEmpty()) category = "Untitled Job";
                        if (date == null || date.trim().isEmpty()) date = "No Date";

                        // create new list item for employee's applied jobs list
                        TextView tvJob = new TextView(ProfileActivity.this);
                        tvJob.setText("• " + category + " - " + date);
                        tvJob.setTextSize(15f);
                        tvJob.setPadding(0, 4, 0, 4);
                        layoutAppliedJobs.addView(tvJob);
                    }
                }

                // show no applications yet if they haven't applied
                if (!hasApplied) {
                    TextView tvNone = new TextView(ProfileActivity.this);
                    tvNone.setText("No applications yet.");
                    tvNone.setTextSize(15f);
                    layoutAppliedJobs.addView(tvNone);
                }
            }

            // Error handling for db failure
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load applications: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}