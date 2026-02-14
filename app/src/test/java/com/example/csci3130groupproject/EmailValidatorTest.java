package com.example.csci3130groupproject;

import org.junit.Test;

import static org.junit.Assert.*;

// Unit tests for EmailValidator
public class EmailValidatorTest {

    // Empty string should return false
    @Test
    public void emptyEmail_returnsFalse() {
        assertFalse(EmailValidator.isValidEmail(""));
    }

    // Invalid format should return false
    @Test
    public void invalidEmail_returnsFalse() {
        assertFalse(EmailValidator.isValidEmail("mitchell"));
    }

    // Valid email format should return true
    @Test
    public void validEmail_returnsTrue() {
        assertTrue(EmailValidator.isValidEmail("test@gmail.com"));
    }
}