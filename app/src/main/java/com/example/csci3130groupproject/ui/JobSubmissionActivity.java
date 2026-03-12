package com.example.csci3130groupproject.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JobSubmissionActivity extends AppCompatActivity {

    private static final String DB_URL = "https://csci3130groupproject-c46e6-default-rtdb.firebaseio.com/";

    private TextView tvJobTitle, tvFileName;
    private Button btnPickResume, btnSubmit;

    private Uri resumeUri = null;
    private String jobId;
    private String jobTitle;

    private final ActivityResultLauncher<String> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    resumeUri = uri;
                    String fileName = uri.getLastPathSegment();
                    tvFileName.setText(fileName != null ? fileName : "File selected");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_submission);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Apply for Job");
        }

        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvFileName = findViewById(R.id.tvFileName);
        btnPickResume = findViewById(R.id.btnPickResume);
        btnSubmit = findViewById(R.id.btnSubmit);

        jobId = getIntent().getStringExtra("jobId");
        jobTitle = getIntent().getStringExtra("jobTitle");

        if (jobId == null) {
            Toast.makeText(this, "Error: Job not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvJobTitle.setText(jobTitle != null ? jobTitle : "Job Application");

        btnPickResume.setOnClickListener(v -> filePickerLauncher.launch("application/pdf"));

        btnSubmit.setOnClickListener(v -> submitApplication());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void submitApplication() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Session expired. Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        if (resumeUri == null) {
            Toast.makeText(this, "Please select a resume PDF first.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSubmit.setEnabled(false);
        btnSubmit.setText("Submitting...");

        String uid = currentUser.getUid();

        // RESUME UPLOAD - TEMPORARY SOLUTION USING BASE64 STRING ENCODING

        // Run encoding on a background thread so it doesn't freeze the UI
        new Thread(() -> {
            try {
                // Read PDF bytes and encode to Base64 string
                InputStream inputStream = getContentResolver().openInputStream(resumeUri);
                byte[] bytes = inputStream.readAllBytes();
                inputStream.close();

                String base64Resume = Base64.encodeToString(bytes, Base64.DEFAULT);

                // Save to Firebase on main thread
                runOnUiThread(() -> saveApplicantToDatabase(uid, base64Resume));

            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("Submit Application");
                    Toast.makeText(this, "Could not read file: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void saveApplicantToDatabase(String uid, String base64Resume) {
        // Fetch the user's profile code, then save everything together
        DatabaseReference userRef = FirebaseDatabase.getInstance(DB_URL)
                .getReference("users")
                .child(uid);

        userRef.get().addOnSuccessListener(userSnap -> {
            String email = userSnap.child("email").getValue(String.class);
            String name = userSnap.child("name").getValue(String.class);
            String role = userSnap.child("role").getValue(String.class);

            DatabaseReference applicantRef = FirebaseDatabase.getInstance(DB_URL)
                    .getReference("jobs")
                    .child(jobId)
                    .child("applicants")
                    .child(uid);

            Map<String, String> applicantData = new HashMap<>();
            applicantData.put("email", email != null ? email : "");
            applicantData.put("name", name != null ? name : "");
            applicantData.put("role", role != null ? role : "");
            applicantData.put("resume", base64Resume);
            applicantData.put("appliedAt", String.valueOf(System.currentTimeMillis()));

            applicantRef.setValue(applicantData)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Application submitted!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        btnSubmit.setEnabled(true);
                        btnSubmit.setText("Submit Application");
                        Toast.makeText(this, "Failed to save application: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        }).addOnFailureListener(e -> {
            btnSubmit.setEnabled(true);
            btnSubmit.setText("Submit Application");
            Toast.makeText(this, "Failed to fetch user profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}