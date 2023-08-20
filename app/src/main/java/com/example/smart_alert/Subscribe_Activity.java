package com.example.smart_alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Subscribe_Activity extends AppCompatActivity {
    private Button subscribe;
    private Button back;
    private Button cancelSub;
    private String email;
    private DatabaseReference reference;
    private String firebaseRealtimeDatabaseInstance= "https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app";
    private SQLiteDatabase DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        subscribe = findViewById(R.id.subBtn);
        back = findViewById(R.id.backBtn);
        cancelSub = findViewById(R.id.cancelBtn);

        reference = FirebaseDatabase.getInstance(firebaseRealtimeDatabaseInstance).getReference().child("userlocations");
        //Create sqlLite
        DB = openOrCreateDatabase("Userdata.db", MODE_PRIVATE, null);
        DB.execSQL("Create table if not exists Userdetails(" +
                "email TEXT primary key," +
                "subscribed TEXT)");
        //get data from ViewEvents
        Intent intent = getIntent();
        email = intent.getStringExtra("UserEmail");

        //Toast.makeText(Subscribe_Activity.this, "Email is "+ email, Toast.LENGTH_SHORT).show();
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        changeSubscriptionStatus(true,email);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        cancelSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSubscriptionStatus(false,email);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(new Intent(Subscribe_Activity.this, HomePage.class));
                intent1.putExtra("Email",email);
                startActivity(intent1);
            }
        });
    }

    private void changeSubscriptionStatus(Boolean status,String email) {


        LocationUtils locationUtils = new LocationUtils();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        DbHelper dbHelper = new DbHelper(DB);
        dbHelper.updateLocalDB(email,status,Subscribe_Activity.this);
    }


}