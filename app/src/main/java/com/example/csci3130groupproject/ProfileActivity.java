package com.example.csci3130groupproject;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);

        loadProfileSummary();
    }

    private void loadProfileSummary() {
        // Temporary hardcoded data (can be replaced with real user data)
        String name = "Jane Doe";
        String email = "jane.doe@example.com";
        String role = "Member";

        tvName.setText("Name: " + name);
        tvEmail.setText("Email: " + email);
        tvRole.setText("Role: " + role);
    }
}