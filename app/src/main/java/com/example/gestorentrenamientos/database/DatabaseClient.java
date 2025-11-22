package com.example.gestorentrenamientos.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient instancia;
    private static AppDatabase appDatabaseStatic; // Para acceso desde callback

    private AppDatabase appDatabase;

    private DatabaseClient(Context context){
        this.context = context;

        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "GestorEntreno")
                .fallbackToDestructiveMigration() // acordate, hace que si cambias algo no tengas que borrar la bd
                .addCallback(AppDatabase.roomCallback) // ← Agregar callback
                .build();

        appDatabaseStatic = appDatabase; // Guardar referencia estática
    }

    public static synchronized DatabaseClient getInstance(Context context){
        if (instancia == null){
            instancia = new DatabaseClient(context);
        }
        return instancia;
    }

    // Método estático para acceso desde el callback
    static AppDatabase getInstanceStatic() {
        return appDatabaseStatic;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}