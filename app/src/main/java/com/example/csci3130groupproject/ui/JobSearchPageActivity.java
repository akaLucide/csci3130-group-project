package com.example.csci3130groupproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;

import java.util.Locale;

/**
 * Activity for searching jobs with various filters such as title, salary, duration, and vicinity.
 * Collects user input and returns filter criteria to the calling activity.
 */
public class JobSearchPageActivity extends AppCompatActivity {

    private AutoCompleteTextView etJobTitle;
    private EditText etMinSalary, etMaxSalary, etMaxDuration, etVicinityKm;
    private Button btnSearch;

    /**
     * Initializes the job search page UI, sets up autocomplete for job titles,
     * and configures the search button to collect filter values.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
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

        btnSearch.setOnClickListener(v -> onSearchClicked());
    }

    /**
     * Collects filter values from the form fields and returns them to the calling activity
     * via an intent, then finishes this activity.
     */
    private void onSearchClicked() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("titleQuery", etJobTitle.getText().toString().trim().toLowerCase(Locale.ROOT));
        resultIntent.putExtra("minSalary", parseDouble(etMinSalary.getText().toString().trim(), 0.0));
        resultIntent.putExtra("maxSalary", parseDouble(etMaxSalary.getText().toString().trim(), Double.MAX_VALUE));
        resultIntent.putExtra("maxDuration", parseDouble(etMaxDuration.getText().toString().trim(), Double.MAX_VALUE));
        resultIntent.putExtra("vicinityKm", parseDouble(etVicinityKm.getText().toString().trim(), Double.MAX_VALUE));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Safely parses a string to a double value, returning a default if the string is empty or invalid.
     *
     * @param s   The string to parse.
     * @param def The default value to return if parsing fails.
     * @return The parsed double value, or the default if parsing fails.
     */
    private double parseDouble(String s, double def) {
        if (TextUtils.isEmpty(s)) return def;
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return def; }
    }
}
