package com.example.csci3130groupproject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.Button;

import com.example.csci3130groupproject.data.FirebaseDB;
import com.example.csci3130groupproject.ui.EmployerDashboardActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class LogoutUnitTest {

    ActivityController<EmployerDashboardActivity> controller;
    EmployerDashboardActivity activity;

    @Before
    public void setup() {
        controller = Robolectric.buildActivity(EmployerDashboardActivity.class).setup();
        activity = controller.get();
    }

    // Verifies the logout button exists and is visible on the dashboard
    @Test
    public void logoutButtonIsVisible() {
        Button btnLogout = activity.findViewById(R.id.btnLogout);
        assertNotNull(btnLogout);
        assertTrue(btnLogout.getVisibility() == android.view.View.VISIBLE);
    }

    // Verifies clicking logout clears the session (user becomes null)
    @Test
    public void logoutClearsSession() {
        Button btnLogout = activity.findViewById(R.id.btnLogout);
        btnLogout.performClick();

        FirebaseDB database = new FirebaseDB(
                activity.getResources().getString(R.string.FIREBASE_DB_URL));
        assertTrue(database.getCurrentUser() == null);
    }

    // Verifies clicking logout shows the confirmation toast
    @Test
    public void logoutShowsToast() {
        Button btnLogout = activity.findViewById(R.id.btnLogout);
        btnLogout.performClick();

        String latestToast = ShadowToast.getTextOfLatestToast();
        assertTrue("Logged out successfully".equals(latestToast));
    }

    // Verifies clicking logout finishes the current activity (redirect away)
    @Test
    public void logoutFinishesActivity() {
        Button btnLogout = activity.findViewById(R.id.btnLogout);
        btnLogout.performClick();

        assertTrue(activity.isFinishing());
    }
}
