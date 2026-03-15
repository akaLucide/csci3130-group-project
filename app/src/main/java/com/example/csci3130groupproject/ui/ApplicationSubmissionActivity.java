package com.example.csci3130groupproject.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.core.ApplicationRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;

/**
 * Activity that allows an employee to submit a job application.
 * Handles PDF resume selection, Base64 encoding, and delegates
 * Firebase submission logic to {@link ApplicationRepository}.
 */
public class ApplicationSubmissionActivity extends AppCompatActivity {

    private TextView tvJobTitle, tvFileName;
    private Button btnPickResume, btnSubmit, btnBack;

    private Uri resumeUri = null;
    private String jobId;
    private String jobTitle;

    private final ApplicationRepository repository = new ApplicationRepository();

    private final ActivityResultLauncher<String> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    resumeUri = uri;
                    String fileName = uri.getLastPathSegment();
                    tvFileName.setText(fileName != null ? fileName : "File selected");
                }
            });

    /**
     * Initializes the activity, binds UI components, and sets up button listeners.
     * Retrieves the job ID and title passed from the previous activity via intent extras.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_submission);

        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvFileName = findViewById(R.id.tvFileName);
        btnPickResume = findViewById(R.id.btnPickResume);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

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
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Validates user session and resume selection, then reads the PDF file on a background
     * thread, encodes it to Base64, and submits the application via {@link ApplicationRepository}.
     * Disables the submit button during processing to prevent duplicate submissions.
     */
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

        // Run encoding on a background thread so it doesn't freeze the UI
        new Thread(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(resumeUri);
                byte[] bytes = inputStream.readAllBytes();
                inputStream.close();

                // Encode PDF to Base64 via repository
                String base64Resume = repository.encodeResume(bytes);

                // Submit application via repository on main thread
                runOnUiThread(() -> repository.submitApplication(jobId, uid, base64Resume, new ApplicationRepository.SubmissionCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ApplicationSubmissionActivity.this, "Application submitted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        btnSubmit.setEnabled(true);
                        btnSubmit.setText("Submit Application");
                        Toast.makeText(ApplicationSubmissionActivity.this, "Failed: " + message, Toast.LENGTH_LONG).show();
                    }
                }));

            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("Submit Application");
                    Toast.makeText(this, "Could not read file: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}