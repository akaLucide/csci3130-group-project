package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.csci3130groupproject.core.JobCategoryFilter;

import org.junit.Test;

public class JobCategoryFilterTest {

    @Test
    public void filter_defaultCategoryQuery_isNull() {
        JobCategoryFilter filter = new JobCategoryFilter();
        assertNotNull(filter);
        assertEquals(null, filter.categoryQuery);
    }

    @Test
    public void filter_setCategoryQuery_storesValue() {
        JobCategoryFilter filter = new JobCategoryFilter();
        filter.categoryQuery = "Pet Services";
        assertEquals("Pet Services", filter.categoryQuery);
    }
}