package com.example.csci3130groupproject;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static boolean isValidEmail(String email) {
        if (email == null) return false;

        String trimmed = email.trim();
        if (trimmed.isEmpty()) return false;

        return EMAIL_PATTERN.matcher(trimmed).matches();
    }
}