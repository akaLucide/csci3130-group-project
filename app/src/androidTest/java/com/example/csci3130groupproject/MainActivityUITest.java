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

public class MainActivityUITest {

    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "com.example.csci3130groupproject";
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        final Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        Assert.assertNotNull(appIntent);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkLandingPageIsVisible(){
        UiObject name = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/nameEditText"));
        assertTrue(name.exists());
        UiObject email = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/emailEditText"));
        assertTrue(email.exists());
        UiObject password = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/passwordEditText"));
        assertTrue(password.exists());
        UiObject confirm = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/confirmPasswordEditText"));
        assertTrue(confirm.exists());
        UiObject role = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/roleSpinner"));
        assertTrue(role.exists());
        UiObject signup = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/signupButton"));
        assertTrue(signup.exists());
    }

    @Test
    public void checkMovedToLogin() throws UiObjectNotFoundException {
        UiObject name = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/nameEditText"));
        name.setText("Name");
        UiObject email = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/emailEditText"));
        email.setText("NameUITest@email.com");
        UiObject password = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/passwordEditText"));
        password.setText("password");
        UiObject confirm = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/confirmPasswordEditText"));
        confirm.setText("password");
        UiObject role = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/roleSpinner"));
        role.click();
        device.wait(Until.hasObject(By.text("employee")), LAUNCH_TIMEOUT);
        device.findObject(By.text("employee")).click();
        UiObject signup = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/signupButton"));
        signup.clickAndWaitForNewWindow();
        UiObject login = device.findObject(new UiSelector().resourceId("com.example.csci3130groupproject:id/loginButton"));
        assertTrue(login.exists());
    }
}
