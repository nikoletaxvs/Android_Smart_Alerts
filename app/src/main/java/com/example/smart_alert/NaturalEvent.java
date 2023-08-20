package com.example.smart_alert;

import android.net.Uri;

public class NaturalEvent {
    //fields
    private String title;
    private String type;
    private String location;
    private String timestamp;
    private String imageUrl;
    //private int imageId;
    private boolean accepted =false; // will be set to true by an employee, if criteria are met
    private String score ="0";   // this score indicates how dangerous an event is

    // Constructors empty, without image, with image
    public NaturalEvent(){}


    public NaturalEvent(String title, String type, String location, String timestamp,String imageUrl){
        this.title = title;
        this.type = type;
        this.location = location;
        this. timestamp =timestamp;
        this.imageUrl = imageUrl;
    }


    //Getters and setters foreach field

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


}
