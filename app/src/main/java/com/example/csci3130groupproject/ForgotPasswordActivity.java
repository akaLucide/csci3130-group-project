package com.example.csci3130groupproject;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordButton;
    private TextView statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInput = findViewById(R.id.emailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        statusMessage = findViewById(R.id.statusMessage);

        resetPasswordButton.setOnClickListener(v -> handlePasswordReset());
    }

    private void handlePasswordReset(){
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            statusMessage.setText("Please enter your email.");
            return;
        }

        String emailText = emailInput.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            statusMessage.setText("Please enter a valid email address.");
            return;
        }

        statusMessage.setText("If this email exists, a reset link will be sent.");
    }
}