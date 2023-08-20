package com.example.smart_alert;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public interface IGetObjectsFromRealtimeDb<T> {
    public ArrayList<T> getObjects();
}