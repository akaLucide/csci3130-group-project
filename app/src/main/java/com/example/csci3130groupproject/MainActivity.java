package com.example.csci3130groupproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.csci3130groupproject.BuildConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    // database variables
    FirebaseDatabase database;
    DatabaseReference dbref;
    String DB_URL;

    // UI variables
    EditText name, email, password, confirmPassword;
    Spinner role;
    String[] roles = {"employer", "employee"};
    Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // database variables
        DB_URL = BuildConfig.FIREBASE_DB_URL;
        database = FirebaseDatabase.getInstance(DB_URL);
        dbref = database.getReference("users");

        initUIComponents();
    }

    // initializes all UI component text boxes
    public void initUIComponents(){
        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);
        role = findViewById(R.id.roleSpinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        role.setAdapter(roleAdapter);
        signup = findViewById(R.id.signupButton);

        Button goToLogin = findViewById(R.id.goToLoginButton);
        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }
}