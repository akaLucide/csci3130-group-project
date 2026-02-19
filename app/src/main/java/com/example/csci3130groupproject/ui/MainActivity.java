package com.example.csci3130groupproject.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.csci3130groupproject.data.FirebaseDB;
import com.example.csci3130groupproject.R;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // database variables
    FirebaseDB database;

    // UI variables
    EditText name, email, password, confirmPassword;
    Spinner role;
    String[] roles = {"Select a role", "employer", "employee"};
    Button signup;
    TextView status;

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

        // initialize database
        database = new FirebaseDB(getResources().getString(R.string.FIREBASE_DB_URL));

        // initialize UI
        initUIComponents();
        setupSignUpButton();
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
        status = findViewById(R.id.statusLabel);

        Button goToLogin = findViewById(R.id.goToLoginButton);
        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }

    // changes status message for signup form
    protected void setStatusLabel(String message){
        status.setText(message);
    }

    // assigns listener for sign up button
    protected void setupSignUpButton(){
        signup.setOnClickListener(this::onClick);
    }

    // checks if any fields in the form are empty
    protected void emptyField(){
        // if any field is empty return empty field
        if (getName().isEmpty() || getEmail().isEmpty() || getPassword().isEmpty() ||
                getConfirmPassword().isEmpty() || getRole().equals("Select a role")){
            setStatusLabel("Empty Field");
            return;
        }
        setStatusLabel("");

    }

    // checks if passwords match
    protected void passwordMatches(){
        if (!getPassword().equals(getConfirmPassword())){
            setStatusLabel("Password does not match");
            return;
        }
        setStatusLabel("");
    }

    // checks naive email regex for correct email
    protected void validEmail(){
        String emailRegex = ".*@.*\\..*$";
        if(!Pattern.matches(emailRegex, getEmail())){
            setStatusLabel("Invalid Email");
            return;
        }
        setStatusLabel("");
    }

    // method to be populated for password criteria
    protected void validPass(){
        if(getPassword().length() < 6){
            setStatusLabel("Password is too short");
            return;
        }
        setStatusLabel("");
    }

    // onclick method for sign up button
    public void onClick(View view){

        // call methods to ensure data input is correct, if errorMessage is not empty the user signup will cancel and send an error message
        emptyField();
        if(status.getText().toString().isEmpty()){
            validEmail();
        }
        if(status.getText().toString().isEmpty()){
            validPass();
        }
        if(status.getText().toString().isEmpty()){
            passwordMatches();
        }

        // if the error message is not empty (there was an error) we quit and print the message
        if(!status.getText().toString().isEmpty()){
            return;
        }

        // attempts to create account given proper signup, failure handled in FirebaseDB
        database.addUser(getName(), getEmail(), getPassword(), getRole(), this)
                .addOnSuccessListener(unused -> {
                    directToLogin();
                }).addOnFailureListener(e -> {
                    setStatusLabel("Authentification failed, duplicate account");
                });
    }

    protected void directToLogin(){
         Intent login = new Intent(MainActivity.this, LoginActivity.class);
         startActivity(login);
    }

    // text getters
    protected String getName(){ return name.getText().toString(); }
    protected String getEmail(){ return email.getText().toString(); }
    protected String getPassword(){ return password.getText().toString(); }
    protected String getConfirmPassword(){ return confirmPassword.getText().toString(); }
    protected String getRole(){ return role.getSelectedItem().toString(); }

}