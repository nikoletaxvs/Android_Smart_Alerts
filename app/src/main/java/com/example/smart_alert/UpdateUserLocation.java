package com.example.smart_alert;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateUserLocation implements IUpdateObjectOnRealtimeDb{
    private DatabaseReference ref;
    @Override
    public Task<Void> updateValueInRealtimeDb(String key, HashMap<String, Object> hashMap) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = db.getReference("userlocations");
        return ref.child(key).updateChildren(hashMap);
    }
}
