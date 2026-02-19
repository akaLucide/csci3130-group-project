package com.example.csci3130groupproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;

public class FirebaseDB {
    FirebaseDatabase db;
    FirebaseAuth auth;

    public FirebaseDB(String URL){
        db = FirebaseDatabase.getInstance(URL);
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Signs out the currently authenticated user.
     * Clears the Firebase Auth session so the user is no longer logged in.
     */
    public void signOutUser() {
        auth.signOut();
    }

    /**
     * Returns the currently signed-in FirebaseUser, or null if no user is logged in.
     */
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public Task<Void> addUser(String name, String email, String password, String role, Context context){
        // check if successful
        // create auth user in firebase, if successful store in db, on fail return boolean
        return auth.createUserWithEmailAndPassword(email, password).continueWithTask( task -> {

            if(!task.isSuccessful()) {
                Log.e("AUTH", "createUser failed", task.getException());
                throw task.getException();
            }

            Log.d("Successful", "auth completed");

            // get User id for current user
            FirebaseUser user = auth.getCurrentUser();
            String uid = user.getUid();

            // create user for db storage
            User you = new User(name, email, role);

            // store in db under users->(user id key) and print error/success
            return db.getReference().child("users").child(uid).setValue(you).addOnCompleteListener( write ->{
                if (write.isSuccessful()) {
                    Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "DB write failed", Toast.LENGTH_SHORT).show();
                    Log.e("DB write", "write failed", task.getException());
                }
            });
        });
    }

    public void loginUser(String email, String password, Context context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                String uid = user.getUid();

                // Fetch user role to navigate to correct dashboard
                db.getReference().child("users").child(uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        User userData = snapshot.getValue(User.class);

                        Intent intent;
                        if (userData.role.equals("employer")) {
                            intent = new Intent(context, EmployerDashboardActivity.class);
                        } else {
                            intent = new Intent(context, EmployeeDashboardActivity.class);
                        }

                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                    @Override
                    public void onCancelled(com.google.firebase.database.DatabaseError error) {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}