package com.example.gestorentrenamientos.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(
        tableName = "rutinas",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("user_id")}
)
public class Rutinas {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    private String NombreRutina; // "Rutina Push/Pull", "Fullbody"
    private String description;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "is_active")
    private boolean isActive; // Para marcar la rutina actual

    // Constructor
    public Rutinas(int userId, String NombreRutina, String description) {
        this.userId = userId;
        this.NombreRutina = NombreRutina;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
        this.isActive = false;
    }

    // Getters y Setters


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNombreRutina() {
        return NombreRutina;
    }

    public void setNombreRutina(String nombreRutina) {
        NombreRutina = nombreRutina;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}