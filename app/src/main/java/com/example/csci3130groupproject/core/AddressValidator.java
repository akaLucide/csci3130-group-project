package com.example.csci3130groupproject.core;

public class AddressValidator {

    public static boolean isValidAddress(String address) {

        if (address == null) {
            return false;
        }

        String trimmed = address.trim();

        if (trimmed.isEmpty()) {
            return false;
        }

        return true;
    }
}