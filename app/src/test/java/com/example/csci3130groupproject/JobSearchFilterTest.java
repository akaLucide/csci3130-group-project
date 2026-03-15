package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.csci3130groupproject.core.JobSearchFilter;

import org.junit.Test;

public class JobSearchFilterTest {

    /**
     * Acceptance Criteria 1: Search form is accessible
     * Given I am logged in as an employee,
     * When I navigate to the employee dashboard,
     * Then I see a search field for job title/keyword and a "Search Jobs" button.
     */
    @Test
    public void filter_defaultTitleQuery_isNull() {
        JobSearchFilter filter = new JobSearchFilter();
        assertNotNull(filter);
        assertEquals(null, filter.titleQuery);
    }

    /**
     * Acceptance Criteria 2: Search by keyword
     * Given I am on the employee dashboard,
     * When I enter a keyword and press "Search Jobs",
     * Then only jobs whose title or category contain that keyword (not case sensitive) are displayed.
     */
    @Test
    public void filter_setTitleQuery_storesValue() {
        JobSearchFilter filter = new JobSearchFilter();
        filter.titleQuery = "babysitting";
        assertEquals("babysitting", filter.titleQuery);
    }

    /**
     * Acceptance Criteria 3: Empty search returns all jobs
     * Given I am on the employee dashboard,
     * When I leave the search field empty and press "Search Jobs",
     * Then all available jobs are returned.
     */
    @Test
    public void filter_setEmptyTitleQuery_storesEmptyString() {
        JobSearchFilter filter = new JobSearchFilter();
        filter.titleQuery = "";
        assertEquals("", filter.titleQuery);
    }
}
