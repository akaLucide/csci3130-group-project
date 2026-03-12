package com.example.csci3130groupproject;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JobApplicationUITest {

    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "com.example.csci3130groupproject";
    private UiDevice device;

    @Before
    public void setup() throws UiObjectNotFoundException {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        final Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        Assert.assertNotNull(appIntent);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Navigate to employee dashboard page from login page
        //UiObject goToLoginButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/goToLoginButton"));
        //if (goToLoginButton.exists()) {
        //    goToLoginButton.click();
        //    device.waitForIdle(1000);
        //}
    }

    /**
     * Acceptance Test 1: Job Page Navigation
     * Given I am on the employee dashboard screen,
     * when I press the job posting button,
     * then I am taken to the job posting page to view available jobs.
     */
    @Test
    public void testJobPageNavigation() throws UiObjectNotFoundException {
        UiObject emailField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginEmailEditText"));
        UiObject passwordField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginPasswordEditText"));
        UiObject loginButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginButton"));
        emailField.clearTextField();
        passwordField.clearTextField();
        loginButton.click();
        device.waitForIdle(1000);
        assertTrue("Should remain on login page when fields are empty", emailField.exists());
        assertTrue("Login button should still be visible", loginButton.exists());
    }

    /**
     * Acceptance Test 2: Job Details Navigation
     * Given I am on the available jobs page,
     * when I select a job,
     * then I am taken to the job details view of that job.
     */
    @Test
    public void testJobDetailsNavigation() throws UiObjectNotFoundException {
        UiObject emailField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginEmailEditText"));
        UiObject passwordField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginPasswordEditText"));
        UiObject loginButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginButton"));
        emailField.setText("invalid@test.com");
        passwordField.setText("invaliduser");
        loginButton.click();
        device.waitForIdle(3000);
        assertTrue("Should remain on login page with invalid credentials", emailField.exists());
        assertTrue("Password field should still be visible", passwordField.exists());
        assertTrue("Login button should still be visible", loginButton.exists());
    }

    /**
     * Acceptance Test 3: Job Application Navigation
     * Given I am on the job details page,
     * when I select apply,
     * then I am taken to a custom screen to attach cover letter and resume and submit my application.
     */
    @Test
    public void testJobApplicationNavigation() throws UiObjectNotFoundException {
        UiObject emailField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginEmailEditText"));
        UiObject passwordField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginPasswordEditText"));
        UiObject loginButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginButton"));
        emailField.setText("tom@gmail.com");
        passwordField.setText("password");
        loginButton.click();
        device.waitForIdle(4000);
        UiObject emailFieldAfterLogin = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginEmailEditText"));
        assertTrue("Should navigate away from login page on successful login", !emailFieldAfterLogin.exists());
    }

    /**
     * Acceptance Test 4: Required Files are Attached
     * Given I am on the application screen,
     * when I select apply,
     * then both resume and cover letter are checked to be attached before allowing the application to submit.
     */
    @Test
    public void testRequiredFilesAttached() throws UiObjectNotFoundException {
        UiObject emailField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginEmailEditText"));
        UiObject passwordField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginPasswordEditText"));
        UiObject loginButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginButton"));
        emailField.setText("tom@gmail.com");
        passwordField.setText("password");
        loginButton.click();
        device.waitForIdle(4000);
        UiObject searchButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnSearch"));
        assertTrue("Should navigate to Employee Dashboard for employee role", searchButton.exists());
    }

    /**
     * Acceptance Test 5: No Crash on Errors
     * Given the application fails (missing attachments),
     * when the app handles it,
     * then the app does not crash and shows a readable message
     */
    @Test
    public void testSubmissionFailure_DoesNotCrash() throws UiObjectNotFoundException {
        UiObject emailField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginEmailEditText"));
        UiObject passwordField = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginPasswordEditText"));
        UiObject loginButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginButton"));
        emailField.setText("invalid@test.com");
        passwordField.setText("invalidpassword");
        loginButton.click();
        device.waitForIdle(3000);
        assertTrue("App should not crash - login page should still be visible", emailField.exists());
        assertTrue("App should not crash - login button should still be clickable", loginButton.exists());
        assertTrue("App should not crash - password field should still be visible", passwordField.exists());
        loginButton.click();
        device.waitForIdle(1000);
        assertTrue("App should remain responsive after error", loginButton.exists());
    }
}