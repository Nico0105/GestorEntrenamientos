package com.example.gestorentrenamientos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorentrenamientos.database.DatabaseClient;
import com.example.gestorentrenamientos.database.DiaEntrenamiento;
import com.example.gestorentrenamientos.database.Ejercicios;
import com.example.gestorentrenamientos.database.EjerciciosPorDia;
import com.example.gestorentrenamientos.database.Rutinas;

import java.util.List;
import java.util.concurrent.Executors;

public class VerRutinasActivity extends AppCompatActivity {

    private LinearLayout containerRutinas;
    private TextView tvSinRutinas;
    private View scrollRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_rutinas);

        containerRutinas = findViewById(R.id.containerRutinas);
        tvSinRutinas = findViewById(R.id.tvSinRutinas);
        scrollRutinas = findViewById(R.id.scrollRutinas);

        cargarRutinas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        containerRutinas.removeAllViews();
        cargarRutinas();
    }

    private void cargarRutinas() {
        Executors.newSingleThreadExecutor().execute(() -> {
            int userId = obtenerUsuarioLogueado();

            List<Rutinas> rutinas = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .rutinasDao()
                    .obtenerRutinasDelUsuario(userId);

            runOnUiThread(() -> {
                if (rutinas.isEmpty()) {
                    tvSinRutinas.setVisibility(View.VISIBLE);
                    scrollRutinas.setVisibility(View.GONE);
                } else {
                    tvSinRutinas.setVisibility(View.GONE);
                    scrollRutinas.setVisibility(View.VISIBLE);

                    for (Rutinas rutina : rutinas) {
                        agregarRutinaCard(rutina);
                    }
                }
            });
        });
    }

    private int obtenerUsuarioLogueado() {
        return getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("USER_ID", -1);
    }

    private void agregarRutinaCard(Rutinas rutina) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_rutina, containerRutinas, false);

        TextView tvNombreRutina = cardView.findViewById(R.id.tvNombreRutina);
        LinearLayout containerDiasRutina = cardView.findViewById(R.id.containerDiasRutina);
        Button btnEditarRutina = cardView.findViewById(R.id.btnEditarRutina);
        Button btnEliminarRutina = cardView.findViewById(R.id.btnEliminarRutina);

        String nombre = rutina.getNombreRutina();
        if (rutina.isActive()) nombre = "⭐ " + nombre + " (Activa)";
        tvNombreRutina.setText(nombre);


        btnEliminarRutina.setOnClickListener(v -> mostrarDialogoEliminar(rutina));

        // Cargar días de la rutina
        Executors.newSingleThreadExecutor().execute(() -> {
            List<DiaEntrenamiento> dias = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .diaEntrenamientoDao()
                    .obtenerDiasDeRutina(rutina.getId());

            runOnUiThread(() -> {
                for (DiaEntrenamiento dia : dias) {
                    LinearLayout diaLayout = new LinearLayout(this);
                    diaLayout.setOrientation(LinearLayout.VERTICAL);
                    diaLayout.setPadding(16, 8, 16, 8);

                    TextView tvDia = new TextView(this);
                    tvDia.setText("📅 Día " + dia.getDias() + " - " + dia.getDiaDe());
                    tvDia.setTextSize(16);
                    tvDia.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                    diaLayout.addView(tvDia);

                    cargarEjerciciosDia(dia.getId(), diaLayout);
                    containerDiasRutina.addView(diaLayout);
                }
            });
        });

        containerRutinas.addView(cardView);
    }

    private void cargarEjerciciosDia(int diaId, LinearLayout container) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<EjerciciosPorDia> ejerciciosPorDia = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .ejerciciosPorDiaDao()
                    .obtenerEjerciciosDelDia(diaId);

            if (!ejerciciosPorDia.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                int count = 0;

                for (EjerciciosPorDia epd : ejerciciosPorDia) {
                    Ejercicios ejercicio = DatabaseClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .ejerciciosDao()
                            .obtenerEjercicioPorId(epd.getEjercicioId());

                    if (ejercicio != null) {
                        if (count > 0) sb.append("\n");
                        sb.append("   • ").append(ejercicio.getName());
                        count++;
                    }
                }

                runOnUiThread(() -> {
                    TextView tvEjercicios = new TextView(this);
                    tvEjercicios.setText(sb.toString());
                    tvEjercicios.setTextSize(14);
                    tvEjercicios.setPadding(32, 4, 16, 12);
                    container.addView(tvEjercicios);
                });
            }
        });
    }

    private void mostrarDialogoEliminar(Rutinas rutina) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Rutina")
                .setMessage("¿Estás seguro de que deseas eliminar '" + rutina.getNombreRutina() + "'?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarRutina(rutina.getId()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarRutina(int rutinaId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .rutinasDao()
                    .eliminarRutina(rutinaId);

            runOnUiThread(() -> {
                Toast.makeText(this, "✅ Rutina eliminada", Toast.LENGTH_SHORT).show();
                containerRutinas.removeAllViews();
                cargarRutinas();
            });
        });
    }
}
