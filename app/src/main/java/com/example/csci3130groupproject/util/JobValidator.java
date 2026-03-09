package com.example.csci3130groupproject.util;

public class JobValidator {
    public static String validate(String description, String date) {
        if (description == null || description.trim().isEmpty()) {
            return "Description required";
        }

        if (date == null || date.equals("No date selected")) {
            return "Pick a date first";
        }

        return "OK";
    }
}
