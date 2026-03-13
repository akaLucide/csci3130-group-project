package com.example.csci3130groupproject.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.data.FirebaseCRUD;
import com.example.csci3130groupproject.util.JobValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class PostJobActivity extends AppCompatActivity {

    private Spinner spJobCategory, spUrgency;
    private Button btnPickDate, btnPostJob, btnBackToDashboard;
    private TextView tvSelectedDate;
    private int selectedYear, selectedMonth, selectedDay;
    private EditText etJobTitle, etJobDescription, etJobLocation, etDurationHours, etSalary;
    private DatabaseReference jobsRef;
    private FirebaseAuth auth;
    private FirebaseCRUD crud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        auth = FirebaseAuth.getInstance();
        jobsRef = FirebaseDatabase.getInstance().getReference("jobs");
        crud = new FirebaseCRUD();

        etJobTitle = findViewById(R.id.etJobTitle);
        spJobCategory = findViewById(R.id.spJobCategory);
        spUrgency = findViewById(R.id.spUrgency);
        btnPickDate = findViewById(R.id.btnPickDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        etJobDescription = findViewById(R.id.etJobDescription);
        btnPostJob = findViewById(R.id.btnPostJob);
        etJobLocation = findViewById(R.id.etJobLocation);
        etDurationHours = findViewById(R.id.etDurationHours);
        etSalary = findViewById(R.id.etSalary);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);

        btnPostJob.setOnClickListener(v -> postJob());
        btnBackToDashboard.setOnClickListener(v -> finish());

        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.job_categories,
                android.R.layout.simple_spinner_item
        );
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJobCategory.setAdapter(catAdapter);

        ArrayAdapter<CharSequence> urgAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.urgency_levels,
                android.R.layout.simple_spinner_item
        );
        urgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUrgency.setAdapter(urgAdapter);

        Calendar cal = Calendar.getInstance();
        selectedYear = cal.get(Calendar.YEAR);
        selectedMonth = cal.get(Calendar.MONTH);
        selectedDay = cal.get(Calendar.DAY_OF_MONTH);
        tvSelectedDate.setText("No date selected");

        btnPickDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    PostJobActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = dayOfMonth;

                        String dateStr = String.format("%04d-%02d-%02d",
                                selectedYear, (selectedMonth + 1), selectedDay);
                        tvSelectedDate.setText(dateStr);
                    },
                    selectedYear, selectedMonth, selectedDay
            );
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });
    }

    private void postJob() {
        android.widget.Toast.makeText(this, "Post Job clicked", android.widget.Toast.LENGTH_SHORT).show();

        String title    = etJobTitle.getText() != null ? etJobTitle.getText().toString().trim() : "";
        String category = spJobCategory.getSelectedItem() != null ? spJobCategory.getSelectedItem().toString() : "";
        String urgency  = spUrgency.getSelectedItem() != null ? spUrgency.getSelectedItem().toString() : "";
        String date     = tvSelectedDate.getText() != null ? tvSelectedDate.getText().toString() : "";
        String desc     = etJobDescription.getText() != null ? etJobDescription.getText().toString().trim() : "";
        String locationAddress = etJobLocation.getText() != null ? etJobLocation.getText().toString().trim() : "";
        String durationHours = etDurationHours.getText() != null ? etDurationHours.getText().toString().trim() : "";
        String salary = etSalary.getText() != null ? etSalary.getText().toString().trim() : "";

        String validationResult = JobValidator.validate(desc, date);
        if (!validationResult.equals("OK")) {
            if (validationResult.equals("Description required")) {
                etJobDescription.setError(validationResult);
            }
            android.widget.Toast.makeText(this, validationResult, android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        if (locationAddress.isEmpty()) {
            etJobLocation.setError("Location address required");
            android.widget.Toast.makeText(this, "Location address required", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        if (durationHours.isEmpty()) {
            etDurationHours.setError("Duration required");
            android.widget.Toast.makeText(this, "Duration required", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        if (salary.isEmpty()) {
            etSalary.setError("Salary required");
            android.widget.Toast.makeText(this, "Salary required", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = (auth.getCurrentUser() != null) ? auth.getCurrentUser().getUid() : null;
        if (uid == null) {
            android.widget.Toast.makeText(this, "Not logged in. Please login again.", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        double salaryPerHour = 0.0;
        try { salaryPerHour = Double.parseDouble(salary); } catch (NumberFormatException ignored) {}
        double expectedDurationHours = 0.0;
        try { expectedDurationHours = Double.parseDouble(durationHours); } catch (NumberFormatException ignored) {}

        java.util.Map<String, Object> job = new java.util.HashMap<>();
        job.put("title", title);
        job.put("category", category);
        job.put("urgency", urgency);
        job.put("date", date);
        job.put("description", desc);
        job.put("locationAddress", locationAddress);
        job.put("expectedDurationHours", expectedDurationHours);
        job.put("salaryPerHour", salaryPerHour);
        job.put("employerId", uid);
        job.put("createdAt", System.currentTimeMillis());

        String key = jobsRef.push().getKey();
        if (key == null) {
            android.widget.Toast.makeText(this, "Failed to generate job id", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        jobsRef.child(key).setValue(job)
                .addOnSuccessListener(unused -> {
                    android.widget.Toast.makeText(this, "Job posted!", android.widget.Toast.LENGTH_SHORT).show();
                    etJobTitle.setText("");
                    etJobDescription.setText("");
                    etJobLocation.setText("");
                    etDurationHours.setText("");
                    etSalary.setText("");
                    tvSelectedDate.setText("No date selected");
                    finish();
                })
                .addOnFailureListener(e ->
                        android.widget.Toast.makeText(this, "Post failed: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show()
                );
    }
}