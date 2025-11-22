package com.example.gestorentrenamientos.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {
        User.class,
        Ejercicios.class,
        Rutinas.class,
        DiaEntrenamiento.class,
        EjerciciosPorDia.class
}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDAO userDao();
    public abstract EjerciciosDao ejerciciosDao();
    public abstract RutinasDao rutinasDao();
    public abstract DiaEntrenamientoDao diaEntrenamientoDao();
    public abstract EjerciciosPorDiaDao ejerciciosPorDiaDao();

    // Callback para insertar ejercicios cuando se crea la BD
    static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Insertar ejercicios predefinidos en segundo plano
            Executors.newSingleThreadExecutor().execute(() -> {
                // Obtener instancia de la BD (ya está creada en este punto)
                AppDatabase database = DatabaseClient.getInstanceStatic();
                if (database != null) {
                    cargarEjerciciosPredefinidos(database.ejerciciosDao());
                }
            });
        }
    };

    private static void cargarEjerciciosPredefinidos(EjerciciosDao dao) {
        // ============ PECHO ============
        dao.insertarEjercicio(new Ejercicios(
                "Press de Banca Plano",
                "Ejercicio fundamental para desarrollo de pecho medio",
                "Pecho", "Barra", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Press Inclinado con Mancuernas",
                "Enfoque en pecho superior",
                "Pecho", "Mancuernas", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Press Declinado",
                "Trabajo de pecho inferior",
                "Pecho", "Barra", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Aperturas con Mancuernas",
                "Aislamiento y estiramiento de pecho",
                "Pecho", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Fondos en Paralelas",
                "Ejercicio compuesto para pecho inferior y tríceps",
                "Pecho", "Peso Corporal", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Cruces en Polea",
                "Aislamiento de pecho con tensión constante",
                "Pecho", "Polea", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Flexiones",
                "Ejercicio básico sin equipo",
                "Pecho", "Peso Corporal", "Principiante"
        ));

        // ============ ESPALDA ============
        dao.insertarEjercicio(new Ejercicios(
                "Peso Muerto",
                "Ejercicio fundamental para espalda baja y cadena posterior",
                "Espalda", "Barra", "Avanzado"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Dominadas",
                "Ejercicio rey de tracción vertical",
                "Espalda", "Peso Corporal", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Remo con Barra",
                "Desarrollo de espalda media y grosor",
                "Espalda", "Barra", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Jalón al Pecho",
                "Alternativa a dominadas para principiantes",
                "Espalda", "Polea", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Remo con Mancuerna",
                "Trabajo unilateral de espalda",
                "Espalda", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Pullover con Mancuerna",
                "Expansión de caja torácica y dorsales",
                "Espalda", "Mancuernas", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Remo en Polea Baja",
                "Trabajo de espalda media con cable",
                "Espalda", "Polea", "Principiante"
        ));

        // ============ PIERNAS ============
        dao.insertarEjercicio(new Ejercicios(
                "Sentadilla con Barra",
                "Ejercicio rey para desarrollo de piernas",
                "Piernas", "Barra", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Sentadilla Frontal",
                "Mayor énfasis en cuádriceps",
                "Piernas", "Barra", "Avanzado"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Peso Muerto Rumano",
                "Desarrollo de femorales y glúteos",
                "Piernas", "Barra", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Prensa de Piernas",
                "Ejercicio seguro para cuádriceps",
                "Piernas", "Máquina", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Zancadas",
                "Ejercicio unilateral completo",
                "Piernas", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Extensión de Cuádriceps",
                "Aislamiento de cuádriceps",
                "Piernas", "Máquina", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Curl Femoral",
                "Aislamiento de femorales",
                "Piernas", "Máquina", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Elevación de Gemelos de Pie",
                "Desarrollo de gemelos",
                "Piernas", "Máquina", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Hip Thrust",
                "Trabajo específico de glúteos",
                "Piernas", "Barra", "Intermedio"
        ));

        // ============ HOMBROS ============
        dao.insertarEjercicio(new Ejercicios(
                "Press Militar",
                "Desarrollo general de hombros",
                "Hombros", "Barra", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Press con Mancuernas",
                "Mayor rango de movimiento que con barra",
                "Hombros", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Elevaciones Laterales",
                "Aislamiento de deltoides laterales",
                "Hombros", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Elevaciones Frontales",
                "Trabajo de deltoides anterior",
                "Hombros", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Pájaros",
                "Aislamiento de deltoides posterior",
                "Hombros", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Face Pulls",
                "Deltoides posterior y salud del hombro",
                "Hombros", "Polea", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Remo al Mentón",
                "Trabajo de deltoides y trapecios",
                "Hombros", "Barra", "Intermedio"
        ));

        // ============ BRAZOS - BÍCEPS ============
        dao.insertarEjercicio(new Ejercicios(
                "Curl con Barra Z",
                "Desarrollo clásico de bíceps",
                "Brazos", "Barra", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Curl con Mancuernas",
                "Trabajo alternado de bíceps",
                "Brazos", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Curl Martillo",
                "Trabajo de bíceps y braquial",
                "Brazos", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Curl Concentrado",
                "Aislamiento máximo de bíceps",
                "Brazos", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Curl en Predicador",
                "Trabajo estricto de bíceps",
                "Brazos", "Barra", "Intermedio"
        ));

        // ============ BRAZOS - TRÍCEPS ============
        dao.insertarEjercicio(new Ejercicios(
                "Press Francés",
                "Ejercicio clásico de tríceps",
                "Brazos", "Barra", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Extensiones en Polea",
                "Aislamiento de tríceps con cable",
                "Brazos", "Polea", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Fondos para Tríceps",
                "Ejercicio con peso corporal",
                "Brazos", "Peso Corporal", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Patada de Tríceps",
                "Aislamiento unilateral",
                "Brazos", "Mancuernas", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Press Cerrado",
                "Trabajo compuesto de tríceps",
                "Brazos", "Barra", "Intermedio"
        ));

        // ============ CORE/ABDOMEN ============
        dao.insertarEjercicio(new Ejercicios(
                "Plancha",
                "Ejercicio isométrico fundamental",
                "Core", "Peso Corporal", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Crunches",
                "Ejercicio básico de abdomen",
                "Core", "Peso Corporal", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Elevación de Piernas",
                "Trabajo de abdomen inferior",
                "Core", "Peso Corporal", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Abdominales en Bicicleta",
                "Trabajo de oblicuos",
                "Core", "Peso Corporal", "Principiante"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Rotaciones Rusas",
                "Trabajo rotacional de core",
                "Core", "Mancuernas", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Plancha Lateral",
                "Trabajo específico de oblicuos",
                "Core", "Peso Corporal", "Intermedio"
        ));

        dao.insertarEjercicio(new Ejercicios(
                "Ab Wheel",
                "Ejercicio avanzado de core",
                "Core", "Equipamiento", "Avanzado"
        ));
    }
}