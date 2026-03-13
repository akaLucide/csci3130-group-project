package com.example.csci3130groupproject;

import com.example.csci3130groupproject.core.AddressValidator;

import org.junit.Test;

import static org.junit.Assert.*;

//Unit tests for the AddressValidator class
//These tests verify that invalid addresses are rejected and valid addresses are accepted
public class AddressValidatorTest {

    //an empty string should not be considered a valid address
    @Test
    public void emptyAddress_returnsFalse() {
        assertFalse(AddressValidator.isValidAddress(""));
    }

    //a null address should not be considered a valid address
    @Test
    public void nullAddress_returnsFalse() {
        assertFalse(AddressValidator.isValidAddress(null));
    }

    //an address containing only whitespace should not be considered a valid address
    @Test
    public void whitespaceAddress_returnsFalse() {
        assertFalse(AddressValidator.isValidAddress("   "));
    }

    //This test verifies that the validator accepts a normal non-empty address string and returns true.
    @Test
    public void validAddress_returnsTrue() {
        assertTrue(AddressValidator.isValidAddress("6225 University Ave, Halifax, NS"));
    }
}