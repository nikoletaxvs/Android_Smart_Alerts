package com.example.smart_alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Analytics_Activity extends AppCompatActivity {
    private DatabaseReference ref;
    private Button back;
    private String email;
    private PieChart piechart;
    private List<PieEntry> entries;
    private String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        piechart = findViewById(R.id.chart);
        back = findViewById(R.id.backBtn3);
        Intent intent = getIntent();
        email = intent.getStringExtra("UserEmail");
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = db.getReference("analytics");

        entries = new ArrayList<>();
        Intent intent2 = getIntent();
        from = intent2.getStringExtra("From");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int[] eventValues={0,0,0,0,0};
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if(dataSnapshot.getValue().toString().contains("Fire")){
                        eventValues[0] ++;
                    }else if(dataSnapshot.getValue().toString().equals("Tsunami")){
                        eventValues[1]++;
                    }else if(dataSnapshot.getValue().toString().contains("Tornado")){
                        eventValues[2]++;
                    }else if(dataSnapshot.getValue().toString().contains("Tornado")){
                        eventValues[3]++;
                    }else if(dataSnapshot.getValue().toString().contains("Flood")){
                        eventValues[4]++;
                    }
                }
               // long count= snapshot.getChildrenCount();
                setValues(eventValues[0],eventValues[1],eventValues[2],eventValues[3],eventValues[4]);
                setUpChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from == "Civilian"){
                    Intent intent1 = new Intent(Analytics_Activity.this, HomePage.class);
                    intent1.putExtra("UserEmail",email);
                    startActivity(intent1);
                }else{
                    Intent intent1 = new Intent(Analytics_Activity.this, ViewEvents.class);
                    //intent1.putExtra("UserEmail",email);
                    startActivity(intent1);
                }
            }
        });

    }

    private void setUpChart() {
        PieDataSet pieDataSet = new PieDataSet(entries,"Pie Chart");
        PieData pieData = new PieData(pieDataSet);
        int[] colors = {Color.rgb(135,206,235),Color.rgb(65,105,225),Color.rgb(255,0,0),Color.rgb(169,169,169),Color.rgb(222,184,135)};
        pieDataSet.setColors(ColorTemplate.createColors(colors));
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieDataSet.setValueTextSize(20f);
        pieDataSet.setValueLineVariableLength(true);

        piechart.setEntryLabelTextSize(0f);
        piechart.setData(pieData);
        piechart.invalidate();
    }

    private void setValues(int fire, int tsunami ,int tornado , int earthquake , int flood) {
        entries.add(new PieEntry(flood,"Flood"));
        entries.add(new PieEntry(tsunami,"Tsunami"));
        entries.add(new PieEntry(fire,"Fire"));
        entries.add(new PieEntry(tornado,"Tornado"));
        entries.add(new PieEntry(tornado,"Earthquake"));
    }

}