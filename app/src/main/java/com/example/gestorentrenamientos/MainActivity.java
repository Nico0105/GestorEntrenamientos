package com.example.gestorentrenamientos;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorentrenamientos.database.User;
import com.example.gestorentrenamientos.database.DatabaseClient;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Button btnCrearRutina;
    private Button btnVerRutinas;
    private TextView tvNombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCrearRutina = findViewById(R.id.btnCrearRutina);
        btnVerRutinas = findViewById(R.id.btnVerRutinas);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);

        int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getInt("USER_ID", -1); // -1 si no existe

        if (userId != -1) {
            Executors.newSingleThreadExecutor().execute(() -> {
                User user = DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .userDao()
                        .getUserById(userId);

                if (user != null) {
                    runOnUiThread(() -> tvNombreUsuario.setText("Hola, " + user.getUsername()));
                } else {
                    runOnUiThread(() -> tvNombreUsuario.setText("Hola"));
                }
            });
        } else {
            tvNombreUsuario.setText("Hola");
        }

        btnCrearRutina.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearRutinaActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });

        btnVerRutinas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VerRutinasActivity.class);
            startActivity(intent);
        });
    }
}
