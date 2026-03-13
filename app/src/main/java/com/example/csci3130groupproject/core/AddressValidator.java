package com.example.csci3130groupproject.core;

public class AddressValidator {

    //validates that an address string is usable
    //an address is considered valid if it is not null and empty
    //after removing leading/trailing whitespaces.
    public static boolean isValidAddress(String address) {

        //if an address is null its not valid
        if (address == null) {
            return false;
        }

        //remove leading and trailing whitespaces
        String trimmed = address.trim();

        //if the trimmed string is empty then the address only contained spaces
        if (trimmed.isEmpty()) {
            return false;
        }

        //otherwise, the address contains actual characters
        return true;
    }
}