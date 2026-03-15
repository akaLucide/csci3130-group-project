package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.core.JobMatcher;
import com.example.csci3130groupproject.core.JobSearchFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class JobMatcherTest {

    private JobMatcher matcher;

    @Before
    public void setUp() {
        matcher = new JobMatcher();
    }

    private Job createJob(String title, String category) {
        Job job = new Job();
        job.title = title;
        job.category = category;
        return job;
    }

    private JobSearchFilter keywordFilter(String keyword) {
        JobSearchFilter f = new JobSearchFilter();
        f.titleQuery = keyword;
        f.minSalary = 0;
        f.maxSalary = Double.MAX_VALUE;
        f.maxDuration = Double.MAX_VALUE;
        f.vicinityKm = Double.MAX_VALUE;
        return f;
    }

    /**
     * Acceptance Criteria 2: Search by keyword
     * Given I am on the employee dashboard,
     * When I enter a keyword and press "Search Jobs",
     * Then only jobs whose title or category contain that keyword (not case sensitive) are displayed.
     */
    @Test
    public void keyword_matchesJobTitle_caseInsensitive() {
        Job job = createJob("Mowing the lawn", "Mowing the lawn");
        JobSearchFilter filter = keywordFilter("mowing");
        assertTrue(matcher.matches(job, filter));
    }

    /**
     * Acceptance Criteria 3: Empty search returns all jobs
     * Given I am on the employee dashboard,
     * When I leave the search field empty and press "Search Jobs",
     * Then all available jobs are returned.
     */
    @Test
    public void emptyKeyword_matchesAnyJob() {
        Job job = createJob("Picking up groceries", "Picking up groceries");
        JobSearchFilter filter = keywordFilter("");
        assertTrue(matcher.matches(job, filter));
    }

    /**
     * Acceptance Criteria 4: No results found
     * Given I am on the employee dashboard,
     * When I search with a keyword that matches no jobs,
     * Then I see a message "No matching jobs found." and the results count shows 0.
     */
    @Test
    public void noJobsMatch_filterReturnsZero() {
        Job job1 = createJob("PC repairing", "PC repairing");
        Job job2 = createJob("Pet services", "Pet services");

        JobSearchFilter filter = keywordFilter("babysitting");

        int matchCount = 0;
        Job[] jobs = {job1, job2};
        for (Job j : jobs) {
            if (matcher.matches(j, filter)) matchCount++;
        }
        assertEquals(0, matchCount);
    }

    /**
     * Acceptance Criteria 5: Search results display job details
     * Given a search returns results,
     * When I view the results list,
     * Then each result shows the job title, category, salary/hour, and duration.
     *
     * Acceptance Criteria 6: Result count displayed
     * Given a search returns results,
     * When the results are displayed,
     * Then I see "Results: X" where X is the number of matching jobs.
     */
    @Test
    public void multipleJobs_filterReturnsCorrectSubset() {
        Job job1 = createJob("Babysitting", "Babysitting");
        Job job2 = createJob("Mowing the lawn", "Mowing the lawn");
        Job job3 = createJob("Babysitting evening", "Babysitting");
        Job job4 = createJob("PC repairing", "PC repairing");

        JobSearchFilter filter = keywordFilter("baby");

        int matchCount = 0;
        Job[] jobs = {job1, job2, job3, job4};
        for (Job j : jobs) {
            if (matcher.matches(j, filter)) matchCount++;
        }
        assertEquals(2, matchCount);
    }
}
