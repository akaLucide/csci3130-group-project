package com.example.csci3130groupproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // database variables
    FirebaseDB database;

    // UI variables
    EditText email, password;
    Button loginBtn, signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize database
        database = new FirebaseDB(getResources().getString(R.string.FIREBASE_DB_URL));

        // initialize UI
        initUIComponents();
        setupLoginButton();
        setupSignupButton();
    }

    // initializes all UI components
    private void initUIComponents() {
        email = findViewById(R.id.loginEmailEditText);
        password = findViewById(R.id.loginPasswordEditText);
        loginBtn = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.goToSignupButton);
    }

    // assigns listener for login button
    private void setupLoginButton() {
        loginBtn.setOnClickListener(this::onLoginClick);
    }

    // assigns listener for signup button (to go back to signup page)
    private void setupSignupButton() {
        signupBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });
    }

    // validates that fields are not empty
    private String emptyField() {
        if (getEmail().isEmpty() || getPassword().isEmpty()) {
            return "Please fill in all fields";
        }
        return "";
    }

    // onclick method for login button
    public void onLoginClick(View view) {
        // check for empty fields
        String errorMessage = emptyField();

        if (!errorMessage.isEmpty()) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        // attempt to login with Firebase Authentication
        database.loginUser(getEmail(), getPassword(), this);
    }

    // text getters
    protected String getEmail() {
        return email.getText().toString().trim();
    }

    protected String getPassword() {
        return password.getText().toString();
    }
}
