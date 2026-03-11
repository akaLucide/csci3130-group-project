package com.example.csci3130groupproject.ui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;

public class JobSubmissionActivity extends AppCompatActivity {

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

        btnSubmit.setOnClickListener(v -> {
            // TODO: submission logic (Base64 encode + save to Firebase)
            Toast.makeText(this, "Submit clicked - logic coming soon", Toast.LENGTH_SHORT).show();
        });
    }
}