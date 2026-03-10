package com.example.csci3130groupproject.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
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
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);

        btnBack.setOnClickListener(v -> finish());

        loadProfileSummary();
    }

    private void loadProfileSummary() {
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

        tvEmail.setText("Email: " + (authEmail != null ? authEmail : "Not available"));

        DatabaseReference userRef = FirebaseDatabase.getInstance(DB_URL)
                .getReference("users")
                .child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    tvName.setText("Name: Not available");
                    tvRole.setText("Role: Not available");
                    Toast.makeText(ProfileActivity.this, "Profile not found in database", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = snapshot.child("name").getValue(String.class);
                String role = snapshot.child("role").getValue(String.class);
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