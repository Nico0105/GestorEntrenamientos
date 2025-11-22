package com.example.gestorentrenamientos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorentrenamientos.database.DatabaseClient;
import com.example.gestorentrenamientos.database.User;
import com.example.gestorentrenamientos.database.UserDAO;
import com.example.gestorentrenamientos.utils.PasswordUtils;


import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistrarse = findViewById(R.id.tvRegistrarse);

        // Click en Login
        btnLogin.setOnClickListener(v -> realizarLogin());

        // Click en "¿No tienes cuenta? Regístrate"
        tvRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void realizarLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validaciones
        if (email.isEmpty()) {
            etEmail.setError("Ingresa tu email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingresa tu contraseña");
            etPassword.requestFocus();
            return;
        }

        // Deshabilitar botón mientras se procesa
        btnLogin.setEnabled(false);
        btnLogin.setText("Iniciando sesión...");

        // Buscar usuario en la BD
        Executors.newSingleThreadExecutor().execute(() -> {
            UserDAO dao = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .userDao();

            User user = dao.getUserByEmail(email);

            runOnUiThread(() -> {
                btnLogin.setEnabled(true);
                btnLogin.setText("INICIAR SESIÓN");

                if (user != null && PasswordUtils.verificarPassword(password, user.getPasswordHash())) {
                    // Login exitoso - Actualizar último login
                    Executors.newSingleThreadExecutor().execute(() -> {
                        dao.ultimoLogin(user.getId(), System.currentTimeMillis());
                    });

                    Toast.makeText(this, "¡Bienvenido " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();

                    // Ir a MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", user.getId());
                    intent.putExtra("USERNAME", user.getUsername());
                    startActivity(intent);
                    finish(); // Cerrar LoginActivity
                } else {
                    Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    etPassword.setText(""); // Limpiar contraseña
                }
            });
        });
    }
}