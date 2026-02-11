package com.example.csci3130groupproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // database variables
    FirebaseDB database;

    // UI variables
    EditText name, email, password, confirmPassword;
    Spinner role;
    String[] roles = {"Select a role", "employer", "employee"};
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

        Button goToLogin = findViewById(R.id.goToLoginButton);
        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }

    // assigns listener for sign up button
    protected void setupSignUpButton(){
        signup.setOnClickListener(this::onClick);
    }

    // checks if any fields in the form are empty
    protected String emptyField(){
        // if any field is empty return empty field
        if (getName().isEmpty() || getEmail().isEmpty() || getPassword().isEmpty() ||
                getConfirmPassword().isEmpty() || getRole().equals("Select a role")){
            return " Empty Field";
        }
        return "";
    }

    // checks if passwords match
    protected String passwordMatches(){
        if (!getPassword().equals(getConfirmPassword())){
            return " Password does not match";
        }
        return "";
    }

    // checks naive email regex for correct email
    protected boolean validEmail(){
        String emailRegex = ".*@.*\\..*$";
        return Pattern.matches(emailRegex, getEmail());
    }

    // method to be populated for password criteria
    //protected String validPass(){} for password criteria

    // onclick method for sign up button
    public void onClick(View view){

        // call methods to ensure data input is correct, if errorMessage is not empty the user signup will cancel and send an error message
        String errorMessage = emptyField();
        if (!validEmail()) {
            errorMessage += " Invalid email";

        }
        errorMessage += passwordMatches();

        // if the error message is not empty (there was an error) we quit and print the message
        if(!errorMessage.isEmpty()){
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        // attempts to create account given proper signup, failure handled in FirebaseDB
        database.addUser(getName(), getEmail(), getPassword(), getRole(), this);
    }

    //protected void directToLogin(){  new intent - pass anything - send to login }

    // text getters
    protected String getName(){ return name.getText().toString(); }
    protected String getEmail(){ return email.getText().toString(); }
    protected String getPassword(){ return password.getText().toString(); }
    protected String getConfirmPassword(){ return confirmPassword.getText().toString(); }
    protected String getRole(){ return role.getSelectedItem().toString(); }

}