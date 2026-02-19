package com.example.csci3130groupproject.core;

import java.util.regex.Pattern;

// Basic email validation helper class
public class EmailValidator {

    // Regex pattern used to check email format
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Returns true if the email matches the pattern
    public static boolean isValidEmail(String email) {

        // Return false if email is null
        if (email == null) return false;

        // Remove extra spaces and check if empty
        String trimmed = email.trim();
        if (trimmed.isEmpty()) return false;

        // Check email against regex pattern
        return EMAIL_PATTERN.matcher(trimmed).matches();
    }
}