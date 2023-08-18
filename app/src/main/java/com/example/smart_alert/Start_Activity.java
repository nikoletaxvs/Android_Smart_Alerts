package com.example.smart_alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Start_Activity extends AppCompatActivity {
    private Button register;
    private Button login;
    private int ACCESS_FINE_LOCATION_CODE=1;
    private ImageButton greekLan;
    private ImageButton engLan;
    LanguageManager lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        lang = new LanguageManager(this);

        //link fields with xml elements
        register =findViewById(R.id.button);
        login =findViewById(R.id.button2);
       greekLan = findViewById(R.id.imageGreece);
       engLan = findViewById(R.id.imageEng);

        //setting listeners
        greekLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Start_Activity.this,"Greek",Toast.LENGTH_SHORT).show();
                lang.updateResource("el");
                startActivity(new Intent(Start_Activity.this,Start_Activity.class)) ;
            }
        });
        engLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Start_Activity.this,"English",Toast.LENGTH_SHORT).show();
                lang.updateResource("en");
                startActivity(new Intent(Start_Activity.this,Start_Activity.class)) ;
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // redirecting user to register form
                startActivity(new Intent(Start_Activity.this, Register_Activity.class));
                //finish();//when the user clicks the "back" button he will not be able to return to this activity
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //redirecting user to login form
                startActivity(new Intent(Start_Activity.this,Login_Activity.class));
                //finish();//when the user clicks the "back" button he will not be able to return to this activity
            }
        });
        if(ContextCompat.checkSelfPermission(Start_Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //persmission granted
        }else{
            requestLocationPermission();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==ACCESS_FINE_LOCATION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"ACCESS FINE LOCATION GRANDED",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"ACCESS FINE LOCATION NOT GRANDED",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because civil protection needs to know where this event is located")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Start_Activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_LOCATION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_LOCATION_CODE);
        }
    }


}