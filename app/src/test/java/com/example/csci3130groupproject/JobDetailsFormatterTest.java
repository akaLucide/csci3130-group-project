package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.util.JobDetailsFormatter;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class JobDetailsFormatterTest {

    @Test
    public void format_withAllFields_showsDetailedInformation() {
        Job job = new Job();
        job.title = "Babysitter";
        job.category = "Child Care";
        job.description = "Take care of two children in the evening";
        job.locationAddress = "123 Main Street";
        job.salaryPerHour = 20.0;
        job.expectedDurationHours = 4.0;
        job.urgency = "High";
        job.date = "2026-03-15";

        String result = JobDetailsFormatter.format(job);

        assertTrue(result.contains("Title: Babysitter"));
        assertTrue(result.contains("Category: Child Care"));
        assertTrue(result.contains("Description: Take care of two children in the evening"));
        assertTrue(result.contains("Location: 123 Main Street"));
        assertTrue(result.contains("Salary/hr: $20.0"));
        assertTrue(result.contains("Duration: 4.0 hours"));
        assertTrue(result.contains("Urgency: High"));
        assertTrue(result.contains("Date: 2026-03-15"));
    }

    @Test
    public void format_missingFields_showsNotProvidedFallback() {
        Job job = new Job();
        job.title = null;
        job.category = "";
        job.description = null;
        job.locationAddress = "";
        job.urgency = null;
        job.date = "";

        String result = JobDetailsFormatter.format(job);

        assertTrue(result.contains("Title: Not provided"));
        assertTrue(result.contains("Category: Not provided"));
        assertTrue(result.contains("Description: Not provided"));
        assertTrue(result.contains("Location: Not provided"));
        assertTrue(result.contains("Urgency: Not provided"));
        assertTrue(result.contains("Date: Not provided"));
    }

    @Test
    public void resultsCountText_returnsCorrectCount() {
        assertEquals("Results: 2", JobDetailsFormatter.resultsCountText(2));
    }

    @Test
    public void formatList_withNoJobs_showsNoMatchingJobsMessage() {
        String result = JobDetailsFormatter.formatList(Collections.emptyList());

        assertEquals("No matching jobs found.", result);
    }

    @Test
    public void formatList_withMultipleJobs_numbersAndSeparatesJobs() {
        Job job1 = new Job();
        job1.title = "Babysitter";
        job1.category = "Child Care";
        job1.description = "Evening shift";
        job1.locationAddress = "123 Main Street";
        job1.salaryPerHour = 18.0;
        job1.expectedDurationHours = 3.0;
        job1.urgency = "Medium";
        job1.date = "2026-03-15";

        Job job2 = new Job();
        job2.title = "Dog Walker";
        job2.category = "Pet Care";
        job2.description = "Walk dog for 1 hour";
        job2.locationAddress = "456 Park Ave";
        job2.salaryPerHour = 15.0;
        job2.expectedDurationHours = 1.0;
        job2.urgency = "Low";
        job2.date = "2026-03-16";

        String result = JobDetailsFormatter.formatList(Arrays.asList(job1, job2));

        assertTrue(result.contains("1) Title: Babysitter"));
        assertTrue(result.contains("2) Title: Dog Walker"));
        assertTrue(result.contains("Category: Child Care"));
        assertTrue(result.contains("Category: Pet Care"));
    }

    @Test
    public void dashboardTitle_usesTitleWhenPresent() {
        Job job = new Job();
        job.title = "Dog Walker";
        job.category = "Pet Care";
        job.date = "2026-03-16";

        String result = JobDetailsFormatter.dashboardTitle(job);

        assertEquals("Dog Walker", result);
    }

    @Test
    public void dashboardTitle_fallsBackToCategoryAndDate() {
        Job job = new Job();
        job.title = "";
        job.category = "Child Care";
        job.date = "2026-03-15";

        String result = JobDetailsFormatter.dashboardTitle(job);

        assertEquals("Child Care - 2026-03-15", result);
    }
}