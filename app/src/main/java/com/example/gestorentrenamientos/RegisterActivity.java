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

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegistrarse;
    private TextView tvYaTienesCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar vistas
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        tvYaTienesCuenta = findViewById(R.id.tvYaTienesCuenta);

        // Click en Registrarse
        btnRegistrarse.setOnClickListener(v -> realizarRegistro());

        // Click en si ya tiene cuenta, Inicia sesión
        tvYaTienesCuenta.setOnClickListener(v -> {
            finish();
        });
    }

    private void realizarRegistro() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validaciones
        if (username.isEmpty()) {
            etUsername.setError("Ingresa un nombre de usuario");
            etUsername.requestFocus();
            return;
        }

        if (username.length() < 3) {
            etUsername.setError("Mínimo 3 caracteres");
            etUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Ingresa tu email");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingresa una contraseña");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mínimo 6 caracteres");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return;
        }

        // Deshabilitar botón mientras se procesa
        btnRegistrarse.setEnabled(false);
        btnRegistrarse.setText("Registrando...");

        // Registrar usuario en la BD
        Executors.newSingleThreadExecutor().execute(() -> {
            UserDAO dao = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .userDao();

            // Verificar si el email ya existe
            if (dao.emailExiste(email) > 0) {
                runOnUiThread(() -> {
                    btnRegistrarse.setEnabled(true);
                    btnRegistrarse.setText("REGISTRARSE");
                    Toast.makeText(this, "El email ya está registrado", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Verificar si el username ya existe
            if (dao.usernameExiste(username) > 0) {
                runOnUiThread(() -> {
                    btnRegistrarse.setEnabled(true);
                    btnRegistrarse.setText("REGISTRARSE");
                    Toast.makeText(this, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Crear usuario con password hasheado
            String passwordHash = PasswordUtils.hashPassword(password);
            User nuevoUsuario = new User(username, email, passwordHash);

            dao.insertUser(nuevoUsuario);

            runOnUiThread(() -> {
                Toast.makeText(this, "¡Registro exitoso! Inicia sesión", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}