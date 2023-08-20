package com.example.smart_alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.backendmodule.Score;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class ViewEvents extends AppCompatActivity {
    private ListView listView;
    private Button logout;
    private Button analytics;
    private ArrayList<NaturalEvent> naturalEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        //link with view
        listView = findViewById(R.id.listview1);
        logout = findViewById(R.id.logout2);
        analytics = findViewById(R.id.viewAnalyticsEmpl);

        naturalEvents = new ArrayList<>();

        ListAdapter listAdapter = new ListAdapter(getApplicationContext(),naturalEvents);
        listView.setAdapter(listAdapter);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        DatabaseReference reference = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("uploads");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                naturalEvents.clear();
                Hashtable<String, Integer> typeValues = new Hashtable<String, Integer>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NaturalEvent naturalEvent = dataSnapshot.getValue(NaturalEvent.class);
                    naturalEvents.add(naturalEvent);
                }
                Score s = new Score();

                // get score for types in a certain time range
                for (NaturalEvent n:naturalEvents) {

                  String n_timestamp =n.getTimestamp();
                  String n_type=n.getType();

                  typeValues= s.calculateScores(n_timestamp,n_type);
                }
                String[] elements ={"Fire","Tsunami","Earthquake","Tornado","Flood"};

                //score for same type and close by events
                for(NaturalEvent n:naturalEvents) {
                    for (String str : elements)
                        if (n.getType().startsWith(str)) {
                            n.setScore(String.valueOf(typeValues.get(str)));
                            for (NaturalEvent n2 : naturalEvents) {
                                if (n == n2) {
                                    continue;
                                }
                                if (n.getType().startsWith(n2.getType())) {
                                    if (s.isNearby(n.getLocation(), n2.getLocation())) {
                                        int temp = Integer.parseInt(n.getScore());
                                        n.setScore(String.valueOf(temp + 10));
                                    }
                                }
                            }
                        }

                }
               listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast
                        .makeText(ViewEvents.this,
                                "Error Loading Image",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent = new Intent(ViewEvents.this, EventDescription_Activity.class);
               intent.putExtra("Title",naturalEvents.get(i).getTitle());
               intent.putExtra("Type",naturalEvents.get(i).getType());
               intent.putExtra("Score",naturalEvents.get(i).getScore());
               intent.putExtra("EventLocation",naturalEvents.get(i).getLocation());
               intent.putExtra("ImageUrl",naturalEvents.get(i).getImageUrl());
               startActivity(intent);
            }
        });
        // logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ViewEvents.this, "Logged Out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ViewEvents.this, Start_Activity.class));
            }
        });
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEvents.this,Analytics_Activity.class);
                intent.putExtra("From","Employee");
                startActivity(intent);
            }
        });
    }

    /*private String getTimestamp() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(new Date());
    }*/
}