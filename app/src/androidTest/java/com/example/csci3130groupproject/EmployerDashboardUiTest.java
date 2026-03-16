package com.example.csci3130groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci3130groupproject.ui.EmployerDashboardActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EmployerDashboardUiTest {

    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "com.example.csci3130groupproject";
    private UiDevice device;

    @Test
    public void employerDashboard_loadsSuccessfully() throws UiObjectNotFoundException {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        final Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        assert appIntent != null;
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // login as example employer bob
        UiObject goToLogin = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/goToLoginButton"));
        goToLogin.clickAndWaitForNewWindow();
        UiObject email = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginEmailEditText"));
        email.setText("bob@gmail.com");
        UiObject password = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginPasswordEditText"));
        password.setText("password");
        UiObject login = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginButton"));
        login.clickAndWaitForNewWindow();

        ActivityScenario.launch(EmployerDashboardActivity.class);
        
        // Wait for jobs to load from Firebase
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.etCategoryFilter)).check(matches(isDisplayed()));
        onView(withId(R.id.btnProfile)).check(matches(isDisplayed()));
        onView(withId(R.id.btnPostJob)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogout)).check(matches(isDisplayed()));
        onView(withId(R.id.btnApplyCategoryFilter)).check(matches(isDisplayed()));
        onView(withId(R.id.btnClearCategoryFilter)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutPostedJobs)).check(matches(isDisplayed()));
        onView(withId(R.id.tvPostedJobsTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.tvRole)).check(matches(isDisplayed()));
    }
}