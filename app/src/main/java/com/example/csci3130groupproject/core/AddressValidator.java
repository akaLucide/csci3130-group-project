package com.example.csci3130groupproject.core;

/**
 * Utility class responsible for validating job location addresses.
 *
 * An address is considered valid if it is not null and contains
 * at least one non-whitespace character after trimming.
 */
public class AddressValidator {

    /**
     * Validates whether a given address string is usable.
     *
     * @param address the address string entered by the user
     * @return true if the address contains characters after trimming whitespace,
     *         false otherwise
     */
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