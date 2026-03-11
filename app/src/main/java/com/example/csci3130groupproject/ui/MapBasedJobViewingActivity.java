package com.example.csci3130groupproject.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.csci3130groupproject.R;
import com.example.csci3130groupproject.core.Job;
import com.example.csci3130groupproject.core.JobSearchFilter;
import com.example.csci3130groupproject.data.FirebaseCRUD;
import com.example.csci3130groupproject.databinding.ActivityMapBasedJobViewingBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapBasedJobViewingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBasedJobViewingBinding binding;
    private FirebaseCRUD crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBasedJobViewingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        crud = new FirebaseCRUD();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        JobSearchFilter filter = new JobSearchFilter();

        crud.searchJobs(filter, new FirebaseCRUD.JobsCallback() {
            @Override
            public void onSuccess(List<Job> jobs) {
                addJobMarkers(jobs);
            }

            @Override
            public void onError(String message) {
                LatLng halifax = new LatLng(44.6488, -63.5752);
                mMap.addMarker(new MarkerOptions().position(halifax).title("Error loading jobs"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
            }
        });
    }

    private void addJobMarkers(List<Job> jobs) {
        if (jobs.isEmpty()) {
            LatLng halifax = new LatLng(44.6488, -63.5752);
            mMap.addMarker(new MarkerOptions().position(halifax).title("No jobs found"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        boolean movedCamera = false;

        for (Job job : jobs) {
            if (job.locationAddress == null || job.locationAddress.trim().isEmpty()) {
                continue;
            }

            try {
                List<Address> addresses = geocoder.getFromLocationName(job.locationAddress, 1);

                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng jobLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String title = (job.title != null && !job.title.isEmpty()) ? job.title : "Job";
                    String snippet = job.locationAddress;

                    mMap.addMarker(new MarkerOptions()
                            .position(jobLatLng)
                            .title(title)
                            .snippet(snippet));

                    if (!movedCamera) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jobLatLng, 11f));
                        movedCamera = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!movedCamera) {
            LatLng halifax = new LatLng(44.6488, -63.5752);
            mMap.addMarker(new MarkerOptions().position(halifax).title("No valid job locations found"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
        }
    }
}