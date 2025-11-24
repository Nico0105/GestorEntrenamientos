package com.example.gestorentrenamientos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorentrenamientos.database.AppDatabase;
import com.example.gestorentrenamientos.database.DatabaseClient;
import com.example.gestorentrenamientos.database.DiaEntrenamiento;
import com.example.gestorentrenamientos.database.Ejercicios;
import com.example.gestorentrenamientos.database.EjerciciosPorDia;
import com.example.gestorentrenamientos.database.Rutinas;
import com.example.gestorentrenamientos.database.RutinasDao;

import java.util.ArrayList;
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
        int userId = obtenerUsuarioLogueado();
        if (userId == -1) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Rutinas> rutinas = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .rutinasDao()
                    .obtenerRutinasDelUsuario(userId);

            runOnUiThread(() -> {
                containerRutinas.removeAllViews(); // 🔹 limpiar antes de agregar

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
        // Primero intentar obtenerlo del Intent
        int userId = getIntent().getIntExtra("user_id", -1);
        if (userId != -1) return userId;

        // Si no viene por Intent, usar SharedPreferences
        return getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getInt("USER_ID", -1);
    }

    private void agregarRutinaCard(Rutinas rutina) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_rutina, containerRutinas, false);

        TextView tvNombreRutina = cardView.findViewById(R.id.tvNombreRutina);
        LinearLayout containerDiasRutina = cardView.findViewById(R.id.containerDiasRutina);

        Button btnEliminarRutina = cardView.findViewById(R.id.btnEliminarRutina);
        Button btnEditar = cardView.findViewById(R.id.btnEditar);

        String nombre = rutina.getNombreRutina();
        if (rutina.isActive()) nombre = "⭐ " + nombre + " (Activa)";
        tvNombreRutina.setText(nombre);

        btnEditar.setOnClickListener(v -> mostrarDialogoEditar(rutina));

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

    private void abrirSeleccionarEjerciciosActivity(DiaEntrenamiento dia, ArrayList<Integer> ejerciciosPrevios) {
        Intent intent = new Intent(this, SeleccionarEjerciciosActivity.class);
        intent.putExtra("DIA_INDEX", dia.getId());
        intent.putIntegerArrayListExtra("EJERCICIOS_PREVIOS", ejerciciosPrevios != null ? ejerciciosPrevios : new ArrayList<>());
        startActivityForResult(intent, dia.getId());
    }

    private void abrirSeleccionEjercicios(Rutinas rutina, boolean agregarNuevoDia) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
            List<DiaEntrenamiento> dias = db.diaEntrenamientoDao().obtenerDiasDeRutina(rutina.getId());

            runOnUiThread(() -> {
                if (agregarNuevoDia) {
                    agregarDiaManual(rutina);
                } else if (!dias.isEmpty()) {
                    String[] nombresDias = new String[dias.size()];
                    for (int i = 0; i < dias.size(); i++) {
                        nombresDias[i] = "Día " + dias.get(i).getDias() + " - " + dias.get(i).getDiaDe();
                    }

                    new AlertDialog.Builder(this)
                            .setTitle("Selecciona el día a editar")
                            .setItems(nombresDias, (dialog, which) -> {
                                DiaEntrenamiento dia = dias.get(which);

                                // ¡Esta parte debe ir en otro thread!
                                Executors.newSingleThreadExecutor().execute(() -> {
                                    List<EjerciciosPorDia> epdList = db.ejerciciosPorDiaDao().obtenerEjerciciosDelDia(dia.getId());
                                    ArrayList<Integer> ejerciciosPrevios = new ArrayList<>();
                                    for (EjerciciosPorDia epd : epdList) ejerciciosPrevios.add(epd.getEjercicioId());

                                    runOnUiThread(() -> abrirSeleccionarEjerciciosActivity(dia, ejerciciosPrevios));
                                });
                            })
                            .show();
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            int diaId = requestCode;
            ArrayList<Integer> ejerciciosSeleccionados = data.getIntegerArrayListExtra("EJERCICIOS_IDS");

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
                List<EjerciciosPorDia> actuales = db.ejerciciosPorDiaDao().obtenerEjerciciosDelDia(diaId);

                if (ejerciciosSeleccionados.isEmpty()) {
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(this)
                                .setTitle("Eliminar Día")
                                .setMessage("Desmarcaste todos los ejercicios de este día. ¿Deseas eliminar el día de tu rutina?")
                                .setPositiveButton("Sí", (dialog, which) -> {
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        // aca borro todos los ejercicios y el día
                                        db.ejerciciosPorDiaDao().eliminarTodosLosEjerciciosDelDia(diaId);
                                        DiaEntrenamiento dia = db.diaEntrenamientoDao().obtenerDiaPorId(diaId);
                                        if (dia != null) db.diaEntrenamientoDao().eliminarDia(dia);

                                        runOnUiThread(() -> {
                                            Toast.makeText(this, "✅ Día eliminado", Toast.LENGTH_SHORT).show();
                                            containerRutinas.removeAllViews();
                                            cargarRutinas();
                                        });
                                    });
                                })
                                .setNegativeButton("No", null)
                                .show();
                    });
                    return; // salir del hilo
                }

                //actualizas los ejercicios del día
                List<Integer> actualesIds = new ArrayList<>();
                for (EjerciciosPorDia epd : actuales) actualesIds.add(epd.getEjercicioId());

                // Eliminar los que ya no están
                for (EjerciciosPorDia epd : actuales) {
                    if (!ejerciciosSeleccionados.contains(epd.getEjercicioId())) {
                        db.ejerciciosPorDiaDao().eliminarEjercicioPorDia(epd);
                    }
                }

                // Agregar los nuevos
                int orderIndex = 1;
                for (int idEjercicio : ejerciciosSeleccionados) {
                    if (!actualesIds.contains(idEjercicio)) {
                        EjerciciosPorDia nuevo = new EjerciciosPorDia(diaId, idEjercicio, 3, 10, 8, 0);
                        nuevo.setOrderIndex(orderIndex++);
                        db.ejerciciosPorDiaDao().insertarEjercicioPorDia(nuevo);
                    }
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Ejercicios actualizados", Toast.LENGTH_SHORT).show();
                    containerRutinas.removeAllViews();
                    cargarRutinas();
                });
            });
        }
    }

    private void agregarDiaManual(Rutinas rutina) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar día");

        String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        final int[] diaSeleccionado = {0};

        builder.setSingleChoiceItems(diasSemana, 0, (dialog, which) -> diaSeleccionado[0] = which);

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String nombreDia = diasSemana[diaSeleccionado[0]];

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
                DiaEntrenamiento nuevoDia = new DiaEntrenamiento(rutina.getId(), diaSeleccionado[0] + 1, nombreDia);
                long diaId = db.diaEntrenamientoDao().insertarDia(nuevoDia);
                nuevoDia.setId((int) diaId);

                runOnUiThread(() -> abrirSeleccionarEjerciciosActivity(nuevoDia, null));
            });
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private  void mostrarDialogoEditar(Rutinas rutina){
        String[] opciones = {"Cambiar nombre", "Agregar/quitar ejercicios", "Agregar nuevo día"};
        new AlertDialog.Builder(this)
                .setTitle("Editar Rutina")
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            mostrarDialogoEditarNombre(rutina);
                            break;
                        case 1:
                            abrirSeleccionEjercicios(rutina, false); // editar ejercicios existentes
                            break;
                        case 2:
                            agregarDiaManual(rutina);
                            break;
                    }
                })
                .show();

    }

    private void abrirDialogoSeriesReps(int ejercicioPorDiaId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
            EjerciciosPorDia epd = db.ejerciciosPorDiaDao().obtenerEjercicioPorDiaPorId(ejercicioPorDiaId);
            if (epd == null) return;

            runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Editar Series, Reps y Peso");

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(16,16,16,16);

                EditText etSets = new EditText(this);
                etSets.setHint("Sets");
                etSets.setInputType(InputType.TYPE_CLASS_NUMBER);
                etSets.setText(String.valueOf(epd.getSets()));
                layout.addView(etSets);

                EditText etRepsMin = new EditText(this);
                etRepsMin.setHint("Reps mínimas");
                etRepsMin.setInputType(InputType.TYPE_CLASS_NUMBER);
                etRepsMin.setText(String.valueOf(epd.getRepsMin()));
                layout.addView(etRepsMin);

                EditText etRepsMax = new EditText(this);
                etRepsMax.setHint("Reps máximas");
                etRepsMax.setInputType(InputType.TYPE_CLASS_NUMBER);
                etRepsMax.setText(String.valueOf(epd.getRepsMax()));
                layout.addView(etRepsMax);

                EditText etPeso = new EditText(this);
                etPeso.setHint("Peso (kg)");
                etPeso.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                etPeso.setText(String.valueOf(epd.getPesoKg()));
                layout.addView(etPeso);

                builder.setView(layout);

                AlertDialog dialog = builder.create();
                dialog.show();

                // Custom botón "Guardar" que no cierra automáticamente
                Button btnGuardar = new Button(this);
                btnGuardar.setText("Guardar");
                layout.addView(btnGuardar);

                btnGuardar.setOnClickListener(v -> {
                    int sets = Integer.parseInt(etSets.getText().toString());
                    int repsMin = Integer.parseInt(etRepsMin.getText().toString());
                    int repsMax = Integer.parseInt(etRepsMax.getText().toString());
                    float peso = Float.parseFloat(etPeso.getText().toString());

                    agregarSeriesReps(epd.getId(), sets, repsMin, repsMax, peso);

                    // Cierra el diálogo después de actualizar
                    dialog.dismiss();
                });
            });
        });
    }

    private void actualizarEjercicioEnUI(EjerciciosPorDia epd) {
        // Iteramos sobre todas las cards de rutinas
        for (int i = 0; i < containerRutinas.getChildCount(); i++) {
            View cardView = containerRutinas.getChildAt(i);
            LinearLayout containerDias = cardView.findViewById(R.id.containerDiasRutina);

            // Iteramos los días
            for (int j = 0; j < containerDias.getChildCount(); j++) {
                LinearLayout diaLayout = (LinearLayout) containerDias.getChildAt(j);

                // Iteramos los ejercicios de ese día
                for (int k = 0; k < diaLayout.getChildCount(); k++) {

                    View hijo = diaLayout.getChildAt(k);
                    if (!(hijo instanceof LinearLayout)) continue;

                    LinearLayout ejercicioLayout = (LinearLayout) hijo;

                    if (ejercicioLayout.getChildCount() < 2) continue;

                    TextView tvEjercicio = (TextView) ejercicioLayout.getChildAt(0);

                    if (tvEjercicio.getTag() != null && (int) tvEjercicio.getTag() == epd.getId()) {

                        String repsText = epd.getRepsMin() + "-" + epd.getRepsMax();

                        Ejercicios ejercicio = DatabaseClient.getInstance(getApplicationContext())
                                .getAppDatabase()
                                .ejerciciosDao()
                                .obtenerEjercicioPorId(epd.getEjercicioId());

                        if (ejercicio != null) {
                            tvEjercicio.setText(
                                    ejercicio.getName() + " - " +
                                            epd.getSets() + " x " + repsText + " / " + epd.getPesoKg() + "kg"
                            );
                        }

                        return;
                    }
                }

            }
        }
    }

    private void agregarSeriesReps(int ejercicioPorDiaId, int sets, int repsMin, int repsMax, float peso){
        Executors.newSingleThreadExecutor().execute(() ->{
            AppDatabase db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
            EjerciciosPorDia epd = db.ejerciciosPorDiaDao().obtenerEjercicioPorDiaPorId(ejercicioPorDiaId);
            if (epd != null) {
                epd.setSets(sets);
                epd.setRepsMin(repsMin);
                epd.setRepsMax(repsMax);
                epd.setPesoKg(peso);

                db.ejerciciosPorDiaDao().actualizarEjercicioPorDia(epd);

                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Ejercicio actualizado", Toast.LENGTH_SHORT).show();

                    // 🔹 Aquí actualizamos solo el layout del día
                    actualizarEjercicioEnUI(epd);
                });
            } else {
                runOnUiThread(() ->
                        Toast.makeText(this, "⚠ Error: ejercicio no encontrado", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void mostrarDialogoEditarNombre(Rutinas rutina){
        EditText input = new EditText(this);
        input.setText(rutina.getNombreRutina());

        new AlertDialog.Builder(this)
                .setTitle("Editar Rutina")
                .setMessage("Cambia el nombre de la rutina:")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoNombre = input.getText().toString().trim();
                    if (!nuevoNombre.isEmpty()) {
                        editarRutina(rutina, nuevoNombre);
                    } else {
                        Toast.makeText(this, "⚠ El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void editarRutina(Rutinas rutina, String nuevoNombre) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
            rutina.setNombreRutina(nuevoNombre);
            db.rutinasDao().actualizarRutina(rutina);

            runOnUiThread(() -> {
                Toast.makeText(this, "Rutina Actualizada ✔", Toast.LENGTH_SHORT).show();
                containerRutinas.removeAllViews(); // limpiar cards
                cargarRutinas(); // recargar la lista actualizada
            });
        });
    }

    private void cargarEjerciciosDia(int diaId, LinearLayout container) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<EjerciciosPorDia> ejerciciosPorDia = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .ejerciciosPorDiaDao()
                    .obtenerEjerciciosDelDia(diaId);

            if (ejerciciosPorDia == null || ejerciciosPorDia.isEmpty()) return;

            List<Ejercicios> listaEjercicios = new ArrayList<>();
            for (EjerciciosPorDia epd : ejerciciosPorDia) {
                Ejercicios ejercicio = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .ejerciciosDao()
                        .obtenerEjercicioPorId(epd.getEjercicioId());
                listaEjercicios.add(ejercicio);
            }

            runOnUiThread(() -> {
                for (int i = 0; i < ejerciciosPorDia.size(); i++) {
                    EjerciciosPorDia epd = ejerciciosPorDia.get(i);
                    Ejercicios ejercicio = listaEjercicios.get(i);
                    if (ejercicio == null) continue;

                    LinearLayout ejercicioLayout = new LinearLayout(this);
                    ejercicioLayout.setOrientation(LinearLayout.HORIZONTAL);
                    ejercicioLayout.setPadding(16, 4, 16, 4);

                    TextView tvEjercicio = new TextView(this);
                    String repsText = epd.getRepsMin() + "-" + epd.getRepsMax();
                    tvEjercicio.setText(ejercicio.getName() + " - " + epd.getSets() + " x " + repsText + " / " + epd.getPesoKg() + "kg");
                    tvEjercicio.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    ejercicioLayout.addView(tvEjercicio);


                    Button btnEditarSeries = new Button(this);
                    btnEditarSeries.setText("🏋️‍♂️");
                    btnEditarSeries.setOnClickListener(v -> abrirDialogoSeriesReps(epd.getId()));
                    ejercicioLayout.addView(btnEditarSeries);

                    container.addView(ejercicioLayout);
                }
            });
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
