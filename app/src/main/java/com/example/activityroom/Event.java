package com.example.activityroom;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
    private String id;
    private String title;
    private String description;
    private Date date;
    private String time;
    private String venue;
    private String imagePath; // Add this field

    public Event() {}

    public Event(String title, String description, Date date, String time, String venue) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.imagePath = ""; // Initialize as empty
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getDate() { return date; }
    public String getTime() { return time; }
    public String getVenue() { return venue; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
