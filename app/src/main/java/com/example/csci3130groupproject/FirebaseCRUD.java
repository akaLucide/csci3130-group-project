package com.example.csci3130groupproject;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private final DatabaseReference jobsRef;
    private final JobMatcher matcher;

    public FirebaseCRUD() {
        jobsRef = FirebaseDatabase.getInstance().getReference().child("jobs");
        matcher = new JobMatcher();
    }

    public interface JobsCallback {
        void onSuccess(List<Job> jobs);
        void onError(String message);
    }

    public void searchJobs(@NonNull JobSearchFilter filter, @NonNull JobsCallback callback) {
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Job> matches = new ArrayList<>();

                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    Job job = jobSnap.getValue(Job.class);
                    if (job == null) continue;

                    if (matcher.matches(job, filter)) {
                        matches.add(job);
                    }
                }

                callback.onSuccess(matches);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("DB error: " + error.getMessage());
            }
        });
    }
}
