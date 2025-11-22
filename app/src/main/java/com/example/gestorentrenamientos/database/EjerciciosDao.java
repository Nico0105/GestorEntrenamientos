package com.example.gestorentrenamientos.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EjerciciosDao {

    @Insert
    void insertarEjercicio(Ejercicios ejercicio);

    @Insert
    void insertarVariosEjercicios(List<Ejercicios> ejercicios);

    @Update
    void actualizarEjercicio(Ejercicios ejercicio);

    @Delete
    void eliminarEjercicio(Ejercicios ejercicio);

    // Obtener todos los ejercicios
    @Query("SELECT * FROM ejercicios ORDER BY name ASC")
    List<Ejercicios> obtenerTodosLosEjercicios();

    // Buscar ejercicios por grupo muscular
    @Query("SELECT * FROM ejercicios WHERE grupo_muscular = :grupoMuscular ORDER BY name ASC")
    List<Ejercicios> buscarPorGrupoMuscular(String grupoMuscular);

    // Buscar ejercicios por nombre (para búsqueda con autocompletado)
    @Query("SELECT * FROM ejercicios WHERE name LIKE '%' || :nombre || '%' ORDER BY name ASC")
    List<Ejercicios> buscarPorNombre(String nombre);

    // Buscar por nivel de dificultad
    @Query("SELECT * FROM ejercicios WHERE nivel_dificultad = :nivel ORDER BY name ASC")
    List<Ejercicios> buscarPorNivelDificultad(String nivel);

    // Obtener ejercicio por ID
    @Query("SELECT * FROM ejercicios WHERE id = :id LIMIT 1")
    Ejercicios obtenerEjercicioPorId(int id);

    // Obtener todos los grupos musculares únicos (para filtros)
    @Query("SELECT DISTINCT grupo_muscular FROM ejercicios ORDER BY grupo_muscular ASC")
    List<String> obtenerGruposMusculares();

    // Buscar por equipamiento
    @Query("SELECT * FROM ejercicios WHERE equipamiento = :equipamiento ORDER BY name ASC")
    List<Ejercicios> buscarPorEquipamiento(String equipamiento);
}