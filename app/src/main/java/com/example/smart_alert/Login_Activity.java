package com.example.smart_alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class Login_Activity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private RadioButton radioButtonEmployee;
    private RadioButton radioButtonCitizen;
    private Button login;
    private Boolean employeeLogin= false;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseRef;
    private String firebaseRealtimeDatabaseInstance= "https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app";
    private SQLiteDatabase DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //link with view
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        radioButtonEmployee = findViewById(R.id.radioButton);
        radioButtonCitizen = findViewById(R.id.radioButton2);

        auth = FirebaseAuth.getInstance();
        //Create sqlLite
        DB = openOrCreateDatabase("Userdata.db", MODE_PRIVATE, null);
        DB.execSQL("Create table if not exists Userdetails(" +
                "email TEXT primary key," +
                "subscribed TEXT)");
        radioButtonEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeeLogin = true;
            }
        });

        radioButtonCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeeLogin=false;
            }
        });

        // whenever the login button is clicked the UserLogin method will be invoked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                loginUser(txt_email,txt_password, employeeLogin);
            }
        });
    }

    private void loginUser(String email, String password, Boolean employeeLogin) {

        //user gets logs in the app using information registered in firebase
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // Log in as a Civil Protection Employee
                if(employeeLogin){
                    Toast.makeText(Login_Activity.this,"Login was successful!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login_Activity.this,ViewEvents.class));
                    finish(); //when the user clicks the "back" button he will not be able to return to this activity
                // Login as a Civilian, location will get tracked
                }else{
                    LocationUtils locationUtils = new LocationUtils();
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    mDatabaseRef = FirebaseDatabase.getInstance(firebaseRealtimeDatabaseInstance).getReference("userlocations");
                    DbHelper dbHelper = new DbHelper(DB);
                    String subscribed = dbHelper.getSubscriptionStatus(email,Login_Activity.this);
                    String locationTracked=locationUtils.trackLocation(email,subscribed,mDatabaseRef,Login_Activity.this,locationManager);
                   // location is tracked ,login is successful
                    if(locationTracked != "unknown"){
                        Toast.makeText(Login_Activity.this,"Login was successful!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login_Activity.this, HomePage.class);
                        intent.putExtra("UserEmail",email);
                        intent.putExtra("UserLocation",locationTracked);
                        //intent.putExtra("UserSubscribed",)
                        startActivity(intent);
                        finish(); //when the user clicks the "back" button he will not be able to return to this activity
                        // location is not tracked ,login is not successful
                    }else{
                        Toast.makeText(Login_Activity.this,"Location not found , try again in a few seconds!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        auth.signInWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login_Activity.this,"Credentials Incorrect!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}