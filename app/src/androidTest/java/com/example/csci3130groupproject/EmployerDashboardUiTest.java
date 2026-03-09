package com.example.csci3130groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci3130groupproject.ui.EmployerDashboardActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EmployerDashboardUiTest {

    @Test
    public void employerDashboard_loadsSuccessfully() {
        ActivityScenario.launch(EmployerDashboardActivity.class);

        onView(withId(R.id.spJobCategory)).check(matches(isDisplayed()));
        onView(withId(R.id.spUrgency)).check(matches(isDisplayed()));
        onView(withId(R.id.btnPickDate)).check(matches(isDisplayed()));
        onView(withId(R.id.etJobDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.btnPostJob)).check(matches(isDisplayed()));
        onView(withId(R.id.tvSelectedDate)).check(matches(withText("No date selected")));
    }
}