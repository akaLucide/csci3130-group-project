package com.example.csci3130groupproject.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
                    // Show the selected file name
                    String fileName = uri.getLastPathSegment();
                    tvFileName.setText(fileName != null ? fileName : "File selected");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_submission);

        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvFileName = findViewById(R.id.tvFileName);
        btnPickResume = findViewById(R.id.btnPickResume);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Get job info passed from EmployeeDashboardActivity
        jobId = getIntent().getStringExtra("jobId");
        jobTitle = getIntent().getStringExtra("jobTitle");

        if (jobId == null) {
            Toast.makeText(this, "Error: Job not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvJobTitle.setText(jobTitle != null ? jobTitle : "Job Application");

        btnPickResume.setOnClickListener(v -> filePickerLauncher.launch("application/pdf"));

        btnSubmit.setOnClickListener(v -> submitApplication());
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
        String fileName = uid + "_resume.pdf";

        // Upload resume to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference("resumes/" + jobId + "/" + fileName);

        storageRef.putFile(resumeUri)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            // Save applicant entry under the job in Realtime Database
                            saveApplicantToDatabase(uid, downloadUri.toString());
                        })
                )
                .addOnFailureListener(e -> {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("Submit Application");
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void saveApplicantToDatabase(String uid, String resumeUrl) {
        DatabaseReference applicantRef = FirebaseDatabase.getInstance(DB_URL)
                .getReference("jobs")
                .child(jobId)
                .child("applicants")
                .child(uid);

        Map<String, String> applicantData = new HashMap<>();
        applicantData.put("userId", uid);
        applicantData.put("resumeUrl", resumeUrl);
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
    }
}