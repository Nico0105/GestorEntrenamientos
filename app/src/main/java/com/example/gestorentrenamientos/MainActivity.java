package com.example.gestorentrenamientos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorentrenamientos.database.DatabaseClient;
import com.example.gestorentrenamientos.database.Ejercicios;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerEjercicios;
    private List<Ejercicios> listaEjercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerEjercicios = findViewById(R.id.spinnerEjercicios);
        Button btnVerificar = findViewById(R.id.btnVerificar);

        listaEjercicios = new ArrayList<>();

        // Cargar ejercicios automáticamente al abrir la app
        cargarEjerciciosEnSpinner();

        // Botón para verificar (ahora también carga el spinner)
        btnVerificar.setOnClickListener(v -> {
            cargarEjerciciosEnSpinner();
        });

        // Listener cuando se selecciona un ejercicio del spinner
        spinnerEjercicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!listaEjercicios.isEmpty() && position > 0) {
                    // position 0 es "-- Seleccionar ejercicio --"
                    Ejercicios ejercicioSeleccionado = listaEjercicios.get(position - 1);
                    mostrarDetallesEjercicio(ejercicioSeleccionado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
    }

    private void cargarEjerciciosEnSpinner() {
        // Ejecutar en hilo secundario (obligatorio para Room)
        Executors.newSingleThreadExecutor().execute(() -> {

            // Obtener todos los ejercicios de la BD
            List<Ejercicios> ejercicios = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .ejerciciosDao()
                    .obtenerTodosLosEjercicios();

            // Volver al hilo principal para actualizar la UI
            runOnUiThread(() -> {
                if (ejercicios.isEmpty()) {
                    Toast.makeText(this,
                            "⚠️ No hay ejercicios cargados",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Guardar la lista completa
                    listaEjercicios = ejercicios;

                    // Crear lista de nombres para el spinner
                    List<String> nombresEjercicios = new ArrayList<>();
                    nombresEjercicios.add("-- Seleccionar ejercicio --"); // Primer item

                    for (Ejercicios ej : ejercicios) {
                        String texto = ej.getName() + " (" + ej.getGrupoMuscular() + ")";
                        nombresEjercicios.add(texto);
                    }

                    // Configurar el adapter del Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            nombresEjercicios
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEjercicios.setAdapter(adapter);

                    Toast.makeText(this,
                            "✅ " + ejercicios.size() + " ejercicios disponibles",
                            Toast.LENGTH_SHORT).show();

                    // Mostrar algunos en el Logcat para verificar
                    Log.d("DB_CHECK", "=== EJERCICIOS CARGADOS ===");
                    for (int i = 0; i < Math.min(10, ejercicios.size()); i++) {
                        Ejercicios ej = ejercicios.get(i);
                        Log.d("DB_CHECK", (i + 1) + ". " + ej.getName() +
                                " - " + ej.getGrupoMuscular() +
                                " (" + ej.getEquipamiento() + ")");
                    }
                }
            });
        });
    }

    private void mostrarDetallesEjercicio(Ejercicios ejercicio) {
        String detalles = "📋 " + ejercicio.getName() + "\n" +
                "💪 Grupo: " + ejercicio.getGrupoMuscular() + "\n" +
                "🏋️ Equipo: " + ejercicio.getEquipamiento() + "\n" +
                "⭐ Nivel: " + ejercicio.getNivelDificultad();

        Toast.makeText(this, detalles, Toast.LENGTH_LONG).show();
        Log.d("EJERCICIO_SELECCIONADO", detalles);
    }
}