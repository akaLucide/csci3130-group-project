package com.example.csci3130groupproject;

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

public class LogoutUITest {

    private static final int LAUNCH_TIMEOUT = 5000;
    private static final int LOGIN_WAIT = 8000;
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

        // Navigate to login screen
        UiObject goToLogin = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/goToLoginButton"));
        if (goToLogin.exists()) {
            goToLogin.clickAndWaitForNewWindow();
        }

        // Log in with test credentials
        UiObject email = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginEmailEditText"));
        email.setText("LucTest@gmail.com");
        UiObject password = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginPasswordEditText"));
        password.setText("TestTest123");
        UiObject loginBtn = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginButton"));
        loginBtn.clickAndWaitForNewWindow();

        // Wait for the dashboard to load
        device.wait(Until.hasObject(By.res(launcherPackage, "btnLogout")), LOGIN_WAIT);
    }

    // Verifies the logout button is visible on the employer dashboard
    @Test
    public void logoutButtonIsVisibleOnDashboard() {
        UiObject logoutBtn = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnLogout"));
        assertTrue(logoutBtn.exists());
    }

    // Verifies clicking logout redirects the user back to the login screen
    @Test
    public void logoutRedirectsToLoginScreen() throws UiObjectNotFoundException {
        UiObject logoutBtn = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnLogout"));
        logoutBtn.clickAndWaitForNewWindow();

        // After logout, the login screen should appear with the login button visible
        UiObject loginBtn = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/loginButton"));
        device.wait(Until.hasObject(By.res(launcherPackage, "loginButton")), LAUNCH_TIMEOUT);
        assertTrue(loginBtn.exists());
    }

    // Verifies that after logout, the dashboard is no longer accessible (logout button gone)
    @Test
    public void logoutClearsSessionAndDashboardNotAccessible() throws UiObjectNotFoundException {
        UiObject logoutBtn = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnLogout"));
        logoutBtn.clickAndWaitForNewWindow();

        // Wait for login screen
        device.wait(Until.hasObject(By.res(launcherPackage, "loginButton")), LAUNCH_TIMEOUT);

        // The logout button from the dashboard should no longer be visible
        UiObject dashboardLogout = device.findObject(new UiSelector().resourceId(launcherPackage + ":id/btnLogout"));
        assertTrue(!dashboardLogout.exists());
    }
}
