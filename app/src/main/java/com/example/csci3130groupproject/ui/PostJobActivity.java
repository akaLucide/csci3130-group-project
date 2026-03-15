package com.example.csci3130groupproject.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.data.FirebaseCRUD;
import com.example.csci3130groupproject.util.JobCategoryUtil;
import com.example.csci3130groupproject.util.JobValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostJobActivity extends AppCompatActivity {

    private Spinner spUrgency;
    private Button btnPickDate, btnPostJob, btnBackToDashboard;
    private TextView tvSelectedDate;
    private int selectedYear, selectedMonth, selectedDay;
    private EditText etJobTitle, etJobCategory, etJobDescription, etJobLocation, etDurationHours, etSalary;
    private DatabaseReference jobsRef;
    private DatabaseReference categoriesRef;
    private FirebaseAuth auth;
    private FirebaseCRUD crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        auth = FirebaseAuth.getInstance();
        jobsRef = FirebaseDatabase.getInstance().getReference("jobs");
        categoriesRef = FirebaseDatabase.getInstance().getReference("jobCategories");
        crud = new FirebaseCRUD();

        etJobTitle = findViewById(R.id.etJobTitle);
        etJobCategory = findViewById(R.id.etJobCategory);
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
        String title = etJobTitle.getText() != null ? etJobTitle.getText().toString().trim() : "";
        String typedCategory = etJobCategory.getText() != null ? etJobCategory.getText().toString().trim() : "";
        String urgency = spUrgency.getSelectedItem() != null ? spUrgency.getSelectedItem().toString() : "";
        String date = tvSelectedDate.getText() != null ? tvSelectedDate.getText().toString() : "";
        String desc = etJobDescription.getText() != null ? etJobDescription.getText().toString().trim() : "";
        String locationAddress = etJobLocation.getText() != null ? etJobLocation.getText().toString().trim() : "";
        String durationHours = etDurationHours.getText() != null ? etDurationHours.getText().toString().trim() : "";
        String salary = etSalary.getText() != null ? etSalary.getText().toString().trim() : "";

        String categoryValidation = JobValidator.validateCategory(typedCategory);
        if (!categoryValidation.equals("OK")) {
            etJobCategory.setError(categoryValidation);
            Toast.makeText(this, categoryValidation, Toast.LENGTH_SHORT).show();
            return;
        }

        String validationResult = JobValidator.validate(desc, date);
        if (!validationResult.equals("OK")) {
            if (validationResult.equals("Description required")) {
                etJobDescription.setError(validationResult);
            }
            Toast.makeText(this, validationResult, Toast.LENGTH_SHORT).show();
            return;
        }

        if (locationAddress.isEmpty()) {
            etJobLocation.setError("Location address required");
            Toast.makeText(this, "Location address required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (durationHours.isEmpty()) {
            etDurationHours.setError("Duration required");
            Toast.makeText(this, "Duration required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (salary.isEmpty()) {
            etSalary.setError("Salary required");
            Toast.makeText(this, "Salary required", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = (auth.getCurrentUser() != null) ? auth.getCurrentUser().getUid() : null;
        if (uid == null) {
            Toast.makeText(this, "Not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String normalizedCategory = JobCategoryUtil.normalizeKey(typedCategory);

        categoriesRef.child(normalizedCategory).get()
                .addOnSuccessListener(snapshot -> saveJobWithResolvedCategory(
                        snapshot,
                        typedCategory,
                        title,
                        urgency,
                        date,
                        desc,
                        locationAddress,
                        durationHours,
                        salary,
                        uid,
                        normalizedCategory
                ))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to check category: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void saveJobWithResolvedCategory(
            DataSnapshot snapshot,
            String typedCategory,
            String title,
            String urgency,
            String date,
            String desc,
            String locationAddress,
            String durationHours,
            String salary,
            String uid,
            String normalizedCategory
    ) {
        String storedCategory = snapshot.child("name").getValue(String.class);
        String resolvedCategory = JobCategoryUtil.resolveStoredOrNewDisplayName(storedCategory, typedCategory);

        if (!snapshot.exists()) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("name", resolvedCategory);
            categoryMap.put("normalizedKey", normalizedCategory);
            categoryMap.put("createdAt", System.currentTimeMillis());
            categoriesRef.child(normalizedCategory).setValue(categoryMap);
        }

        Map<String, Object> job = new HashMap<>();
        job.put("title", title);
        job.put("category", resolvedCategory);
        job.put("urgency", urgency);
        job.put("date", date);
        job.put("description", desc);
        job.put("locationAddress", locationAddress);
        job.put("expectedDurationHours", Double.parseDouble(durationHours));
        job.put("salaryPerHour", Double.parseDouble(salary));
        job.put("employerId", uid);
        job.put("createdAt", System.currentTimeMillis());

        String key = jobsRef.push().getKey();
        if (key == null) {
            Toast.makeText(this, "Failed to generate job id", Toast.LENGTH_SHORT).show();
            return;
        }

        jobsRef.child(key).setValue(job)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Job posted!", Toast.LENGTH_SHORT).show();
                    etJobTitle.setText("");
                    etJobCategory.setText("");
                    etJobDescription.setText("");
                    etJobLocation.setText("");
                    etDurationHours.setText("");
                    etSalary.setText("");
                    tvSelectedDate.setText("No date selected");
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Post failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}