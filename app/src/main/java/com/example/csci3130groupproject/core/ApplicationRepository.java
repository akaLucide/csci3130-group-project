package com.example.csci3130groupproject.core;

import android.util.Base64;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository class responsible for handling job application submission logic.
 * Encodes resume files and persists applicant data to Firebase Realtime Database.
 */
public class ApplicationRepository {

    private static final String DB_URL = "https://csci3130groupproject-c46e6-default-rtdb.firebaseio.com/";

    /**
     * Callback interface for job application submission results.
     */
    public interface SubmissionCallback {
        /**
         * Called when the application was successfully submitted.
         */
        void onSuccess();

        /**
         * Called when the application submission failed.
         *
         * @param message A description of the error that occurred.
         */
        void onFailure(String message);
    }

    /**
     * Encodes raw PDF bytes into a Base64 string for storage in Firebase.
     *
     * @param pdfBytes The raw bytes of the PDF file.
     * @return A Base64-encoded string representation of the PDF.
     */
    public String encodeResume(byte[] pdfBytes) {
        return Base64.encodeToString(pdfBytes, Base64.DEFAULT);
    }

    /**
     * Fetches the user's profile from Firebase, then saves a full applicant entry
     * under the specified job's applicants node in the Realtime Database.
     *
     * @param jobId        The Firebase key of the job being applied to.
     * @param uid          The Firebase UID of the applicant.
     * @param base64Resume The Base64-encoded resume string to store.
     * @param callback     Callback to notify the caller of success or failure.
     */
    public void submitApplication(String jobId, String uid, String base64Resume, SubmissionCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance(DB_URL)
                .getReference("users")
                .child(uid);

        userRef.get().addOnSuccessListener(userSnap -> {
            String email = userSnap.child("email").getValue(String.class);
            String name = userSnap.child("name").getValue(String.class);
            String role = userSnap.child("role").getValue(String.class);

            DatabaseReference applicantRef = FirebaseDatabase.getInstance(DB_URL)
                    .getReference("jobs")
                    .child(jobId)
                    .child("applicants")
                    .child(uid);

            Map<String, String> applicantData = new HashMap<>();
            applicantData.put("email", email != null ? email : "");
            applicantData.put("name", name != null ? name : "");
            applicantData.put("role", role != null ? role : "");
            applicantData.put("resume", base64Resume);
            applicantData.put("appliedAt", String.valueOf(System.currentTimeMillis()));

            applicantRef.setValue(applicantData)
                    .addOnSuccessListener(unused -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));

        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}