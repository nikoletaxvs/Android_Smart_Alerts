package com.example.smart_alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDescription_Activity extends AppCompatActivity {

    private ImageButton reject;
    private Button notifyUsers;
    private TextView titleTxt;
    private TextView typeTxt;
    private TextView scoreTxt;
    private ImageView photo;
    private String eventLocation;
    private ArrayList<Civilian> civilians;
    private ArrayList<NaturalEvent> naturalEvents;
    private  String title;
    private String eventType;
    private  String score;
    private DatabaseReference ref;
    private DatabaseReference civilianReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);

        // link to view
        reject = findViewById(R.id.rejectBtn);
        notifyUsers = findViewById(R.id.notifyBtn);
        titleTxt = findViewById(R.id.textTitle);
        typeTxt = findViewById(R.id.textType);
        scoreTxt = findViewById(R.id.textScore);
        photo = findViewById(R.id.imageView10);
        //initialize civilians array
        civilians = new ArrayList<>();
        naturalEvents = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = db.getReference("analytics");
        //if reject is clicked , employee goes back to event list
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventDescription_Activity.this, ViewEvents.class));
            }
        });

        //getting all civilians from Realtime Database in Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("userlocations");

        //get data from ViewEvents
        Intent intent = getIntent();

        title = intent.getStringExtra("Title");
        eventType = intent.getStringExtra("Type");
        score = intent.getStringExtra("Score");
        eventLocation = intent.getStringExtra("EventLocation");
        String imageUrl = intent.getStringExtra("ImageUrl");

        //Set values
        titleTxt.setText(title);
        typeTxt.setText(eventType);
        scoreTxt.setText(score);
        Picasso.with(this)
                .load(imageUrl)
                .fit()
                .centerCrop()
                .into(photo);

        notifyUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventLocation !="unknown") {
                    civilianReference = db.getReference("userlocations");
                    civilianReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Civilian civilian = dataSnapshot.getValue(Civilian.class);
                              notifyThem(eventLocation,civilian);
                              ref.child(String.valueOf(System.currentTimeMillis())).setValue(eventType);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    Toast.makeText(EventDescription_Activity.this, "L unknown", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void notifyThem(String location,Civilian c) {

        String title =typeTxt.getText().toString() + " near you!";
        String message=titleTxt.getText().toString();
        //Toast.makeText(this, String.valueOf(civilians.size()), Toast.LENGTH_SHORT).show();

        if(c.getSubscribed()==false){
            if(isNearby(c.getLocation())){
                    FCMSend.pushNotification(
                            EventDescription_Activity.this,
                            c.getToken(),
                            title,
                            message
                    );
              }
        }



    }
    // If the user's and event's locations are less than 200 km apart , the user will get notified
    private boolean isNearby(String location) {
        location = location.replace("Lat:" ,"");
        location = location.replace("Log:","");
        String[] locs = location.split(" ");
        eventLocation = eventLocation.replace("Lat:" ,"");
        eventLocation = eventLocation.replace("Log:","");
        String[] locsEvent = eventLocation.split(" ");

        if(locs[0] != null && locs[0] != null && locsEvent[0]!=null && locsEvent[1]!=null){
            Double lat = Double.parseDouble(locs[0]);
            Double log = Double.parseDouble(locs[1]);
            Double latEvent = Double.parseDouble(locsEvent[0]);
            Double logEvent = Double.parseDouble(locsEvent[1]);
            Double d = haversine_distance(lat,log,latEvent,logEvent);
            return d<200.0;
        }

      //  Toast.makeText(EventDescription_Activity.this, locs[0] + locs[1],Toast.LENGTH_SHORT).show();
        return false;
    }

    //nearby Users get notified
   /* private void notifyNearbyUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("userlocations");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   Civilian civilian = dataSnapshot.getValue(Civilian.class);
                    civilians.add(civilian);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    //The haversine formula determines the great-circle distance between two points
    // on a sphere given their longitudes and latitudes.
    // Important in navigation
    public double haversine_distance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371.01; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
    }
    /*private String evaluateScore() {
        int usersReporting =0;

        for (NaturalEvent badThing: naturalEvents) {
            usersReporting++;
        }

        return String.valueOf(usersReporting);
    }*/
}
