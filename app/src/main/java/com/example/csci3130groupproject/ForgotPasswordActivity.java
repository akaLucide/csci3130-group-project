package com.example.csci3130groupproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Activity for the Forgot Password screen
public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordButton;
    private TextView statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables edge-to-edge layout
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Link UI components to variables
        emailInput = findViewById(R.id.emailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        statusMessage = findViewById(R.id.statusMessage);

        // Run validation when button is clicked
        resetPasswordButton.setOnClickListener(v -> handlePasswordReset());
    }

    // Handles email validation and status messages
    private void handlePasswordReset() {

        String emailText = emailInput.getText().toString().trim();

        // Check if email field is empty
        if (emailText.isEmpty()) {
            statusMessage.setText("Please enter your email.");
            return;
        }

        // Check if email format is invalid
        if (!EmailValidator.isValidEmail(emailText)) {
            statusMessage.setText("Please enter a valid email address.");
            return;
        }

        // Display success message (no backend call in this iteration)
        statusMessage.setText("If this email exists, a reset link will be sent.");
    }
}