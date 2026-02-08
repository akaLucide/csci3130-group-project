package com.example.csci3130groupproject;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB {
    FirebaseDatabase db;

    public FirebaseDB(String URL){
        db = FirebaseDatabase.getInstance(URL);
    }


}
