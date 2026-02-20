package com.example.csci3130groupproject;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.csci3130groupproject.ui.EmployeeDashboardActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class EmployeeDashboardRedirectUiTest {

    @Test
    public void whenNotLoggedIn_redirectsToLogin() {
        init();
        try (ActivityScenario<EmployeeDashboardActivity> scenario =
                     ActivityScenario.launch(EmployeeDashboardActivity.class)) {

            intended(hasComponent("com.example.csci3130groupproject.ui.LoginActivity"));
        } finally {
            release();
        }
    }
}

