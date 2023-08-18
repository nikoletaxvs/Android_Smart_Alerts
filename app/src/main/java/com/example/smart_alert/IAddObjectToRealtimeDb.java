package com.example.smart_alert;

import com.google.android.gms.tasks.Task;

public interface IAddObjectToRealtimeDb<T> {
    public Task<Void> insertToRealtimeDb(T t);
}
