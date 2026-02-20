package com.example.csci3130groupproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class RoleLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String role = getIntent().getStringExtra("ROLE");
        if (role == null) {
            goLogin("Role missing");
            return;
        }

        String r = role.trim().toLowerCase();
        if (r.equals("employer")) {
            startActivity(new Intent(this, EmployerDashboardActivity.class).putExtra("ROLE", "Employer"));
            finish();
        } else if (r.equals("employee")) {
            startActivity(new Intent(this, EmployeeDashboardActivity.class).putExtra("ROLE", "Employee"));
            finish();
        } else {
            goLogin("Invalid role");
        }
    }

    private void goLogin(String msg) {
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("ERROR", msg);
        startActivity(i);
        finish();
    }
}
