package com.example.csci3130groupproject;

import android.os.Bundle;
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

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);

        loadProfileSummary();
    }

    private void loadProfileSummary() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            tvName.setText("Name: Not available");
            tvEmail.setText("Email: Not available");
            tvRole.setText("Role: Not available");
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Email can come from Auth immediately
        tvEmail.setText("Email: " + (currentUser.getEmail() != null ? currentUser.getEmail() : "Not available"));

        String uid = currentUser.getUid();
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
                    Toast.makeText(ProfileActivity.this, "Profile not found in database", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = snapshot.getValue(User.class);
                if (user == null) {
                    tvName.setText("Name: Not available");
                    tvRole.setText("Role: Not available");
                    Toast.makeText(ProfileActivity.this, "Failed to parse profile data", Toast.LENGTH_SHORT).show();
                    return;
                }

                tvName.setText("Name: " + (user.name != null ? user.name : "Not available"));
                tvRole.setText("Role: " + (user.role != null ? user.role : "Not available"));
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