package com.example.csci3130groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci3130groupproject.ui.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileSummaryUiTest {

    // Ensures ProfileActivity goes into the "Not available" state
    @Before
    public void signOutToMakeTestDeterministic() {
        FirebaseAuth.getInstance().signOut();
    }

    @Rule
    public ActivityScenarioRule<ProfileActivity> activityRule =
            new ActivityScenarioRule<>(ProfileActivity.class);

    // Basic smoke checks: views exist on screen
    @Test
    public void profileSummaryScreenLoads() {
        onView(withId(R.id.tvName)).check(matches(isDisplayed()));
        onView(withId(R.id.tvEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.tvRole)).check(matches(isDisplayed()));
        onView(withId(R.id.btnBackToDashboard)).check(matches(isDisplayed()));
    }

    //Not Available when not logged in
    @Test
    public void showsNotAvailable_whenNotLoggedIn() {
        onView(withId(R.id.tvName)).check(matches(withText("Name: Not available")));
        onView(withId(R.id.tvEmail)).check(matches(withText("Email: Not available")));
        onView(withId(R.id.tvRole)).check(matches(withText("Role: Not available")));
    }

    // Verify the activity is finishing after clicking back
    @Test
    public void backButton_finishesActivity() {
        ActivityScenario<ProfileActivity> scenario = activityRule.getScenario();

        onView(withId(R.id.btnBackToDashboard)).perform(click());

        scenario.onActivity(activity -> {
            org.junit.Assert.assertTrue(activity.isFinishing());
        });
    }
}
