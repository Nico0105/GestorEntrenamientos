package com.example.gestorentrenamientos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCrearRutina;
    private Button btnVerRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar botones
        btnCrearRutina = findViewById(R.id.btnCrearRutina);
        btnVerRutinas = findViewById(R.id.btnVerRutinas);

        // Botón para crear nueva rutina
        btnCrearRutina.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearRutinaActivity.class);
            startActivity(intent);
        });

        // Botón para ver rutinas creadas
        btnVerRutinas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VerRutinasActivity.class);
            startActivity(intent);
        });
    }
}