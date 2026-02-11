package com.example.csci3130groupproject;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

public class LogoutHelper {

    // replace 0 with the actual logout button resource ID e.g. R.id.btnLogout
    private static final int LOGOUT_BUTTON_ID = 0;

    public static void setupLogoutButton(Activity activity, FirebaseDB database) {
        Button logoutButton = activity.findViewById(LOGOUT_BUTTON_ID);
        if (logoutButton == null) {
            return; // no logout button in this layout, skip safely
        }
        logoutButton.setOnClickListener(v -> performLogout(activity, database));
    }

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
