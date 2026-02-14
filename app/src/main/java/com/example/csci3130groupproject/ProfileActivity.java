package com.example.csci3130groupproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final String DB_URL = "https://csci3130groupproject-c46e6-default-rtdb.firebaseio.com/";

    private TextView tvName, tvEmail, tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnBack = findViewById(R.id.btnBackToDashboard);

        btnBack.setOnClickListener(v -> {
            finish();  // closes ProfileActivity and returns to previous screen
        });

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);

        loadProfileSummary();
    }

    private void loadProfileSummary() {
        // Show placeholders while loading
        tvName.setText("Name: Loading...");
        tvEmail.setText("Email: Loading...");
        tvRole.setText("Role: Loading...");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            tvName.setText("Name: Not available");
            tvEmail.setText("Email: Not available");
            tvRole.setText("Role: Not available");
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();
        String authEmail = currentUser.getEmail();

        // Email can be shown from Auth immediately (if available)
        tvEmail.setText("Email: " + (authEmail != null ? authEmail : "Not available"));

        DatabaseReference userRef = FirebaseDatabase.getInstance(DB_URL)
                .getReference("users")
                .child(uid);

        // Read once (basic summary)
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    tvName.setText("Name: Not available");
                    tvRole.setText("Role: Not available");
                    // keep tvEmail as whatever we already set from Auth
                    Toast.makeText(ProfileActivity.this, "Profile not found in database", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = snapshot.child("name").getValue(String.class);
                String role = snapshot.child("role").getValue(String.class);

                // If you also store email in DB, you can prefer it when Auth email is null:
                String dbEmail = snapshot.child("email").getValue(String.class);

                tvName.setText("Name: " + (name != null && !name.isEmpty() ? name : "Not available"));
                tvRole.setText("Role: " + (role != null && !role.isEmpty() ? role : "Not available"));

                if ((authEmail == null || authEmail.isEmpty()) && dbEmail != null && !dbEmail.isEmpty()) {
                    tvEmail.setText("Email: " + dbEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvName.setText("Name: Not available");
                tvRole.setText("Role: Not available");
                Toast.makeText(ProfileActivity.this, "DB error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}