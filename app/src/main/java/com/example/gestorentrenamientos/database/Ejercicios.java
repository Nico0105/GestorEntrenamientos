package com.example.gestorentrenamientos.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ejercicios")
public class Ejercicios {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;

    @ColumnInfo(name = "grupo_muscular")
    private String grupoMuscular; // ← minúscula inicial

    @ColumnInfo(name = "equipamiento")
    private String equipamiento; // ← minúscula inicial

    @ColumnInfo(name = "nivel_dificultad")
    private String nivelDificultad; // ← minúscula inicial

    // Constructor
    public Ejercicios(String name, String description, String grupoMuscular,
                      String equipamiento, String nivelDificultad) {
        this.name = name;
        this.description = description;
        this.grupoMuscular = grupoMuscular;
        this.equipamiento = equipamiento;
        this.nivelDificultad = nivelDificultad;
    }

    // Getters y Setters actualizados
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGrupoMuscular() { return grupoMuscular; }
    public void setGrupoMuscular(String grupoMuscular) { this.grupoMuscular = grupoMuscular; }

    public String getEquipamiento() { return equipamiento; }
    public void setEquipamiento(String equipamiento) { this.equipamiento = equipamiento; }

    public String getNivelDificultad() { return nivelDificultad; }
    public void setNivelDificultad(String nivelDificultad) { this.nivelDificultad = nivelDificultad; }
}