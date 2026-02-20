package com.example.csci3130groupproject;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.csci3130groupproject.ui.MainActivity;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import org.junit.Test;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class SignupUnitTest {

    ActivityController<MainActivity> controller;
    MainActivity shadow;

    @Before
    public void setup(){
        controller = Robolectric.buildActivity(MainActivity.class).setup();
        shadow = controller.get();
    }

    @Test
    public void emptyField() {
        EditText name = shadow.findViewById(R.id.nameEditText);
        name.setText("braedon");
        EditText email = shadow.findViewById(R.id.emailEditText);
        email.setText("braedon@gmail.com");
        EditText password = shadow.findViewById(R.id.passwordEditText);
        password.setText("braedon");
        EditText confirm = shadow.findViewById(R.id.confirmPasswordEditText);
        confirm.setText("braedon");
        Button signup = shadow.findViewById(R.id.signupButton);
        signup.performClick();
        TextView status = shadow.findViewById(R.id.statusLabel);
        assert status.getText().equals("Empty Field");
    }

    @Test
    public void invalidEmail() {
        EditText name = shadow.findViewById(R.id.nameEditText);
        name.setText("braedon");
        EditText email = shadow.findViewById(R.id.emailEditText);
        email.setText("braedon");
        EditText password = shadow.findViewById(R.id.passwordEditText);
        password.setText("braedon");
        EditText confirm = shadow.findViewById(R.id.confirmPasswordEditText);
        confirm.setText("braedon");
        Spinner role = shadow.findViewById(R.id.roleSpinner);
        role.performClick();
        role.setSelection(2);
        Button signup = shadow.findViewById(R.id.signupButton);
        signup.performClick();
        TextView status = shadow.findViewById(R.id.statusLabel);
        assert status.getText().equals("Invalid Email");
    }

    @Test
    public void invalidPass() {
        EditText name = shadow.findViewById(R.id.nameEditText);
        name.setText("braedon");
        EditText email = shadow.findViewById(R.id.emailEditText);
        email.setText("braedon@gmail.com");
        EditText password = shadow.findViewById(R.id.passwordEditText);
        password.setText("br");
        EditText confirm = shadow.findViewById(R.id.confirmPasswordEditText);
        confirm.setText("braedon");
        Spinner role = shadow.findViewById(R.id.roleSpinner);
        role.performClick();
        role.setSelection(2);
        Button signup = shadow.findViewById(R.id.signupButton);
        signup.performClick();
        TextView status = shadow.findViewById(R.id.statusLabel);
        assert status.getText().equals("Password is too short");
    }

    @Test
    public void passDoesNotMatch() {
        EditText name = shadow.findViewById(R.id.nameEditText);
        name.setText("braedon");
        EditText email = shadow.findViewById(R.id.emailEditText);
        email.setText("braedon@gmail.com");
        EditText password = shadow.findViewById(R.id.passwordEditText);
        password.setText("braedon");
        EditText confirm = shadow.findViewById(R.id.confirmPasswordEditText);
        confirm.setText("braedo");
        Spinner role = shadow.findViewById(R.id.roleSpinner);
        role.performClick();
        role.setSelection(2);
        Button signup = shadow.findViewById(R.id.signupButton);
        signup.performClick();
        TextView status = shadow.findViewById(R.id.statusLabel);
        assert status.getText().equals("Password does not match");
    }

    @Test
    public void validInput() {
        EditText name = shadow.findViewById(R.id.nameEditText);
        name.setText("braedon");
        EditText email = shadow.findViewById(R.id.emailEditText);
        email.setText("jUnitTest@gmail.com");
        EditText password = shadow.findViewById(R.id.passwordEditText);
        password.setText("braedon");
        EditText confirm = shadow.findViewById(R.id.confirmPasswordEditText);
        confirm.setText("braedon");
        Spinner role = shadow.findViewById(R.id.roleSpinner);
        role.performClick();
        role.setSelection(2);
        Button signup = shadow.findViewById(R.id.signupButton);
        signup.performClick();
        TextView status = shadow.findViewById(R.id.statusLabel);
        assert status.getText().isEmpty();
    }
}
