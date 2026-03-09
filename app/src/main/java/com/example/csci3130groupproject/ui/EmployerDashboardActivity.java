package com.example.csci3130groupproject.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.data.FirebaseCRUD;
import com.example.csci3130groupproject.core.LogoutHelper;
import com.example.csci3130groupproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.csci3130groupproject.util.JobValidator;

import java.util.Calendar;

public class EmployerDashboardActivity extends AppCompatActivity {

    private Spinner spJobCategory, spUrgency;
    private Button btnPickDate, btnLogout;
    private TextView tvSelectedDate;
    private int selectedYear, selectedMonth, selectedDay;
    private EditText etJobDescription;
    private EditText etJobLocation;
    private EditText etDurationHours;
    private EditText etSalary;
    private DatabaseReference jobsRef;
    private Button btnPostJob;
    private FirebaseAuth auth;
    private FirebaseCRUD crud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_dashboard);

        // enable logout button
        LogoutHelper.setupLogoutButton(this);

        //Firebase
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            android.widget.Toast.makeText(this, "Session expired. Please log in.", android.widget.Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        jobsRef = FirebaseDatabase.getInstance().getReference("jobs");
        crud = new FirebaseCRUD();

        // UI
        Button btnProfile = findViewById(R.id.btnProfile);
        spJobCategory = findViewById(R.id.spJobCategory);
        spUrgency = findViewById(R.id.spUrgency);
        btnPickDate = findViewById(R.id.btnPickDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        etJobDescription = findViewById(R.id.etJobDescription);
        btnLogout = findViewById(R.id.btnLogout);
        btnPostJob = findViewById(R.id.btnPostJob);
        etJobLocation = findViewById(R.id.etJobLocation);
        etDurationHours = findViewById(R.id.etDurationHours);
        etSalary = findViewById(R.id.etSalary);

        // Listeners AFTER views are initialized
        btnPostJob.setOnClickListener(v -> postJob());

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerDashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Setup Job Category dropdown
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.job_categories,
                android.R.layout.simple_spinner_item
        );
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJobCategory.setAdapter(catAdapter);

        // Setup Urgency dropdown
        ArrayAdapter<CharSequence> urgAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.urgency_levels,
                android.R.layout.simple_spinner_item
        );
        urgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUrgency.setAdapter(urgAdapter);

        // enable Date selection
        // Default to today's date
        Calendar cal = Calendar.getInstance();
        selectedYear = cal.get(Calendar.YEAR);
        selectedMonth = cal.get(Calendar.MONTH);
        selectedDay = cal.get(Calendar.DAY_OF_MONTH);
        // default text
        tvSelectedDate.setText("No date selected");

        btnPickDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    EmployerDashboardActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = dayOfMonth;

                        // Display as YYYY-MM-DD
                        String dateStr = String.format("%04d-%02d-%02d",
                                selectedYear, (selectedMonth + 1), selectedDay);
                        tvSelectedDate.setText(dateStr);
                    },
                    selectedYear, selectedMonth, selectedDay
            );
            // prevent selecting past dates
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            dialog.show();
        });
    }

private void postJob() {
    android.widget.Toast.makeText(this, "Post Job clicked", android.widget.Toast.LENGTH_SHORT).show();

    String category = spJobCategory.getSelectedItem() != null ? spJobCategory.getSelectedItem().toString() : "";
    String urgency  = spUrgency.getSelectedItem() != null ? spUrgency.getSelectedItem().toString() : "";
    String date     = tvSelectedDate.getText() != null ? tvSelectedDate.getText().toString() : "";
    String desc     = etJobDescription.getText() != null ? etJobDescription.getText().toString().trim() : "";
    // read address
    String locationAddress = etJobLocation.getText() != null
            ? etJobLocation.getText().toString().trim()
            : "";
    String durationHours = etDurationHours.getText() != null
            ? etDurationHours.getText().toString().trim()
            : "";

    String salary = etSalary.getText() != null
            ? etSalary.getText().toString().trim()
            : "";
    android.util.Log.d("RTDB", "postJob() category=" + category + ", urgency=" + urgency + ", date=" + date + ", descLen=" + desc.length() + ", address=" + locationAddress+
            ", durationHours=" + durationHours +
            ", salary=" + salary);
    //for JobValidator
    String validationResult = JobValidator.validate(desc, date);
    if (!validationResult.equals("OK")) {
        if (validationResult.equals("Description required")) {
            etJobDescription.setError(validationResult);
        }
        android.widget.Toast.makeText(this, validationResult, android.widget.Toast.LENGTH_SHORT).show();
        return;
    }
    // add: validate address
    if (locationAddress.isEmpty()) {
        etJobLocation.setError("Location address required");
        android.widget.Toast.makeText(this, "Location address required", android.widget.Toast.LENGTH_SHORT).show();
        return;
    }
    //add duration hours
    if (durationHours.isEmpty()) {
        etDurationHours.setError("Duration required");
        android.widget.Toast.makeText(this, "Duration required", android.widget.Toast.LENGTH_SHORT).show();
        return;
    }
    //add salary
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

    java.util.Map<String, Object> job = new java.util.HashMap<>();
    job.put("category", category);
    job.put("urgency", urgency);
    job.put("date", date);
    job.put("description", desc);
    // add: save address to Firebase
    job.put("locationAddress", locationAddress);
    //add save duration and salary to Firebase
    job.put("durationHours", durationHours);
    job.put("salary", salary);
    job.put("employerId", uid);
    job.put("createdAt", System.currentTimeMillis());

    String key = jobsRef.push().getKey();
    if (key == null) {
        android.widget.Toast.makeText(this, "Failed to generate job id", android.widget.Toast.LENGTH_SHORT).show();
        return;
    }

    jobsRef.child(key).setValue(job)
            .addOnSuccessListener(unused -> {
                android.util.Log.d("RTDB", "Job posted key=" + key);
                android.widget.Toast.makeText(this, "Job posted!", android.widget.Toast.LENGTH_SHORT).show();
                etJobDescription.setText("");
                etJobLocation.setText("");
                etDurationHours.setText("");
                etSalary.setText("");
                tvSelectedDate.setText("No date selected");
            })
            .addOnFailureListener(e -> {
                android.util.Log.e("RTDB", "Failed to post job", e);
                android.widget.Toast.makeText(this, "Post failed: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            });
}
}
