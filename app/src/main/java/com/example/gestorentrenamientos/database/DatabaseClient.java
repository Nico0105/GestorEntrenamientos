package com.example.gestorentrenamientos.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient instancia;

    private AppDatabase appDatabase;

    private DatabaseClient(Context context){
        this.context = context;

        appDatabase  = Room.databaseBuilder(context, AppDatabase.class, "GestorEntreno").build();
    }


    public static synchronized DatabaseClient getInstance(Context context){
        if (instancia == null){
            instancia = new DatabaseClient(context);
        }
        return instancia;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
