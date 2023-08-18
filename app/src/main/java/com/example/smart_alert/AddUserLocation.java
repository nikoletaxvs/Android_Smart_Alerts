package com.example.smart_alert;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUserLocation implements IAddObjectToRealtimeDb<Civilian>{

    private DatabaseReference ref;
    @Override
    public Task<Void> insertToRealtimeDb(Civilian civilian) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://smart-alert-f5055-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = db.getReference("userlocations");
        return ref.push().setValue(civilian);
    }
}
