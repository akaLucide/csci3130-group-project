package com.example.csci3130groupproject;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

public class FallbackEspressoTest {
    @Test
    public void invalidRole_redirectsToLogin() {
        Intent i = new Intent(ApplicationProvider.getApplicationContext(), RoleLandingActivity.class);
        i.putExtra("ROLE", "Admin"); // invalid

        ActivityScenario.launch(i);

        intended(hasComponent(LoginActivity.class.getName()));
    }

}
