package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.csci3130groupproject.util.JobCategoryUtil;

import org.junit.Test;

public class JobCategoryUtilTest {

    @Test
    public void normalizeKey_trimsAndLowercasesCategory() {
        String result = JobCategoryUtil.normalizeKey("  Pet   Services  ");
        assertEquals("pet services", result);
    }

    @Test
    public void resolveStoredOrNewDisplayName_withoutStoredCategory_formatsTypedCategory() {
        String result = JobCategoryUtil.resolveStoredOrNewDisplayName(null, "  pet services ");
        assertEquals("Pet Services", result);
    }

    @Test
    public void matchesFilter_sameCategoryDifferentCase_returnsTrue() {
        boolean result = JobCategoryUtil.matchesFilter("Pet Services", "pet services");
        assertTrue(result);
    }

    @Test
    public void matchesFilter_emptyFilter_returnsTrue() {
        boolean result = JobCategoryUtil.matchesFilter("Pet Services", "");
        assertTrue(result);
    }

    @Test
    public void matchesFilter_differentCategory_returnsFalse() {
        boolean result = JobCategoryUtil.matchesFilter("Pet Services", "Babysitting");
        assertFalse(result);
    }
}