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


import java.util.Calendar;

public class EmployerDashboardActivity extends AppCompatActivity {

    private Spinner spJobCategory, spUrgency;
    private Button btnPickDate, btnLogout;
    private TextView tvSelectedDate;
    private int selectedYear, selectedMonth, selectedDay;
    private EditText etJobDescription;
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

    android.util.Log.d("RTDB", "postJob() category=" + category + ", urgency=" + urgency + ", date=" + date + ", descLen=" + desc.length());

    if (desc.isEmpty()) {
        etJobDescription.setError("Description required");
        android.widget.Toast.makeText(this, "Description required", android.widget.Toast.LENGTH_SHORT).show();
        return;
    }

    if (date.equals("No date selected")) {
        android.widget.Toast.makeText(this, "Pick a date first", android.widget.Toast.LENGTH_SHORT).show();
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
            })
            .addOnFailureListener(e -> {
                android.util.Log.e("RTDB", "Failed to post job", e);
                android.widget.Toast.makeText(this, "Post failed: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            });
}
}
