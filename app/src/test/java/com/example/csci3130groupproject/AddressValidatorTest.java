package com.example.csci3130groupproject;

import com.example.csci3130groupproject.core.AddressValidator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the AddressValidator class.
 * These tests ensure that address validation behaves correctly
 * for valid and invalid inputs.
 */
public class AddressValidatorTest {

    /**
     * Unit Test: Empty Address
     *
     * Given an empty string is provided,
     * when the address validator checks the value,
     * then the result should be false.
     */
    @Test
    public void emptyAddress_returnsFalse() {
        assertFalse(AddressValidator.isValidAddress(""));
    }

    /**
     * Unit Test: Null Address
     *
     * Given a null address value,
     * when the validator checks the value,
     * then the result should be false.
     */
    @Test
    public void nullAddress_returnsFalse() {
        assertFalse(AddressValidator.isValidAddress(null));
    }

    /**
     * Unit Test: Whitespace Address
     *
     * Given an address containing only whitespace,
     * when the validator trims and checks the value,
     * then the result should be false.
     */
    @Test
    public void whitespaceAddress_returnsFalse() {
        assertFalse(AddressValidator.isValidAddress("   "));
    }

    /**
     * Unit Test: Valid Address
     *
     * Given a properly formatted address string,
     * when the validator checks the address,
     * then the result should be true.
     */
    @Test
    public void validAddress_returnsTrue() {
        assertTrue(AddressValidator.isValidAddress("6225 University Ave, Halifax, NS"));
    }
}