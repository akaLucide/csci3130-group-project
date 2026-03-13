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

//UI test class for the Map-Based Job Viewing feature
//Uses espresso to verify that important UI elements are visible
@RunWith(AndroidJUnit4.class)
public class MapBasedJobViewingUiTest {

    //Launches MapBasedJobViewingActivity before each test
    @Rule
    public ActivityScenarioRule<MapBasedJobViewingActivity> activityRule =
            new ActivityScenarioRule<>(MapBasedJobViewingActivity.class);

    //Verifies that the Google Map component is displayed when the map-based job viewing screen opens
    @Test
    public void employeeCanViewJobLocationsOnMap() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    //Verifies that the "Back to Dashboard" button exists and is visible on the screen so users can return
    @Test
    public void backToDashboardButtonDisplays() {
        onView(withId(R.id.btnBackToDashboard)).check(matches(isDisplayed()));
    }
}