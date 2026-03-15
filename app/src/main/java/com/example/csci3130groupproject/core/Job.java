package com.example.csci3130groupproject.core;

// Data model representing a job posting stored in Firebase
public class Job {
    public String title;
    public String category;
    public double salaryPerHour;
    public double expectedDurationHours;
    public String urgency;
    public String date;
    public String description;
    public String locationAddress;
    public String employerId;
    public long createdAt;



    // No-arg constructor required for Firebase deserialization
    public Job() {}
}