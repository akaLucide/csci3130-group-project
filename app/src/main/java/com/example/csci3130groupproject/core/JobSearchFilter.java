package com.example.csci3130groupproject.core;

/**
 * Holds user-entered search criteria for job search, including title, salary, duration, and vicinity.
 * Passed from JobSearchPageActivity to the dashboard and used for filtering jobs.
 */
public class JobSearchFilter {
    public String titleQuery;
    public double minSalary;
    public double maxSalary;
    public double maxDuration;
    public double vicinityKm;
}
