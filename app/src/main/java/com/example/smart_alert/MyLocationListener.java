package com.example.smart_alert;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyLocationListener implements LocationListener{
    static public String myLocation="unknown";
    Context context;
    public MyLocationListener(Context context){
        this.context=context;
    }
    public String getLocation(){
        return myLocation;
    }
    public void setMyLocation(String l){
        myLocation = l;
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        String Loca= "Log:" + String.valueOf(location.getLongitude())+" "+ "Lat:" + String.valueOf(location.getLatitude());
        setMyLocation(Loca);

    }

    @Override
    public void onFlushComplete(int requestCode) {
        //Toast.makeText(context,"Flush is complete",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
       // Toast.makeText(context,"Gps status is changed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
       // Toast.makeText(context,"GPS is enabled",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
       // Toast.makeText(context,"Gps is disabled",Toast.LENGTH_LONG).show();
    }
}
