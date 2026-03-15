package com.example.csci3130groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci3130groupproject.ui.MapBasedJobViewingActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI tests for the Map-Based Job Viewing feature.
 * These tests verify that important UI components are visible
 * when the activity launches.
 */
@RunWith(AndroidJUnit4.class)
public class MapBasedJobViewingUiTest {

    /**
     * Launches MapBasedJobViewingActivity before each test.
     */
    @Rule
    public ActivityScenarioRule<MapBasedJobViewingActivity> activityRule =
            new ActivityScenarioRule<>(MapBasedJobViewingActivity.class);

    /**
     * Acceptance Test: Map Display
     *
     * Given I open the map-based job viewing screen,
     * when the activity loads,
     * then the Google Map component should be visible.
     */
    @Test
    public void employeeCanViewJobLocationsOnMap() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    /**
     * Acceptance Test: Back Button Visibility
     *
     * Given I am on the map-based job viewing screen,
     * when the page is displayed,
     * then the "Back to Dashboard" button should be visible
     * so the user can return to the dashboard.
     */
    @Test
    public void backToDashboardButtonDisplays() {
        onView(withId(R.id.btnBackToDashboard)).check(matches(isDisplayed()));
    }
}