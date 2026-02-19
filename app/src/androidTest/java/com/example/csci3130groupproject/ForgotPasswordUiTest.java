package com.example.csci3130groupproject;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci3130groupproject.ui.ForgotPasswordActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

// UI tests for Forgot Password screen
@RunWith(AndroidJUnit4.class)
public class ForgotPasswordUiTest {

    // Launches the activity before each test
    @Rule
    public ActivityScenarioRule<ForgotPasswordActivity> activityRule =
            new ActivityScenarioRule<>(ForgotPasswordActivity.class);

    // Verifies the screen loads correctly
    @Test
    public void forgotPasswordScreenLoads() {
        onView(withText("Reset Password")).check(matches(isDisplayed()));
    }

    // Verifies invalid email shows error message
    @Test
    public void invalidEmailShowsErrorMessage() {
        onView(withId(R.id.emailInput))
                .perform(typeText("invalidemail"));

        onView(withId(R.id.resetPasswordButton))
                .perform(click());

        onView(withText("Please enter a valid email address."))
                .check(matches(isDisplayed()));
    }
}