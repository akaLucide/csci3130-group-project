package com.example.csci3130groupproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.data.FirebaseCRUD;
import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.core.JobSearchFilter;
import com.example.csci3130groupproject.core.LogoutHelper;
import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.util.JobDetailsFormatter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;

public class EmployeeDashboardActivity extends AppCompatActivity {

    //UI element
    private TextView tvRole, tvResults, tvResultsList;
    private EditText etJobTitle, etMinSalary, etMaxSalary, etMaxDuration, etVicinityKm;
    private Button btnSearch, btnLogout;
    private FirebaseAuth auth;
    private FirebaseCRUD crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        Button btnProfile = findViewById(R.id.btnProfile);

        // enable logout button
        LogoutHelper.setupLogoutButton(this);

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        //setup UI element
        tvRole = findViewById(R.id.tvRole);
        etJobTitle = findViewById(R.id.etJobTitle);
        etMinSalary = findViewById(R.id.etMinSalary);
        etMaxSalary = findViewById(R.id.etMaxSalary);
        etMaxDuration = findViewById(R.id.etMaxDuration);
        etVicinityKm = findViewById(R.id.etVicinityKm);

        btnSearch = findViewById(R.id.btnSearch);
        btnLogout = findViewById(R.id.btnLogout);

        tvResults = findViewById(R.id.tvResults);
        tvResultsList = findViewById(R.id.tvResultsList);

        //init Firebase
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Session expired. Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        crud = new FirebaseCRUD();

        //search button
        btnSearch.setOnClickListener(v -> onSearchClicked());

        // Wire up logout button
        LogoutHelper.setupLogoutButton(this);
    }

    private void onSearchClicked() {
        // Read inputs into a filter object
        JobSearchFilter filter = new JobSearchFilter();
        filter.titleQuery = etJobTitle.getText().toString().trim().toLowerCase(Locale.ROOT);
        filter.minSalary = parseDouble(etMinSalary.getText().toString().trim(), 0.0);
        filter.maxSalary = parseDouble(etMaxSalary.getText().toString().trim(), Double.MAX_VALUE);
        filter.maxDuration = parseDouble(etMaxDuration.getText().toString().trim(), Double.MAX_VALUE);
        filter.vicinityKm = parseDouble(etVicinityKm.getText().toString().trim(), Double.MAX_VALUE);

        // Call data layer
        crud.searchJobs(filter, new FirebaseCRUD.JobsCallback() {
            @Override
            public void onSuccess(List<Job> jobs) {
                showJobs(jobs);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EmployeeDashboardActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showJobs(List<Job> jobs) {
        tvResults.setText(JobDetailsFormatter.resultsCountText(jobs.size()));
        tvResultsList.setText(JobDetailsFormatter.formatList(jobs));

        if (jobs.isEmpty()) {
            Toast.makeText(this, "No matching jobs found.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Found " + jobs.size() + " job(s).", Toast.LENGTH_SHORT).show();
        }
    }

    private double parseDouble(String s, double def) {
        if (TextUtils.isEmpty(s)) {
            return def;
        }
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return def;
        }
    }
}