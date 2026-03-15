package com.example.csci3130groupproject.core;

import android.text.TextUtils;

import java.util.Locale;

/**
 * Evaluates whether a job matches all criteria in a search filter.
 * Used to determine if a job should be included in search results.
 */
public class JobMatcher {
    /**
     * Returns true only if the job satisfies every active filter field in the given filter.
     *
     * @param job The job to evaluate.
     * @param f   The search filter containing user criteria.
     * @return true if the job matches all filter criteria, false otherwise.
     */
    public boolean matches(Job job, JobSearchFilter f) {
        if (job == null || f == null) return false;

        // Title filter: match title, category, OR description
        if (!TextUtils.isEmpty(f.titleQuery)) {
            String title = (job.title == null) ? "" : job.title.toLowerCase(Locale.ROOT);
            String category = (job.category == null) ? "" : job.category.toLowerCase(Locale.ROOT);
            String description = (job.description == null) ? "" : job.description.toLowerCase(Locale.ROOT);

            if (!title.contains(f.titleQuery) && !category.contains(f.titleQuery)
                    && !description.contains(f.titleQuery)) {
                return false;
            }
        }

        // Salary range
        if (job.salaryPerHour < f.minSalary || job.salaryPerHour > f.maxSalary) return false;

        // Duration max
        if (job.expectedDurationHours > f.maxDuration) return false;

        // Vicinity not used yet (no map/location)
        return true;
    }
}
