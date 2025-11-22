package com.example.gestorentrenamientos.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

        @Insert
        void insertUser(User user);

        @Update
        void updateUser(User user);

        @Delete
        void deleteUser(User user);

        // Obtener usuario por email (para login)
        @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
        User getUserByEmail(String email);

        // Obtener usuario por username
        @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
        User getUserByUsername(String username);

        // Obtener usuario por ID
        @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
        User getUserById(int id);

        // Obtener todos los usuarios (útil para debugging/admin)
        @Query("SELECT * FROM users")
        List<User> getAllUsers();

        // Actualizar último login
        @Query("UPDATE users SET last_login_at = :timestamp WHERE id = :id")
        void ultimoLogin(int id, long timestamp);

        // Verificar si un email ya existe (útil para validar registro)
        @Query("SELECT COUNT(*) FROM users WHERE email = :email")
        int emailExiste(String email);

        // Verificar si un username ya existe
        @Query("SELECT COUNT(*) FROM users WHERE username = :username")
        int usernameExiste(String username);

        // Eliminar todos los usuarios (útil para testing)
        @Query("DELETE FROM users")
        void deleteAllUsers();

        // Contar total de usuarios
        @Query("SELECT COUNT(*) FROM users")
        int contarUsuarios();
}