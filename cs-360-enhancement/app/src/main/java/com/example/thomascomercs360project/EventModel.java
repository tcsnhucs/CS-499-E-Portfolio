package com.example.thomascomercs360project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventModel implements Comparable<EventModel> {
    private String id;
    private String title;
    private String description;
    private String date;
    private String time;
    private boolean persistent;
    private Date dateTime;

    public EventModel(String id, String title, String description, String date, String time, boolean persistent) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.persistent = persistent;
        this.dateTime = parseDateTime(date, time);
    }

    private Date parseDateTime(String date, String time) {
        SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy hh:mm a", Locale.getDefault());
        try {
            return format.parse(date + " " + time);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public boolean isPersistent() { return persistent; }
    public Date getDateTime() { return dateTime; }

    @Override
    public int compareTo(EventModel other) {
        if (this.dateTime == null || other.dateTime == null) return 0;
        return this.dateTime.compareTo(other.dateTime);
    }
}
