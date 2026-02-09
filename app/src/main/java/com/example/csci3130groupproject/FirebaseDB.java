package com.example.csci3130groupproject;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB {
    FirebaseDatabase db;

    public FirebaseDB(String URL){
        db = FirebaseDatabase.getInstance(URL);
    }

    public boolean addUser(String name, String email, String password, String role){
        // if duplicate account cancel creation
        if(duplicate(name, email, password, role)){
            return true;
        }

        return false;
    }

    private boolean duplicate(String name, String email, String password, String role){
        // iterate through users to find duplicates
        return false;
    }
}
