package com.example.gestorentrenamientos.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RutinasDao {

    @Insert
    long insertarRutina(Rutinas rutina); // Retorna el ID generado

    @Update
    void actualizarRutina(Rutinas rutina);

    // Obtener todas las rutinas de un usuario
    @Query("SELECT * FROM rutinas WHERE user_id = :userId")
    List<Rutinas> obtenerRutinasDelUsuario(int userId);

    // Obtener la rutina activa del usuario
    @Query("SELECT * FROM rutinas WHERE user_id = :userId AND is_active = 1 LIMIT 1")
    Rutinas obtenerRutinaActiva(int userId);

    // Desactivar todas las rutinas de un usuario (antes de activar una nueva)
    @Query("UPDATE rutinas SET is_active = 0 WHERE user_id = :userId")
    void desactivarTodasLasRutinas(int userId);

    // Activar una rutina específica
    @Query("UPDATE rutinas SET is_active = 1 WHERE id = :rutinaId")
    void activarRutina(int rutinaId);

    // Obtener rutina por ID
    @Query("SELECT * FROM rutinas WHERE id = :id LIMIT 1")
    Rutinas obtenerRutinaPorId(int id);

    // Contar cuántas rutinas tiene un usuario
    @Query("SELECT COUNT(*) FROM rutinas WHERE user_id = :userId")
    int contarRutinasDelUsuario(int userId);

    @Query("DELETE FROM rutinas WHERE id = :rutinaId")
    void eliminarRutina(int rutinaId);
}