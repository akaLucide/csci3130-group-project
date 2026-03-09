package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;

import com.example.csci3130groupproject.util.JobValidator;

import org.junit.Test;

public class JobValidatorTest {

    @Test
    public void validate_emptyDescription_returnsDescriptionRequired() {
        String result = JobValidator.validate("", "2026-03-10");
        assertEquals("Description required", result);
    }

    @Test
    public void validate_noDateSelected_returnsPickDateFirst() {
        String result = JobValidator.validate("Fix sink", "No date selected");
        assertEquals("Pick a date first", result);
    }

    @Test
    public void validate_validInput_returnsOK() {
        String result = JobValidator.validate("Fix sink", "2026-03-10");
        assertEquals("OK", result);
    }
}