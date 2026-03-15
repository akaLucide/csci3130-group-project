package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.csci3130groupproject.core.JobTypeManager;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobTypeManagerTest {

    @Test
    public void createType_emptyName_returnsNameRequired() {
        List<String> existing = Arrays.asList("Pet services", "Babysitting");

        String result = JobTypeManager.validateNewType("   ", existing);

        assertEquals("Job type name required", result);
    }

    @Test
    public void createType_duplicateIgnoringCase_returnsAlreadyExists() {
        List<String> existing = Arrays.asList("Pet services", "Babysitting");

        String result = JobTypeManager.validateNewType("pet SERVICES", existing);

        assertEquals("Job type already exists", result);
    }

    @Test
    public void createType_validName_returnsOK() {
        List<String> existing = Arrays.asList("Pet services", "Babysitting");

        String result = JobTypeManager.validateNewType("Tutoring", existing);

        assertEquals("OK", result);
    }

    @Test
    public void addType_validName_appendsTrimmedType() {
        List<String> existing = new ArrayList<>(Arrays.asList("Pet services", "Babysitting"));

        List<String> updated = JobTypeManager.addType(existing, "  Tutoring  ");

        assertEquals(3, updated.size());
        assertEquals("Tutoring", updated.get(2));
    }

    @Test
    public void removeType_existingType_removesIt() {
        List<String> existing = new ArrayList<>(Arrays.asList("Pet services", "Babysitting", "Tutoring"));

        List<String> updated = JobTypeManager.removeType(existing, "Babysitting");

        assertEquals(2, updated.size());
        assertTrue(!updated.contains("Babysitting"));
    }

    @Test
    public void removeType_missingType_keepsListUnchanged() {
        List<String> existing = new ArrayList<>(Arrays.asList("Pet services", "Babysitting"));

        List<String> updated = JobTypeManager.removeType(existing, "Tutoring");

        assertEquals(2, updated.size());
        assertEquals(existing, updated);
    }
}