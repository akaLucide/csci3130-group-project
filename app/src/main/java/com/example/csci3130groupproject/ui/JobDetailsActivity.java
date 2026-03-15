package com.example.csci3130groupproject.ui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.util.JobDetailsFormatter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class JobDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvJobDetails;
    private Button btnBackToResults;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        tvJobDetails = findViewById(R.id.tvJobDetails);
        btnBackToResults = findViewById(R.id.btnBackToResults);

        // Reconstruct Job object from intent extras passed by the dashboard
        job = new Job();

        job.title = getIntent().getStringExtra("title");
        job.category = getIntent().getStringExtra("category");
        job.description = getIntent().getStringExtra("description");
        job.locationAddress = getIntent().getStringExtra("locationAddress");
        job.salaryPerHour = getIntent().getDoubleExtra("salaryPerHour", 0.0);
        job.expectedDurationHours = getIntent().getDoubleExtra("expectedDurationHours", 0.0);
        job.urgency = getIntent().getStringExtra("urgency");
        job.date = getIntent().getStringExtra("date");

        // Format and display all job details using the shared formatter
        tvJobDetails.setText(JobDetailsFormatter.format(job));

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.jobDetailsMap);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnBackToResults.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (job.locationAddress == null || job.locationAddress.trim().isEmpty()) {
            LatLng halifax = new LatLng(44.6488, -63.5752);
            googleMap.addMarker(new MarkerOptions().position(halifax).title("Location not provided"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocationName(job.locationAddress, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng jobLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                String markerTitle = (job.category != null && !job.category.isEmpty())
                        ? job.category
                        : "Job Location";

                googleMap.addMarker(new MarkerOptions()
                        .position(jobLatLng)
                        .title(markerTitle)
                        .snippet(job.locationAddress));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jobLatLng, 13f));
            } else {
                LatLng halifax = new LatLng(44.6488, -63.5752);
                googleMap.addMarker(new MarkerOptions().position(halifax).title("Location not found"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
            }
        } catch (IOException e) {
            LatLng halifax = new LatLng(44.6488, -63.5752);
            googleMap.addMarker(new MarkerOptions().position(halifax).title("Map error"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
        }
    }
}