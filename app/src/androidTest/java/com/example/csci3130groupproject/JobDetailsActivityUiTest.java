package com.example.csci3130groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.containsString;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci3130groupproject.ui.JobDetailsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JobDetailsActivityUiTest {

    @Test
    public void jobDetailsScreen_loadsAndShowsPassedData() {
        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                JobDetailsActivity.class
        );
        intent.putExtra("title", "Dog Walker");
        intent.putExtra("category", "Pet Care");
        intent.putExtra("description", "Walk dog for one hour");
        intent.putExtra("locationAddress", "456 Park Ave");
        intent.putExtra("salaryPerHour", 15.0);
        intent.putExtra("expectedDurationHours", 1.0);
        intent.putExtra("urgency", "Low");
        intent.putExtra("date", "2026-03-16");

        try (ActivityScenario<JobDetailsActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.tvJobDetails)).check(matches(isDisplayed()));
            onView(withId(R.id.tvJobDetails)).check(matches(withTextContains("Title: Dog Walker")));
            onView(withId(R.id.tvJobDetails)).check(matches(withTextContains("Category: Pet Care")));
            onView(withId(R.id.tvJobDetails)).check(matches(withTextContains("Description: Walk dog for one hour")));
            onView(withId(R.id.tvJobDetails)).check(matches(withTextContains("Location: 456 Park Ave")));
            onView(withId(R.id.tvJobDetails)).check(matches(withTextContains("Salary/hr: $15.0")));
            onView(withId(R.id.tvJobDetails)).check(matches(withTextContains("Duration: 1.0 hours")));
            onView(withId(R.id.tvJobDetails)).check(matches(withTextContains("Urgency: Low")));
            onView(withId(R.id.tvJobDetails)).check(matches(withTextContains("Date: 2026-03-16")));
        }
    }

    @Test
    public void backButton_finishesActivity() {
        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                JobDetailsActivity.class
        );

        try (ActivityScenario<JobDetailsActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.btnBackToResults)).perform(click());

            scenario.onActivity(activity -> {
                org.junit.Assert.assertTrue(activity.isFinishing());
            });
        }
    }

    private static org.hamcrest.Matcher<android.view.View> withTextContains(String text) {
        return androidx.test.espresso.matcher.ViewMatchers.withText(containsString(text));
    }
}