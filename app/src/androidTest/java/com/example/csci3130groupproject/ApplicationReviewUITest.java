package com.example.csci3130groupproject;

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
        // redirect to a job/create a job with applicants on bob@gmail.com or other account for testing
    }

    @Test
    public void applicationsVisible() throws UiObjectNotFoundException{

    }

    @Test
    public void noApplications() {

    }

    @Test
    public void applicantDetailsVisible() {

    }
}