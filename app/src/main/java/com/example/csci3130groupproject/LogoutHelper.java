package com.example.csci3130groupproject;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

/**
 * Centralized logout helper.
 * Any activity with a R.id.btnLogout button can call setupLogoutButton(activity)
 * to wire up logout in one line. Activities without the button are safely skipped.
 */
public class LogoutHelper {

    /**
     * Finds the logout button (R.id.btnLogout) in the given activity's layout.
     * If found, wires it to perform logout on click.
     * If no logout button exists in the layout, this method does nothing.
     *
     * Usage: call LogoutHelper.setupLogoutButton(this) in any activity's onCreate.
     */
    public static void setupLogoutButton(Activity activity) {
        Button logoutButton = activity.findViewById(R.id.btnLogout);
        if (logoutButton == null) {
            return; // no logout button in this layout, skip safely
        }
        FirebaseDB database = new FirebaseDB(
                activity.getResources().getString(R.string.FIREBASE_DB_URL));
        logoutButton.setOnClickListener(v -> performLogout(activity, database));
    }

    /**
     * Signs out the user, shows a confirmation toast, and redirects to LoginActivity.
     * Clears the back stack so the user cannot navigate back after logging out.
     */
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
