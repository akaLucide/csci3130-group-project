package com.example.csci3130groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci3130groupproject.ui.JobSearchPageActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI tests for JobSearchPageActivity.
 *
 * Acceptance Criteria covered:
 * 1: Search form is accessible with all expected fields and a Search button.
 * 2: User can enter a keyword and press Search.
 * 3: Empty search submits successfully (returns all jobs).
 */
@RunWith(AndroidJUnit4.class)
public class JobSearchPageUiTest {

    /**
     * AC 1: Search form is accessible
     * Given the search page is open,
     * Then the job title field, salary fields, duration field, vicinity field,
     * and Search Jobs button are all visible.
     */
    @Test
    public void searchForm_allFieldsAreVisible() {
        try (ActivityScenario<JobSearchPageActivity> scenario =
                     ActivityScenario.launch(JobSearchPageActivity.class)) {

            onView(withId(R.id.etJobTitle)).check(matches(isDisplayed()));
            onView(withId(R.id.etMinSalary)).check(matches(isDisplayed()));
            onView(withId(R.id.etMaxSalary)).check(matches(isDisplayed()));
            onView(withId(R.id.etMaxDuration)).check(matches(isDisplayed()));
            onView(withId(R.id.etVicinityKm)).check(matches(isDisplayed()));
            onView(withId(R.id.btnSearch)).check(matches(isDisplayed()));
        }
    }

    /**
     * AC 1: Search button shows correct label.
     */
    @Test
    public void searchButton_showsCorrectText() {
        try (ActivityScenario<JobSearchPageActivity> scenario =
                     ActivityScenario.launch(JobSearchPageActivity.class)) {

            onView(withId(R.id.btnSearch)).check(matches(withText("Search Jobs")));
        }
    }

    /**
     * AC 2: Keyword search submits and finishes the activity.
     * Given the search page is open,
     * When I type a keyword and press Search Jobs,
     * Then the activity finishes (result is returned to dashboard).
     */
    @Test
    public void searchWithKeyword_finishesActivity() {
        try (ActivityScenario<JobSearchPageActivity> scenario =
                     ActivityScenario.launch(JobSearchPageActivity.class)) {

            onView(withId(R.id.etJobTitle))
                    .perform(typeText("babysitting"), closeSoftKeyboard());

            onView(withId(R.id.btnSearch)).perform(click());

            scenario.onActivity(activity -> {
                org.junit.Assert.assertTrue(activity.isFinishing());
            });
        }
    }

    /**
     * AC 3: Empty search submits successfully.
     * Given the search page is open,
     * When I leave all fields empty and press Search Jobs,
     * Then the activity finishes without error (returns all jobs).
     */
    @Test
    public void emptySearch_finishesActivity() {
        try (ActivityScenario<JobSearchPageActivity> scenario =
                     ActivityScenario.launch(JobSearchPageActivity.class)) {

            onView(withId(R.id.btnSearch)).perform(click());

            scenario.onActivity(activity -> {
                org.junit.Assert.assertTrue(activity.isFinishing());
            });
        }
    }

    /**
     * AC 2: Search with salary filter submits and finishes.
     * Given the search page is open,
     * When I type a min and max salary and press Search Jobs,
     * Then the activity finishes (filter values are returned to dashboard).
     */
    @Test
    public void searchWithSalaryRange_finishesActivity() {
        try (ActivityScenario<JobSearchPageActivity> scenario =
                     ActivityScenario.launch(JobSearchPageActivity.class)) {

            onView(withId(R.id.etMinSalary))
                    .perform(typeText("10"), closeSoftKeyboard());
            onView(withId(R.id.etMaxSalary))
                    .perform(typeText("25"), closeSoftKeyboard());

            onView(withId(R.id.btnSearch)).perform(click());

            scenario.onActivity(activity -> {
                org.junit.Assert.assertTrue(activity.isFinishing());
            });
        }
    }
}
