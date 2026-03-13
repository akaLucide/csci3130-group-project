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

@RunWith(AndroidJUnit4.class)
public class MapBasedJobViewingUiTest {

    @Rule
    public ActivityScenarioRule<MapBasedJobViewingActivity> activityRule =
            new ActivityScenarioRule<>(MapBasedJobViewingActivity.class);

    @Test
    public void employeeCanViewJobLocationsOnMap() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void backToDashboardButtonDisplays() {
        onView(withId(R.id.btnBackToDashboard)).check(matches(isDisplayed()));
    }
}