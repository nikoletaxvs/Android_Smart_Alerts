package com.example.smart_alert;

import com.google.android.gms.tasks.Task;

import java.util.HashMap;

public interface IUpdateObjectOnRealtimeDb {
    public Task<Void> updateValueInRealtimeDb(String key, HashMap<String,Object> hashMap);
}
