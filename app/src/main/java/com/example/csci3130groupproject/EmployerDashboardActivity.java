package com.example.csci3130groupproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        Button btnProfile = findViewById(R.id.btnProfile);

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerDashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        //Firebase
        auth = FirebaseAuth.getInstance();
        crud = new FirebaseCRUD();
        //ui
        spJobCategory = findViewById(R.id.spJobCategory);
        spUrgency = findViewById(R.id.spUrgency);
        btnPickDate = findViewById(R.id.btnPickDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);

        etJobDescription = findViewById(R.id.etJobDescription);
        btnLogout = findViewById(R.id.btnLogout);

        jobsRef = FirebaseDatabase.getInstance().getReference("jobs");

        btnPostJob = findViewById(R.id.btnPostJob);
        //btnPostJob.setOnClickListener(v -> postJob());

        //logout button
        FirebaseDB database = new FirebaseDB(getResources().getString(R.string.FIREBASE_DB_URL));
        btnLogout.setOnClickListener(v -> LogoutHelper.performLogout(this, database));

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

}
