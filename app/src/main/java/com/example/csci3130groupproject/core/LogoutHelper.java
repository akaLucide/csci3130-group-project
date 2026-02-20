package com.example.csci3130groupproject.core;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.data.FirebaseDB;
import com.example.csci3130groupproject.ui.LoginActivity;

/**
 * Centralized logout helper.
 * Any activity with a R.id.btnLogout button can call setupLogoutButton(activity)
 * to wire up logout in one line.
 */
public class LogoutHelper {

    /**
     * Finds the logout button (R.id.btnLogout) in the given activity's layout.
     * Usage: call LogoutHelper.setupLogoutButton(this) in any activity's onCreate.
     */
    public static void setupLogoutButton(Activity activity) {
        Button logoutButton = activity.findViewById(R.id.btnLogout);
        if (logoutButton == null) {
            return; //Skips if no logout button in this layout
        }
        FirebaseDB database = new FirebaseDB(
                activity.getResources().getString(R.string.FIREBASE_DB_URL));
        logoutButton.setOnClickListener(v -> performLogout(activity, database));
    }

    /**
    * Signs out the user, shows a confirmation toast, and redirects to LoginActivity.
    * Usage: called internally by setupLogoutButton or directly from any activity.
    */
    public static void performLogout(Activity activity, FirebaseDB database) {
        // Sign out using FirebaseDB wrapper
        database.signOutUser();

        Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to login screen and clear the back stack so user can't navigate back
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
