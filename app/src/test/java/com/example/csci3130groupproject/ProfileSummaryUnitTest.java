package com.example.csci3130groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class ProfileSummaryUnitTest {

    ActivityController<ProfileActivity> controller;
    ProfileActivity activity;

    @Before
    public void setup() {
        controller = Robolectric.buildActivity(ProfileActivity.class).setup();
        activity = controller.get();
    }

    @Test
    public void showsNotAvailable_whenNotLoggedIn() {
        TextView tvName = activity.findViewById(R.id.tvName);
        TextView tvEmail = activity.findViewById(R.id.tvEmail);
        TextView tvRole = activity.findViewById(R.id.tvRole);

        assertEquals("Name: Not available", tvName.getText().toString());
        assertEquals("Email: Not available", tvEmail.getText().toString());
        assertEquals("Role: Not available", tvRole.getText().toString());
    }

    @Test
    public void backButton_finishesActivity() {
        Button btnBack = activity.findViewById(R.id.btnBackToDashboard);
        btnBack.performClick();

        assertTrue(activity.isFinishing());
    }
}