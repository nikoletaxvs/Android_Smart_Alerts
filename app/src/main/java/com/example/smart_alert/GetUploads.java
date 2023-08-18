package com.example.smart_alert;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetUploads implements IGetObjectsFromRealtimeDb<NaturalEvent>{
    private DatabaseReference ref;
    @Override
    public ArrayList<NaturalEvent> getObjects() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = db.getReference("uploads");
        ArrayList<NaturalEvent> naturalEvents = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap :snapshot.getChildren()){
                    NaturalEvent c = snap.getValue(NaturalEvent.class);
                    naturalEvents.add(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return naturalEvents;
    }
}
