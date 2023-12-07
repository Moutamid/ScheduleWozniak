package com.example.schedulewozniak;

import android.app.Application;

public class ScheduleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the database singleton
        DatabaseSingleton.getInstance(this);
    }
}

