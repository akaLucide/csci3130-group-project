package com.example.csci3130groupproject;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class LogoutHelper {

    public static void performLogout(Activity activity, FirebaseDB database) {
        // sign out using FirebaseDB wrapper
        database.signOutUser();

        Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // redirect to login screen and clear the back stack so user can't navigate back
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
