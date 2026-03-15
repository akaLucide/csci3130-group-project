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

import android.content.Intent;
import com.google.android.gms.maps.model.Marker;

import android.widget.Button;

/**
 * Activity responsible for displaying available job postings on a Google Map.
 *
 * This activity retrieves jobs from Firebase and converts their address
 * into map coordinates using a Geocoder. Each job is displayed as a marker.
 * When a marker is clicked, the JobDetailsActivity opens.
 */
public class MapBasedJobViewingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBasedJobViewingBinding binding;
    private FirebaseCRUD crud;

    /**
     * Initializes the activity, loads the layout, sets up the back button,
     * and prepares the Google Map fragment.
     *
     * @param savedInstanceState saved instance state provided by Android
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflate layout using ViewBinding
        binding = ActivityMapBasedJobViewingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Button allowing the employee to return back to the dashboard
        Button btnBack = findViewById(R.id.btnBackToDashboard);

        //Finish activity to return to EmployeeDashboardActivity
        btnBack.setOnClickListener(v -> {
            finish();
        });

        //Firebase helper used to retrieve job data
        crud = new FirebaseCRUD();

        //Get the mao fragment and register callback for when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Called when the Google Map is fully initialized.
     * Sets up marker click behavior and loads job markers from Firebase.
     *
     * @param googleMap the GoogleMap instance that is ready to be used
     */    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //When a marker is clicked, open the job details page
        //the marker contains the job object stored in its tag
        mMap.setOnMarkerClickListener(marker -> {
            Job clickedJob = (Job) marker.getTag();

            //if the marker has an associated job, open the job details activity
            if (clickedJob != null) {

                //pass job information through the intent so the next screen can display it
                Intent intent = new Intent(MapBasedJobViewingActivity.this, JobDetailsActivity.class);
                intent.putExtra("title", clickedJob.title);
                intent.putExtra("category", clickedJob.category);
                intent.putExtra("description", clickedJob.description);
                intent.putExtra("locationAddress", clickedJob.locationAddress);
                intent.putExtra("salaryPerHour", clickedJob.salaryPerHour);
                intent.putExtra("expectedDurationHours", clickedJob.expectedDurationHours);
                intent.putExtra("urgency", clickedJob.urgency);
                intent.putExtra("date", clickedJob.date);
                startActivity(intent);
            }

            //returns true if the click even was handled
            return true;
        });

        //create a filter object (currently no filtering applied)
        JobSearchFilter filter = new JobSearchFilter();

        //retrieve jobs from Firebase database
        crud.searchJobs(filter, new FirebaseCRUD.JobsCallback() {

            /**
             * Called when jobs are successfully retrieved from Firebase.
             *
             * @param jobs list of jobs retrieved from the database
             */            @Override
            public void onSuccess(List<Job> jobs) {
                addJobMarkers(jobs);
            }

            /**
             * Called when an error occurs while retrieving jobs.
             *
             * @param message error message returned from Firebase
             */
            @Override
            public void onError(String message) {
                LatLng halifax = new LatLng(44.6488, -63.5752);
                mMap.addMarker(new MarkerOptions().position(halifax).title("Error loading jobs"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
            }
        });
    }

    /**
     * Converts a list of job objects into markers on the map using their
     * location addresses.
     *
     * @param jobs list of jobs retrieved from Firebase
     */
    private void addJobMarkers(List<Job> jobs) {

        //if no jobs exist, show default message marker in Halifax
        if (jobs.isEmpty()) {
            LatLng halifax = new LatLng(44.6488, -63.5752);
            mMap.addMarker(new MarkerOptions().position(halifax).title("No jobs found"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
            return;
        }

        //geocoder converts a textual address into latitude/longitude
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        boolean movedCamera = false;

        for (Job job : jobs) {
            //skip jobs that do not have a valid address
            if (job.locationAddress == null || job.locationAddress.trim().isEmpty()) {
                continue;
            }

            try {
                //convert the address string into geographic coordinates
                List<Address> addresses = geocoder.getFromLocationName(job.locationAddress, 1);

                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);

                    //create map coordiante for job location
                    LatLng jobLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                    //use job category as marker title if available
                    String title = (job.category != null && !job.category.isEmpty()) ? job.category : "Job";                    String snippet = job.locationAddress;

                    //create marker on the map
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(jobLatLng)
                            .title(title)
                            .snippet(snippet));

                    //store job object inside marker so we can access it when clicked
                    if (marker != null) {
                        marker.setTag(job);
                    }

                    //move camera to first valid job location
                    if (!movedCamera) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jobLatLng, 11f));
                        movedCamera = true;
                    }
                }
            } catch (IOException e) {
                //print error if geocoding fails
                e.printStackTrace();
            }
        }

        //if no valid addresses were found, center map on halifax
        if (!movedCamera) {
            LatLng halifax = new LatLng(44.6488, -63.5752);
            mMap.addMarker(new MarkerOptions().position(halifax).title("No valid job locations found"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halifax, 11f));
        }
    }
}
