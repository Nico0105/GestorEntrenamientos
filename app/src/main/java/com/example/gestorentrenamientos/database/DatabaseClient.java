package com.example.gestorentrenamientos.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private static DatabaseClient instance;
    private AppDatabase appDatabase;
    private Context context;

    private DatabaseClient(Context context) {
        this.context = context.getApplicationContext();
        appDatabase = AppDatabase.getInstance(this.context);
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
