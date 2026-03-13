package com.example.csci3130groupproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.core.JobSearchFilter;
import com.example.csci3130groupproject.data.FirebaseCRUD;

import java.util.List;
import java.util.Locale;

public class JobSearchPageActivity extends AppCompatActivity {

    private TextView tvResults, tvResultsList;
    private AutoCompleteTextView etJobTitle;
    private EditText etMinSalary, etMaxSalary, etMaxDuration, etVicinityKm;
    private Button btnSearch;
    private FirebaseCRUD crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search_page);

        etJobTitle = findViewById(R.id.etJobTitle);

        // Set up autocomplete suggestions from job categories
        String[] categories = getResources().getStringArray(R.array.job_categories);
        ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        etJobTitle.setAdapter(autoAdapter);

        etMinSalary = findViewById(R.id.etMinSalary);
        etMaxSalary = findViewById(R.id.etMaxSalary);
        etMaxDuration = findViewById(R.id.etMaxDuration);
        etVicinityKm = findViewById(R.id.etVicinityKm);

        btnSearch = findViewById(R.id.btnSearch);
        tvResults = findViewById(R.id.tvResults);
        tvResultsList = findViewById(R.id.tvResultsList);

        crud = new FirebaseCRUD();

        btnSearch.setOnClickListener(v -> onSearchClicked());
    }

    private void onSearchClicked() {
        JobSearchFilter filter = new JobSearchFilter();
        filter.titleQuery = etJobTitle.getText().toString().trim().toLowerCase(Locale.ROOT);
        filter.minSalary = parseDouble(etMinSalary.getText().toString().trim(), 0.0);
        filter.maxSalary = parseDouble(etMaxSalary.getText().toString().trim(), Double.MAX_VALUE);
        filter.maxDuration = parseDouble(etMaxDuration.getText().toString().trim(), Double.MAX_VALUE);
        filter.vicinityKm = parseDouble(etVicinityKm.getText().toString().trim(), Double.MAX_VALUE);

        crud.searchJobs(filter, new FirebaseCRUD.JobsCallback() {
            @Override
            public void onSuccess(List<Job> jobs) {
                showJobs(jobs);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(JobSearchPageActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showJobs(List<Job> jobs) {
        tvResults.setText("Results: " + jobs.size());

        if (jobs.isEmpty()) {
            tvResultsList.setText("No matching jobs found.");
            Toast.makeText(this, "No matching jobs found.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jobs.size(); i++) {
            sb.append(i + 1).append(") ")
                    .append(buildJobLine(jobs.get(i)))
                    .append("\n\n");
        }
        tvResultsList.setText(sb.toString());
        Toast.makeText(this, "Found " + jobs.size() + " job(s).", Toast.LENGTH_SHORT).show();
    }

    private String buildJobLine(Job job) {
        String title = (!TextUtils.isEmpty(job.title)) ? job.title : "(No title)";
        String category = (job.category != null) ? job.category : "(No category)";

        return "Title: " + title
                + "\nCategory: " + category
                + "\nSalary/hr: $" + job.salaryPerHour
                + "\nDuration: " + job.expectedDurationHours + " hours";
    }

    private double parseDouble(String s, double def) {
        if (TextUtils.isEmpty(s)) return def;
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return def; }
    }
}
