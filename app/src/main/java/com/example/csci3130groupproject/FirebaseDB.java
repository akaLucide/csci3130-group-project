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
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( task -> { // could add activity cast context
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                String uid = user.getUid();
                if(uid == null){
                    Log.d("auth", "uid is null");
                }
                User you = new User(name, email, role);

                db.getReference().child("users").child(uid).setValue(you)
                        .addOnCompleteListener( write ->{
                            if (write.isSuccessful()) {
                                Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "DB write failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(context, "Account Authentification failed", Toast.LENGTH_SHORT).show();
                Log.d("AUTH", "createUser failed", task.getException());
            }
        });
    }
}
