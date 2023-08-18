package com.example.smart_alert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {
    private Button uploadEvent;
    private Button subscribe;
    private String email;
    private Button analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        uploadEvent = findViewById(R.id.reportBtn);
        subscribe = findViewById(R.id.viewAllBtn);
        analytics = findViewById(R.id.viewAnBtn);

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomePage.this, Analytics_Activity.class);
                intent1.putExtra("UserEmail",email);

                intent1.putExtra("From","Civilian");

                startActivity(intent1);
            }
        });
        //get data from ViewEvents
        Intent intent = getIntent();
        email = intent.getStringExtra("UserEmail");
       // Toast.makeText(HomePage.this,"Email " +email,Toast.LENGTH_SHORT).show();
        uploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmain = new Intent(HomePage.this, MainActivity.class);
                intentmain.putExtra("UserEmail",email);
              //  Toast.makeText(HomePage.this,"Email " +email,Toast.LENGTH_SHORT).show();
                startActivity(intentmain);
                startActivity(intentmain);
            }
        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomePage.this, Subscribe_Activity.class);
                intent1.putExtra("UserEmail",email);
                //Toast.makeText(HomePage.this,"Email " +email,Toast.LENGTH_SHORT).show();
                startActivity(intent1);
            }
        });

    }
}