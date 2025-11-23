package com.example.gestorentrenamientos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CrearRutinaActivity extends AppCompatActivity {

    private EditText etNombreRutina;
    private LinearLayout containerDias;
    private Button btnAgregarDia;
    private Button btnGuardarRutina;

    private List<DiaView> diasAgregados = new ArrayList<>();

    private String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
    private String[] gruposMusculares = {"Todos", "Pecho", "Espalda", "Piernas", "Hombros", "Brazos", "Abdomen", "Cardio", "Fullbody"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);

        etNombreRutina = findViewById(R.id.etNombreRutina);
        containerDias = findViewById(R.id.containerDias);
        btnAgregarDia = findViewById(R.id.btnAgregarDia);
        btnGuardarRutina = findViewById(R.id.btnGuardarRutina);

        btnAgregarDia.setOnClickListener(v -> agregarNuevoDia());

        btnGuardarRutina.setOnClickListener(v -> guardarRutina());

        // Agregar el primer día automáticamente
        agregarNuevoDia();
    }

    private void agregarNuevoDia() {
        View diaView = LayoutInflater.from(this).inflate(R.layout.item_dia_entrenamiento, containerDias, false);

        Spinner spinnerDia = diaView.findViewById(R.id.spinnerDia);
        Button btnEliminarDia = diaView.findViewById(R.id.btnEliminarDia);
        Button btnSeleccionarEjercicios = diaView.findViewById(R.id.btnSeleccionarEjercicios);
        TextView tvEjerciciosSeleccionados = diaView.findViewById(R.id.tvEjerciciosSeleccionados);

        ArrayAdapter<String> adapterDias = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, diasSemana);
        adapterDias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(adapterDias);

        // Configurar spinner de grupos musculares
        ArrayAdapter<String> adapterGrupos = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, gruposMusculares);
        adapterGrupos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Crear objeto para guardar datos
        DiaView nuevoDia = new DiaView(diaView, spinnerDia, tvEjerciciosSeleccionados);
        diasAgregados.add(nuevoDia);

        // Botón para eliminar día
        btnEliminarDia.setOnClickListener(v -> {
            containerDias.removeView(diaView);
            diasAgregados.remove(nuevoDia);
            Toast.makeText(this, "Día eliminado", Toast.LENGTH_SHORT).show();
        });

        // Botón para seleccionar ejercicios
        btnSeleccionarEjercicios.setOnClickListener(v -> {
            int indexDia = diasAgregados.indexOf(nuevoDia);
            Intent intent = new Intent(CrearRutinaActivity.this, SeleccionarEjerciciosActivity.class);
            intent.putExtra("DIA_INDEX", indexDia);
            // Enviar ejercicios ya seleccionados para marcarlos
            intent.putIntegerArrayListExtra("EJERCICIOS_PREVIOS", nuevoDia.ejerciciosIds);
            startActivityForResult(intent, indexDia);
        });

        containerDias.addView(diaView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            int diaIndex = requestCode;
            ArrayList<Integer> ejerciciosIds = data.getIntegerArrayListExtra("EJERCICIOS_IDS");
            String ejerciciosTexto = data.getStringExtra("EJERCICIOS_TEXTO");

            if (diaIndex >= 0 && diaIndex < diasAgregados.size()) {
                DiaView dia = diasAgregados.get(diaIndex);
                // REEMPLAZAR la lista completa con la nueva selección
                dia.ejerciciosIds = ejerciciosIds;

                // Actualizar el texto mostrando TODOS los ejercicios
                if (dia.ejerciciosIds != null && !dia.ejerciciosIds.isEmpty()) {
                    dia.tvEjercicios.setText("✅ " + dia.ejerciciosIds.size() + " ejercicios: " + ejerciciosTexto);
                    dia.tvEjercicios.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    dia.tvEjercicios.setText("Ejercicios: Ninguno seleccionado");
                    dia.tvEjercicios.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        }
    }

    private void guardarRutina() {
        String nombreRutina = etNombreRutina.getText().toString().trim();

        if (nombreRutina.isEmpty()) {
            Toast.makeText(this, "⚠️ Ingresa un nombre para la rutina", Toast.LENGTH_SHORT).show();
            return;
        }

        if (diasAgregados.isEmpty()) {
            Toast.makeText(this, "⚠️ Agrega al menos un día de entrenamiento", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí guardarás en la base de datos
        // Por ahora solo mostramos los datos
        StringBuilder resumen = new StringBuilder("Rutina: " + nombreRutina + "\n\n");

        for (int i = 0; i < diasAgregados.size(); i++) {
            DiaView dia = diasAgregados.get(i);
            String diaSeleccionado = dia.spinnerDia.getSelectedItem().toString();


            resumen.append((i + 1)).append(". ")
                    .append(diaSeleccionado).append(" - ");

            if (dia.ejerciciosIds != null && !dia.ejerciciosIds.isEmpty()) {
                resumen.append(" (").append(dia.ejerciciosIds.size()).append(" ejercicios)");
            }
            resumen.append("\n");
        }

        Toast.makeText(this, resumen.toString(), Toast.LENGTH_LONG).show();

        // TODO: Guardar en base de datos usando Room
        // finish(); // Cerrar activity después de guardar
    }

    // Clase auxiliar para manejar cada día
    private static class DiaView {
        View view;
        Spinner spinnerDia;
        TextView tvEjercicios;
        ArrayList<Integer> ejerciciosIds;

        DiaView(View view, Spinner spinnerDia ,TextView tvEjercicios) {
            this.view = view;
            this.spinnerDia = spinnerDia;
            this.tvEjercicios = tvEjercicios;
            this.ejerciciosIds = new ArrayList<>();
        }
    }
}