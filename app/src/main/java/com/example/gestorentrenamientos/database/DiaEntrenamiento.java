package com.example.gestorentrenamientos.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(
        tableName = "DiaEntrenamiento",
        foreignKeys = @ForeignKey(
                entity = Rutinas.class,
                parentColumns = "id",
                childColumns = "rutinaId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("rutinaId")}
)
public class DiaEntrenamiento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "rutinaId")
    private int rutinaId;

    @ColumnInfo(name = "Dias")
    private int Dias; // 1, 2, 3, etc.

    @ColumnInfo(name = "DiaDe")
    private String DiaDe; // "Día 1 - Pecho", "Día 2 - Espalda"

    private String notas; // Notas que tenga el usuario

    // Constructor
    public DiaEntrenamiento(int rutinaId, int Dias, String DiaDe) {
        this.rutinaId = rutinaId;
        this.Dias = Dias;
        this.DiaDe = DiaDe;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getDiaDe() {
        return DiaDe;
    }

    public void setDiaDe(String diaDe) {
        DiaDe = diaDe;
    }

    public int getDias() {
        return Dias;
    }

    public void setDias(int dias) {
        Dias = dias;
    }

    public int getRutinaId() {
        return rutinaId;
    }

    public void setRutinaId(int rutinaId) {
        this.rutinaId = rutinaId;
    }
}