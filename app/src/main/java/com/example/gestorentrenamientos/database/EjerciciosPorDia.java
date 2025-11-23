package com.example.gestorentrenamientos.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(
        tableName = "EjerciciosPorDia",
        foreignKeys = {
                @ForeignKey(
                        entity = DiaEntrenamiento.class,
                        parentColumns = "id",
                        childColumns = "DiaEntrenamiento_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Ejercicios.class,
                        parentColumns = "id",
                        childColumns = "ejercicios_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("DiaEntrenamiento_id"), @Index("ejercicios_id")}
)
public class EjerciciosPorDia {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "DiaEntrenamiento_id")
    private int DiaEntrenamiento_id;

    @ColumnInfo(name = "ejercicios_id")
    private int ejercicioId;

    @ColumnInfo(name = "order_index")
    private int orderIndex; // Orden del ejercicio en el día (1, 2, 3...)

    private int sets; // Número de series
    private int reps; // Repeticiones

    @ColumnInfo(name = "PesoKg")
    private float PesoKg; // Peso en kg

    @ColumnInfo(name = "DescansoSeries")
    private int DescansoSeries; // Descanso entre series

    private String notas; // Notas específicas: "RIR 2", "Tempo 3-1-1"

    // Constructor
    public EjerciciosPorDia(int DiaEntrenamiento_id, int ejercicioId,
                       int sets, int reps, float PesoKg) {
        this.DiaEntrenamiento_id = DiaEntrenamiento_id;
        this.ejercicioId = ejercicioId;
        this.sets = sets;
        this.reps = reps;
        this.PesoKg = PesoKg;
        this.DescansoSeries = 90; // Default 90 segundos
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

    public int getOrderIndex() { return orderIndex; }

    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public int getDescansoSeries() {
        return DescansoSeries;
    }

    public void setDescansoSeries(int descansoSeries) {
        DescansoSeries = descansoSeries;
    }

    public float getPesoKg() {
        return PesoKg;
    }

    public void setPesoKg(float pesoKg) {
        PesoKg = pesoKg;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getEjercicioId() {
        return ejercicioId;
    }

    public void setEjercicioId(int ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    public int getDiaEntrenamiento_id() {
        return DiaEntrenamiento_id;
    }

    public void setDiaEntrenamiento_id(int diaEntrenamiento_id) {
        DiaEntrenamiento_id = diaEntrenamiento_id;
    }
}
