package com.example.csci3130groupproject;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

        // login as example employee tom
        UiObject goToLogin = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/goToLoginButton"));
        goToLogin.clickAndWaitForNewWindow();
        UiObject email = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginEmailEditText"));
        email.setText("tom@gmail.com");
        UiObject password = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginPasswordEditText"));
        password.setText("password");
        UiObject login = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginButton"));
        login.clickAndWaitForNewWindow();

        // Wait for employee dashboard and jobs to fully load from Firebase
        device.wait(Until.findObject(By.res(launcherPackage, "layoutPostedJobs")), 6000);
        device.wait(Until.findObject(By.text("Apply")), 8000);
    }

    /**
     * Acceptance Test 1: Job Page Navigation
     * Given I am on the employee dashboard,
     * when I am authenticated and the firebase loads,
     * then I should see all recent posted available jobs listed.
     */
    @Test
    public void testJobPageNavigation() throws UiObjectNotFoundException {
        // Verify jobs list is visible and at least one job with an Apply button is shown
        UiObject jobsList = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/layoutPostedJobs"));
        assertTrue("Employee dashboard should show the jobs list", jobsList.exists());
        UiObject applyButton = device.findObject(new UiSelector().text("Apply"));
        assertTrue("At least one job with an Apply button should be visible", applyButton.exists());
    }

    /**
     * Acceptance Test 2: Job Details Navigation
     * Given I am on the available jobs page,
     * when I select a job,
     * then I am taken to the job details view of that job.
     */
    @Test
    public void testJobDetailsNavigation() throws UiObjectNotFoundException {
        // Click Details on the first job
        UiObject detailsButton = device.findObject(new UiSelector().text("Details"));
        assertTrue("A Details button should be visible on the dashboard", detailsButton.exists());
        detailsButton.click();
        device.waitForIdle(2000);

        // Verify we navigated away from the dashboard to JobDetailsActivity
        UiObject jobsList = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/layoutPostedJobs"));
        assertFalse("Should have navigated away from the dashboard to job details", jobsList.exists());
    }

    /**
     * Acceptance Test 3: Job Application Navigation
     * Given I am on the employee dashboard,
     * when I select apply to a job,
     * then I am taken to a custom screen to attach cover letter and resume and submit my application.
     */
    @Test
    public void testJobApplicationNavigation() throws UiObjectNotFoundException {
        // Click Apply on a job the user has not yet applied to
        UiObject applyButton = device.findObject(new UiSelector().text("Apply"));
        assertTrue("An Apply button should be visible on the dashboard", applyButton.exists());
        applyButton.click();
        device.waitForIdle(3000);

        // Verify we landed on JobSubmissionActivity with the resume picker visible
        UiObject pickResumeButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnPickResume"));
        assertTrue("Should be on the job application screen with a resume upload button", pickResumeButton.exists());
    }

    /**
     * Acceptance Test 4: Required Files are Attached
     * Given I am on the application screen,
     * when I select apply without attaching a resume,
     * then the app checks that a resume is attached before allowing the application to submit.
     */
    @Test
    public void testRequiredFilesAttached() throws UiObjectNotFoundException {
        // Navigate to JobSubmissionActivity
        UiObject applyButton = device.findObject(new UiSelector().text("Apply"));
        applyButton.click();
        device.waitForIdle(3000);

        // Try to submit without attaching a resume
        UiObject submitButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnSubmit"));
        assertTrue("Submit button should be visible", submitButton.exists());
        submitButton.click();
        device.waitForIdle(1000);

        // Should remain on the submission screen since no resume was attached
        UiObject pickResumeButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnPickResume"));
        assertTrue("Should remain on the application screen when no resume is attached", pickResumeButton.exists());
    }

    /**
     * Acceptance Test 5: No Crash on Errors
     * Given the application fails (missing attachments),
     * when the app handles it,
     * then the app does not crash and shows a readable message.
     */
    @Test
    public void testSubmissionFailure_DoesNotCrash() throws UiObjectNotFoundException {
        // Navigate to JobSubmissionActivity
        UiObject applyButton = device.findObject(new UiSelector().text("Apply"));
        applyButton.click();
        device.waitForIdle(3000);

        // Attempt to submit with no resume attached multiple times
        UiObject submitButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnSubmit"));
        submitButton.click();
        device.waitForIdle(1000);
        submitButton.click();
        device.waitForIdle(1000);

        // App should not crash — submission screen should still be fully visible
        UiObject pickResumeButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnPickResume"));
        assertTrue("App should not crash - resume picker should still be visible", pickResumeButton.exists());
        assertTrue("App should not crash - submit button should still be visible", submitButton.exists());
    }

    /**
     * Acceptance Test 6: Job Application Duplicates
     * Given I am on the employee dashboard,
     * when I select apply to a job I have already submitted an application for,
     * then I remain on the dashboard and am given a toast notification saying I have already applied for this job.
     */
    @Test
    public void testDuplicateApplicationBlocked() throws UiObjectNotFoundException {
        // Click Apply on the already-applied job (tom@gmail.com must have already applied to the first job)
        UiObject applyButton = device.findObject(new UiSelector().text("Apply"));
        assertTrue("An Apply button should be visible on the dashboard", applyButton.exists());
        applyButton.click();
        device.waitForIdle(3000);

        // Should remain on the dashboard — JobSubmissionActivity should NOT have opened
        UiObject jobsList = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/layoutPostedJobs"));
        assertTrue("Should remain on dashboard when already applied", jobsList.exists());
    }
}