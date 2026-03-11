package com.example.csci3130groupproject.util;

import com.example.csci3130groupproject.core.Job;

import java.util.List;

public final class JobDetailsFormatter {

    private JobDetailsFormatter() {
    }

    public static String format(Job job) {
        if (job == null) {
            return "Title: Not provided"
                    + "\nCategory: Not provided"
                    + "\nDescription: Not provided"
                    + "\nLocation: Not provided"
                    + "\nSalary/hr: $0.0"
                    + "\nDuration: 0.0 hours"
                    + "\nUrgency: Not provided"
                    + "\nDate: Not provided";
        }

        String title = valueOrFallback(job.title);
        String category = valueOrFallback(job.category);
        String description = valueOrFallback(job.description);
        String location = valueOrFallback(job.locationAddress);
        String urgency = valueOrFallback(job.urgency);
        String date = valueOrFallback(job.date);

        // Format a single job's detailed information
        return "Title: " + title
                + "\nCategory: " + category
                + "\nDescription: " + description
                + "\nLocation: " + location
                + "\nSalary/hr: $" + job.salaryPerHour
                + "\nDuration: " + job.expectedDurationHours + " hours"
                + "\nUrgency: " + urgency
                + "\nDate: " + date;
    }

    // Format the results counter text
    public static String resultsCountText(int count) {
        return "Results: " + count;
    }

    // Format a list of jobs for display
    public static String formatList(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return "No matching jobs found.";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jobs.size(); i++) {
            sb.append(i + 1)
                    .append(") ")
                    .append(format(jobs.get(i)));

            if (i < jobs.size() - 1) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }

    // Return fallback text if value is empty
    private static String valueOrFallback(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Not provided";
        }
        return value.trim();
    }
}