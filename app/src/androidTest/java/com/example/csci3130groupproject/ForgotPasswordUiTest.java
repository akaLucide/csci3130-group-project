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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ForgotPasswordUiTest {

    @Rule
    public ActivityScenarioRule<ForgotPasswordActivity> activityRule =
            new ActivityScenarioRule<>(ForgotPasswordActivity.class);

    @Test
    public void forgotPasswordScreenLoads() {
        onView(withText("Reset Password")).check(matches(isDisplayed()));
    }

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