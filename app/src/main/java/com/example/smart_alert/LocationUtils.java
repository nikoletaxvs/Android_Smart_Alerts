package com.example.smart_alert;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class LocationUtils {
    public  String trackLocation(String email,String subscribed, DatabaseReference mDatabaseRef,Context context,LocationManager locationManager){
        String location =getLocation( context, locationManager);
        if(location == "unknown"){
            return "unknown";
        }else{
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                System.out.println("Fetching FCM registration token failed "+  task.getException());

                            }else{
                                // Get new FCM registration token
                                String token = task.getResult();
                                String[] username = email.split("@"); //the username -(@gmail.com) will be passed, this arg is unique

                                  HashMap<String,Object> hashMap = new HashMap<>();
                                  hashMap.put("email",email);
                                  hashMap.put("location",location);
                                  hashMap.put("subscribed",Boolean.valueOf(subscribed));
                                  hashMap.put("token",token);

                                  UpdateUserLocation update = new UpdateUserLocation();
                                  update.updateValueInRealtimeDb(username[0],hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void unused) {

                                      }
                                  }).addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {

                                      }
                                  });

                            }


                        }
                    });
            return location;
        }
    }
public  boolean trackLocation2(String email, DatabaseReference mDatabaseRef,Context context,LocationManager locationManager){
        String location =getLocation( context, locationManager);
        if(location == "unknown"){
            return false;
        }else{
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                System.out.println("Fetching FCM registration token failed "+  task.getException());

                            }else{
                                // Get new FCM registration token
                                String token = task.getResult();
                                String[] username = email.split("@"); //the username -(@gmail.com) will be passed, this arg is unique

                                //loop through "userlocations" and get user's data
                                Civilian civilian = new Civilian(email,location,false,token);
                                // Civilian civilian = c.findUserByEmail(mDatabaseRef,email);
                                //update token
                              // civilian.setToken(token);
                                //update location
                             //   civilian.setLocation(location);
                              //  civilian.setSubscribed(false);

                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("email",email);
                                hashMap.put("location",location);
                                hashMap.put("subscribed",true);
                                hashMap.put("token",token);

                                UpdateUserLocation update = new UpdateUserLocation();
                                update.updateValueInRealtimeDb(username[0],hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                                //  mDatabaseRef.child(username[0]).setValue(civilian);
                                //mDatabaseRef.child("nikoletaxvs").child("Subscribed").setValue(false);
                            }
                        }
                    });
            return true;
        }
    }

    public String getLocation(Context context,LocationManager locationManager) {

        MyLocationListener myLocationListener = new MyLocationListener(context);
        //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context,"Enable GPS Permission",Toast.LENGTH_LONG).show();
            return "unknown";
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            return myLocationListener.getLocation();

        }

    }
    public boolean checkLocation(Context context,LocationManager locationManager){
        if(getLocation(context,locationManager) == "unknown"){
            Toast.makeText(context, "Location not found,try again later",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}

