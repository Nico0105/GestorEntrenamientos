package com.example.gestorentrenamientos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorentrenamientos.database.AppDatabase;
import com.example.gestorentrenamientos.database.DatabaseClient;
import com.example.gestorentrenamientos.database.User;


public class EditUserActivity extends AppCompatActivity {

    private EditText txtUsername, txtEmail;
    private Button btnGuardar;
    private AppDatabase database;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        btnGuardar = findViewById(R.id.btnGuardar);

        database = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        int userId = getIntent().getIntExtra("userId", -1);

        // Cargar usuario
        new Thread(() -> {
            user = database.userDao().getUserById(userId);

            runOnUiThread(() -> {
                txtUsername.setText(user.getUsername());
                txtEmail.setText(user.getEmail());
            });

        }).start();

        btnGuardar.setOnClickListener(v -> updateUser());
    }

    private void updateUser(){
        String newUsername = txtUsername.getText().toString();
        String newEmail = txtEmail.getText().toString();

        user.setUsername(newUsername);
        user.setEmail(newEmail);

        new Thread(() -> {
            database.userDao().updateUser(user);
            runOnUiThread(() -> {
                Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
