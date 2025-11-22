package com.example.gestorentrenamientos.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface UserDAO {

        @Insert
        void insertUser(User user);

        @Query("SELECT * FROM user WHERE nombre = :usuario AND password = :password LIMIT 1")
        User login(String usuario, String password);

        @Query("SELECT * FROM user")
        List<User> getAllUsers();
}
