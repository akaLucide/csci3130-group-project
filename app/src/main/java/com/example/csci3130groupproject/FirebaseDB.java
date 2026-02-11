package com.example.csci3130groupproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB {
    FirebaseDatabase db;
    FirebaseAuth auth;

    public FirebaseDB(String URL){
        db = FirebaseDatabase.getInstance(URL);
        auth = FirebaseAuth.getInstance();
    }

    public void addUser(String name, String email, String password, String role, Context context){
        // create auth user in firebase, if successful store in db, on fail print toast
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( task -> {
            if (task.isSuccessful()) {
                Log.d("Successful", "auth completed");

                // get User id for current user
                FirebaseUser user = auth.getCurrentUser();
                String uid = user.getUid();

                // create user for db storage
                User you = new User(name, email, role);

                // store in db under users->(user id key) and print error/success
                db.getReference().child("users").child(uid).setValue(you).addOnCompleteListener( write ->{
                    if (write.isSuccessful()) {
                        Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "DB write failed", Toast.LENGTH_SHORT).show();
                        Log.e("DB write", "write failed", task.getException());
                    }
                });
            } else {
                Toast.makeText(context, "Account Authentification failed", Toast.LENGTH_SHORT).show();
                Log.e("AUTH", "createUser failed", task.getException());
            }
        });
    }

    public void loginUser(String email, String password, Context context) {
        // authenticate user with Firebase
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Successful", "login completed");

                // get current user
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Navigate to homescreen logic ----- HERE -----

                } else {
                    Toast.makeText(context, "Login failed - no user found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Login failed - invalid credentials", Toast.LENGTH_SHORT).show();
                Log.e("AUTH", "signIn failed", task.getException());
            }
        });
    }
}
