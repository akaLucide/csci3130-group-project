package com.example.csci3130groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ApplicationReviewUITest {

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

        // login as example employer bob
        UiObject goToLogin = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/goToLoginButton"));
        goToLogin.clickAndWaitForNewWindow();
        UiObject email = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginEmailEditText"));
        email.setText("bob@gmail.com");
        UiObject password = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginPasswordEditText"));
        password.setText("password");
        UiObject login = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginButton"));
        login.clickAndWaitForNewWindow();
    }

    @Test
    public void applicationPageVisible() throws UiObjectNotFoundException{
        UiObject applicantsBtn = device.findObject(new UiSelector().description("Babysitting - 2026-03-11-applicants"));
        applicantsBtn.clickAndWaitForNewWindow();
        UiObject backbtn = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/backToEmployerDashboardBtn"));
        assertTrue(backbtn.exists());
    }

    @Test
    public void applicantsExist() throws UiObjectNotFoundException, InterruptedException {
        UiObject applicantsBtn = device.findObject(new UiSelector().description("Babysitting - 2026-03-11-applicants"));
        applicantsBtn.clickAndWaitForNewWindow();
        Thread.sleep(3000);
        UiObject numApplicants = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/numApplicantsTextView"));
        assertEquals("1 applicants", numApplicants.getText());
    }

    @Test
    public void noApplications() throws UiObjectNotFoundException{
        UiObject applicantsBtn = device.findObject(new UiSelector().description("Mowing the lawn - 2026-03-11-applicants"));
        applicantsBtn.clickAndWaitForNewWindow();
        UiObject numApplicants = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/numApplicantsTextView"));
        assertEquals("0 applicants", numApplicants.getText());
    }
}