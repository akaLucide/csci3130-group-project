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
public class LoginUIAutomatorTest {

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

        // Navigate to login page from signup page
        UiObject goToLoginButton = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/goToLoginButton"));
        if (goToLoginButton.exists()) {
            goToLoginButton.click();
            device.waitForIdle(1000);
        }
    }

    /**
     * Acceptance Test 1: Empty Fields
     * Given I am on the login screen,
     * When I submit with missing email/username or password,
     * Then I see a clear error message and login does not proceed.
     */
    @Test
    public void testEmptyFields() throws UiObjectNotFoundException {
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
     * Acceptance Test 2: Invalid Credentials
     * Given I enter incorrect email/username or password,
     * When I press login,
     * Then I see a clear error message and remain on the login screen.
     */
    @Test
    public void testInvalidCredentials() throws UiObjectNotFoundException {
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
     * Acceptance Test 3: Successful Login
     * Given I enter correct credentials,
     * When I press login,
     * Then I am authenticated successfully.
     *
     * Note: Using initial user created with sign in logic
     */
    @Test
    public void testSuccessfulLogin() throws UiObjectNotFoundException {
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
}