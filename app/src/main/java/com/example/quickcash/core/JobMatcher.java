package com.example.quickcash.core;

import android.text.TextUtils;

import com.example.quickcash.model.Job;
import com.example.quickcash.model.JobSearchFilter;

import java.util.Locale;

public class JobMatcher {
    public boolean matches(Job job, JobSearchFilter f) {
        if (job == null || f == null) return false;

        // Title filter: match title OR category
        if (!TextUtils.isEmpty(f.titleQuery)) {
            String title = (job.title == null) ? "" : job.title.toLowerCase(Locale.ROOT);
            String category = (job.category == null) ? "" : job.category.toLowerCase(Locale.ROOT);

            if (!title.contains(f.titleQuery) && !category.contains(f.titleQuery)) {
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
