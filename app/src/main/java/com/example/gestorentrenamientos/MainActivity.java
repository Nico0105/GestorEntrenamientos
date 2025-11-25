package com.example.gestorentrenamientos;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private Button btnAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCrearRutina = findViewById(R.id.btnCrearRutina);
        btnVerRutinas = findViewById(R.id.btnVerRutinas);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);

        btnAdmin = findViewById(R.id.btnAdmin);
        btnAdmin.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean esAdmin = prefs.getBoolean("IS_ADMIN", false);

        if(esAdmin){
            btnAdmin.setVisibility(View.VISIBLE);
            btnAdmin.setOnClickListener(v -> {
                Intent i = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(i);
            });
        }


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
