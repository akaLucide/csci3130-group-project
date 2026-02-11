package com.example.quickcash.ui;

import static java.lang.Double.parseDouble;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcash.R;
//import com.example.quickcash.data.FirebaseCRUD;
//import com.example.quickcash.model.Job;
//import com.example.quickcash.model.JobSearchFilter;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRegistrar;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Locale;

public class EmployeeDashboardActivity extends AppCompatActivity {
    //UI element
    private TextView tvRole, tvResults, tvResultsList;
    private EditText etJobTitle, etMinSalary, etMaxSalary, etMaxDuration, etVicinityKm;
    private Button btnSearch, btnLogout;
    //private FirebaseAuth auth;
    //private FirebaseCRUD crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
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
        //auth = FirebaseAuth.getInstance();
        //crud = new FirebaseCRUD();

        //search button
        //btnSearch.setOnClickListener(v -> onSearchClicked());
        //logout button
        //btnLogout.setOnClickListener(v -> logout());

    }



//    private void logout() {
//        auth.signOut();
//        Intent i = new Intent(this, LoginActivity.class); // make sure LoginActivity is in ui package
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//        finish();
//    }


    }
