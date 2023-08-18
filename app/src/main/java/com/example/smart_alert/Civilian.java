package com.example.smart_alert;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Civilian {
    private String email;
    private String location;
    private Boolean subscribed;
    private String token;
    public Civilian() {
    }

    public Civilian(String email, String location, Boolean subscribed,String token) {
        this.email = email;
        this.location = location;
        this.subscribed = subscribed;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Civilian findUserByEmail( String email) {
        String[] username = email.split("@");
        GetUserLocations userLocations = new GetUserLocations();
        ArrayList<Civilian> civilians = userLocations.getObjects();

        //loop through realtime database
        for (Civilian c:civilians) {
            if(c.getEmail().contains(email)){
                return c;
            }
        }
        return null;
    }

}
