package com.example.csci3130groupproject;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
    }
}
