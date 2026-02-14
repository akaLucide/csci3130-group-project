package com.example.csci3130groupproject;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailValidatorTest {

    @Test
    public void emptyEmail_returnsFalse() {
        assertFalse(EmailValidator.isValidEmail(""));
    }

    @Test
    public void invalidEmail_returnsFalse() {
        assertFalse(EmailValidator.isValidEmail("mitchell"));
    }

    @Test
    public void validEmail_returnsTrue() {
        assertTrue(EmailValidator.isValidEmail("test@gmail.com"));
    }
}