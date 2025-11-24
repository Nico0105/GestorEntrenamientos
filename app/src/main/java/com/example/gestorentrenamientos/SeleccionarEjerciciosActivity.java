package com.example.gestorentrenamientos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorentrenamientos.database.DatabaseClient;
import com.example.gestorentrenamientos.database.Ejercicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class SeleccionarEjerciciosActivity extends AppCompatActivity {

    private LinearLayout containerFiltros;
    private LinearLayout containerEjercicios;
    private Button btnConfirmarSeleccion;
    private TextView tvTitulo;

    private String grupoMuscular;
    private int diaIndex;
    private Map<Integer, CheckBox> checkBoxEjerciciosMap = new HashMap<>();
    private Map<String, CheckBox> checkBoxFiltrosMap = new HashMap<>();
    private ArrayList<Integer> ejerciciosPrevios;

    private String[] gruposMusculares = {"Pecho", "Espalda", "Piernas", "Hombros", "Brazos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_ejercicios);

        containerFiltros = findViewById(R.id.containerFiltros);
        containerEjercicios = findViewById(R.id.containerEjercicios);
        btnConfirmarSeleccion = findViewById(R.id.btnConfirmarSeleccion);
        tvTitulo = findViewById(R.id.tvTitulo);

        // Obtener datos del intent
        grupoMuscular = getIntent().getStringExtra("GRUPO_MUSCULAR");
        diaIndex = getIntent().getIntExtra("DIA_INDEX", 0);
        ejerciciosPrevios = getIntent().getIntegerArrayListExtra("EJERCICIOS_PREVIOS");

        if (ejerciciosPrevios == null) {
            ejerciciosPrevios = new ArrayList<>();
        }

        tvTitulo.setText("Selecciona ejercicios para tu entrenamiento");

        // Crear filtros de grupos musculares
        crearFiltrosGrupos();

        btnConfirmarSeleccion.setOnClickListener(v -> confirmarSeleccion());
    }

    private void crearFiltrosGrupos() {
        for (String grupo : gruposMusculares) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(grupo);
            checkBox.setTextSize(14);
            checkBox.setPadding(16, 8, 16, 8);

            // Si el grupo muscular recibido coincide, marcarlo por defecto
            if (grupo.equals(grupoMuscular)) {
                checkBox.setChecked(true);
            }

            checkBoxFiltrosMap.put(grupo, checkBox);

            // Listener para recargar ejercicios cuando cambia el filtro
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                cargarEjerciciosFiltrados();
            });

            containerFiltros.addView(checkBox);
        }

        // Cargar ejercicios iniciales
        cargarEjerciciosFiltrados();
    }

    private void cargarEjerciciosFiltrados() {
        // Obtener grupos seleccionados
        List<String> gruposSeleccionados = new ArrayList<>();
        for (Map.Entry<String, CheckBox> entry : checkBoxFiltrosMap.entrySet()) {
            if (entry.getValue().isChecked()) {
                gruposSeleccionados.add(entry.getKey());
            }
        }

        // Si no hay grupos seleccionados, mostrar todos
        if (gruposSeleccionados.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un grupo muscular", Toast.LENGTH_SHORT).show();
            return;
        }

        // Limpiar ejercicios anteriores
        containerEjercicios.removeAllViews();
        checkBoxEjerciciosMap.clear();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Ejercicios> todosEjercicios = new ArrayList<>();

            // Cargar ejercicios de cada grupo seleccionado
            for (String grupo : gruposSeleccionados) {
                List<Ejercicios> ejercicios = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .ejerciciosDao()
                        .buscarPorGrupoMuscular(grupo);
                todosEjercicios.addAll(ejercicios);
            }

            runOnUiThread(() -> {
                if (todosEjercicios.isEmpty()) {
                    Toast.makeText(this,
                            "No hay ejercicios para los grupos seleccionados",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Agrupar por grupo muscular
                String grupoActual = "";
                for (Ejercicios ejercicio : todosEjercicios) {
                    // Agregar encabezado de grupo si cambia
                    if (!ejercicio.getGrupoMuscular().equals(grupoActual)) {
                        grupoActual = ejercicio.getGrupoMuscular();

                        TextView tvGrupo = new TextView(this);
                        tvGrupo.setText("💪 " + grupoActual);
                        tvGrupo.setTextSize(18);
                        tvGrupo.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                        tvGrupo.setPadding(16, 24, 16, 8);
                        containerEjercicios.addView(tvGrupo);
                    }

                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(ejercicio.getName() + " (" + ejercicio.getEquipamiento() + ")");
                    checkBox.setTextSize(16);
                    checkBox.setPadding(32, 8, 16, 8);

                    // Marcar como seleccionado si ya estaba en la lista previa
                    if (ejerciciosPrevios.contains(ejercicio.getId())) {
                        checkBox.setChecked(true);
                    }

                    checkBoxEjerciciosMap.put(ejercicio.getId(), checkBox);
                    containerEjercicios.addView(checkBox);
                }
            });
        });
    }

    private void confirmarSeleccion() {
        ArrayList<Integer> ejerciciosSeleccionados = new ArrayList<>();
        StringBuilder nombresEjercicios = new StringBuilder();
        int contador = 0;

        for (Map.Entry<Integer, CheckBox> entry : checkBoxEjerciciosMap.entrySet()) {
            if (entry.getValue().isChecked()) {
                ejerciciosSeleccionados.add(entry.getKey());
                if (contador > 0) nombresEjercicios.append(", ");
                nombresEjercicios.append(entry.getValue().getText().toString().split(" \\(")[0]);
                contador++;
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putIntegerArrayListExtra("EJERCICIOS_IDS", ejerciciosSeleccionados); // puede estar vacía
        resultIntent.putExtra("EJERCICIOS_TEXTO", nombresEjercicios.toString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}