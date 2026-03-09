package com.example.csci3130groupproject;

import com.example.csci3130groupproject.core.AddressValidator;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddressValidatorTest {

    @Test
    public void emptyAddress_returnsFalse() {
        assertFalse(AddressValidator.isValidAddress(""));
    }

    @Test
    public void nullAddress_returnsFalse() {
        assertFalse(AddressValidator.isValidAddress(null));
    }
}