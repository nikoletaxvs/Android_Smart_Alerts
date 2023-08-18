package com.example.smart_alert;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GetUserLocations implements IGetObjectsFromRealtimeDb<Civilian>{
    private DatabaseReference ref;
    public ArrayList<Civilian> getObjects(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = db.getReference("userlocations");
        ArrayList<Civilian> civilians = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap :snapshot.getChildren()){
                    Civilian c = snap.getValue(Civilian.class);
                    civilians.add(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return civilians;
    }


}
