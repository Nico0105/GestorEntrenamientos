package com.example.gestorentrenamientos.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(
        entities = {
                User.class,
                Ejercicios.class,
                Rutinas.class,
                DiaEntrenamiento.class,
                EjerciciosPorDia.class
        },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "rutinas.db"
                    )
                    .fallbackToDestructiveMigration()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            // Cargamos ejercicios en un hilo separado sin usar 'instance'
                            Executors.newSingleThreadExecutor().execute(() -> {
                                AppDatabase database = getInstance(context); // ahora sí hay instancia
                                cargarEjerciciosPredefinidos(database.ejerciciosDao());
                            });
                        }
                    })
                    .build();
        }
        return instance;
    }

    // DAOS
    public abstract UserDAO userDao();
    public abstract EjerciciosDao ejerciciosDao();
    public abstract RutinasDao rutinasDao();
    public abstract DiaEntrenamientoDao diaEntrenamientoDao();
    public abstract EjerciciosPorDiaDao ejerciciosPorDiaDao();


    private static void cargarEjerciciosPredefinidos(EjerciciosDao dao) {
        // ============ PECHO ============
        dao.insertarEjercicio(new Ejercicios("Press de Banca Plano", "Ejercicio fundamental para desarrollo de pecho medio", "Pecho", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Press Inclinado con Mancuernas", "Enfoque en pecho superior", "Pecho", "Mancuernas", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Press Declinado", "Trabajo de pecho inferior", "Pecho", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Aperturas con Mancuernas", "Aislamiento y estiramiento de pecho", "Pecho", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Fondos en Paralelas con Peso", "Ejercicio compuesto para pecho inferior y tríceps", "Pecho", "Peso Libre", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Cruces en Polea", "Aislamiento de pecho con tensión constante", "Pecho", "Polea", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Press en Máquina Smith", "Desarrollo de pecho seguro", "Pecho", "Máquina", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Press con Bandas Elásticas", "Variación con resistencia progresiva", "Pecho", "Bandas", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Flexiones con Lastre", "Peso corporal más resistencia extra", "Pecho", "Peso Libre", "Intermedio"));

        // ============ ESPALDA ============
        dao.insertarEjercicio(new Ejercicios("Peso Muerto", "Ejercicio fundamental para espalda baja y cadena posterior", "Espalda", "Barra", "Avanzado"));
        dao.insertarEjercicio(new Ejercicios("Remo con Barra", "Desarrollo de espalda media y grosor", "Espalda", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Remo con Mancuerna", "Trabajo unilateral de espalda", "Espalda", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Remo en Polea Baja", "Trabajo de espalda media con cable", "Espalda", "Polea", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Jalón al Pecho", "Alternativa a dominadas para principiantes", "Espalda", "Polea", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Pullover con Mancuerna", "Expansión de caja torácica y dorsales", "Espalda", "Mancuernas", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Remo T-Bar", "Trabajo de grosor de espalda", "Espalda", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Remo en Máquina Sentado", "Aislamiento de espalda media", "Espalda", "Máquina", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Dominadas lastradas", "Desarrollo completo de espalda y fuerza", "Espalda", "Peso Libre", "Avanzado"));
        dao.insertarEjercicio(new Ejercicios("Pull-over en Polea Alta", "Expansión de dorsales y pecho", "Espalda", "Polea", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Remo con Banda Elástica", "Trabajo de dorsales en casa o gimnasio", "Espalda", "Bandas", "Principiante"));

        // ============ PIERNAS ============
        dao.insertarEjercicio(new Ejercicios("Sentadilla con Barra", "Ejercicio rey para desarrollo de piernas", "Piernas", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Sentadilla Frontal", "Mayor énfasis en cuádriceps", "Piernas", "Barra", "Avanzado"));
        dao.insertarEjercicio(new Ejercicios("Prensa de Piernas", "Ejercicio seguro para cuádriceps", "Piernas", "Máquina", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Peso Muerto Rumano", "Desarrollo de femorales y glúteos", "Piernas", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Zancadas con Mancuernas", "Ejercicio unilateral completo", "Piernas", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Extensión de Cuádriceps", "Aislamiento de cuádriceps", "Piernas", "Máquina", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Curl Femoral", "Aislamiento de femorales", "Piernas", "Máquina", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Hip Thrust con Barra", "Trabajo específico de glúteos", "Piernas", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Elevación de Gemelos de Pie", "Desarrollo de gemelos", "Piernas", "Máquina", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Step-ups con Mancuernas", "Trabajo unilateral de piernas y glúteos", "Piernas", "Mancuernas", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Sentadilla Sumo con Mancuerna", "Enfoque en aductores y glúteos", "Piernas", "Mancuernas", "Intermedio"));

        // ============ HOMBROS ============
        dao.insertarEjercicio(new Ejercicios("Press Militar", "Desarrollo general de hombros", "Hombros", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Press con Mancuernas", "Mayor rango de movimiento que con barra", "Hombros", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Elevaciones Laterales", "Aislamiento de deltoides laterales", "Hombros", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Elevaciones Frontales", "Trabajo de deltoides anterior", "Hombros", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Pájaros", "Aislamiento de deltoides posterior", "Hombros", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Face Pulls", "Deltoides posterior y salud del hombro", "Hombros", "Polea", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Remo al Mentón", "Trabajo de deltoides y trapecios", "Hombros", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Elevaciones en Máquina", "Aislamiento de hombros seguro", "Hombros", "Máquina", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Press Arnold", "Desarrollo completo de hombros y deltoides", "Hombros", "Mancuernas", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Elevaciones Laterales en Polea", "Tensión constante en deltoides laterales", "Hombros", "Polea", "Intermedio"));

        // ============ BRAZOS - BÍCEPS ============
        dao.insertarEjercicio(new Ejercicios("Curl con Barra Z", "Desarrollo clásico de bíceps", "Brazos", "Barra", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Curl con Mancuernas", "Trabajo alternado de bíceps", "Brazos", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Curl Martillo", "Trabajo de bíceps y braquial", "Brazos", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Curl Concentrado", "Aislamiento máximo de bíceps", "Brazos", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Curl en Predicador", "Trabajo estricto de bíceps", "Brazos", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Curl 21s", "Técnica avanzada de 21 repeticiones", "Brazos", "Barra", "Avanzado"));

        // ============ BRAZOS - TRÍCEPS ============
        dao.insertarEjercicio(new Ejercicios("Press Francés", "Ejercicio clásico de tríceps", "Brazos", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Extensiones en Polea", "Aislamiento de tríceps con cable", "Brazos", "Polea", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Fondos para Tríceps con Peso", "Ejercicio compuesto de tríceps", "Brazos", "Peso Libre", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Patada de Tríceps", "Aislamiento unilateral", "Brazos", "Mancuernas", "Principiante"));
        dao.insertarEjercicio(new Ejercicios("Press Cerrado", "Trabajo compuesto de tríceps", "Brazos", "Barra", "Intermedio"));
        dao.insertarEjercicio(new Ejercicios("Rompecráneos", "Variación avanzada de tríceps", "Brazos", "Barra", "Avanzado"));
    }

}