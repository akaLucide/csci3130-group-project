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

public class JobSearchPageActivity extends AppCompatActivity {

    private AutoCompleteTextView etJobTitle;
    private EditText etMinSalary, etMaxSalary, etMaxDuration, etVicinityKm;
    private Button btnSearch;

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

    private double parseDouble(String s, double def) {
        if (TextUtils.isEmpty(s)) return def;
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return def; }
    }
}
