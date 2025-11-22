package com.example.gestorentrenamientos.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DiaEntrenamientoDao {

    @Insert
    long insertarDia(DiaEntrenamiento dia); // Retorna el ID generado

    @Update
    void actualizarDia(DiaEntrenamiento dia);

    @Delete
    void eliminarDia(DiaEntrenamiento dia);

    // Obtener todos los días de una rutina, ordenados por número
    @Query("SELECT * FROM DiaEntrenamiento WHERE rutinaId = :rutinaId ORDER BY Dias ASC")
    List<DiaEntrenamiento> obtenerDiasDeRutina(int rutinaId);

    // Obtener un día específico por ID
    @Query("SELECT * FROM DiaEntrenamiento WHERE id = :id LIMIT 1")
    DiaEntrenamiento obtenerDiaPorId(int id);

    // Obtener día por número dentro de una rutina
    @Query("SELECT * FROM DiaEntrenamiento WHERE rutinaId = :rutinaId AND Dias = :numeroDia LIMIT 1")
    DiaEntrenamiento obtenerDiaPorNumero(int rutinaId, int numeroDia);

    // Contar cuántos días tiene una rutina
    @Query("SELECT COUNT(*) FROM DiaEntrenamiento WHERE rutinaId = :rutinaId")
    int contarDiasDeRutina(int rutinaId);

    // Eliminar todos los días de una rutina
    @Query("DELETE FROM DiaEntrenamiento WHERE rutinaId = :rutinaId")
    void eliminarTodosLosDiasDeRutina(int rutinaId);
}