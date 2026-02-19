package com.example.csci3130groupproject;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class LoginUnitTest {

    @Test
    public void checkIfEmailIsEmpty() {
        String emptyEmail = "";
        String validEmail = "andrewTest@gmail.com";

        assertTrue(emptyEmail.trim().isEmpty());
        assertFalse(validEmail.trim().isEmpty());
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        String emptyPassword = "";
        String validPassword = "password";

        assertTrue(emptyPassword.isEmpty());
        assertFalse(validPassword.isEmpty());
    }

    @Test
    public void checkIfBothFieldsEmpty() {
        String email = "";
        String password = "";

        boolean hasError = email.isEmpty() || password.isEmpty();
        assertTrue(hasError);
    }

    @Test
    public void checkIfValidCredentials() {
        String email = "andrewTest@gmail.com";
        String password = "password";

        boolean hasError = email.isEmpty() || password.isEmpty();
        assertFalse(hasError);
    }
}