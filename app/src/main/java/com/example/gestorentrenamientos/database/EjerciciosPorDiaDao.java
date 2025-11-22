package com.example.gestorentrenamientos.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EjerciciosPorDiaDao {

    @Insert
    long insertarEjercicioPorDia(EjerciciosPorDia ejercicioPorDia);

    @Update
    void actualizarEjercicioPorDia(EjerciciosPorDia ejercicioPorDia);

    @Delete
    void eliminarEjercicioPorDia(EjerciciosPorDia ejercicioPorDia);

    // Obtener todos los ejercicios de un día específico, ordenados
    @Query("SELECT * FROM EjerciciosPorDia WHERE DiaEntrenamiento_id = :diaId ORDER BY order_index ASC")
    List<EjerciciosPorDia> obtenerEjerciciosDelDia(int diaId);

    // Obtener ejercicio específico por ID
    @Query("SELECT * FROM EjerciciosPorDia WHERE id = :id LIMIT 1")
    EjerciciosPorDia obtenerEjercicioPorDiaPorId(int id);

    // Contar cuántos ejercicios tiene un día
    @Query("SELECT COUNT(*) FROM EjerciciosPorDia WHERE DiaEntrenamiento_id = :diaId")
    int contarEjerciciosDelDia(int diaId);

    // Eliminar todos los ejercicios de un día
    @Query("DELETE FROM EjerciciosPorDia WHERE DiaEntrenamiento_id = :diaId")
    void eliminarTodosLosEjerciciosDelDia(int diaId);

    // Actualizar el orden de un ejercicio
    @Query("UPDATE EjerciciosPorDia SET order_index = :nuevoOrden WHERE id = :id")
    void actualizarOrden(int id, int nuevoOrden);

    // Obtener el último orden usado (para agregar al final)
    @Query("SELECT MAX(order_index) FROM EjerciciosPorDia WHERE DiaEntrenamiento_id = :diaId")
    Integer obtenerUltimoOrden(int diaId);
}