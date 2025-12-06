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
        version = 3,
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
                                AppDatabase database = instance;
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
        dao.insertarEjercicio(new Ejercicios("Press de Banca Plano", "Pecho medio con barra", "Pecho", "Barra", "Intermedio", "https://www.youtube.com/watch?v=TAH8RxOS0VI"));
        dao.insertarEjercicio(new Ejercicios("Press de Banca Inclinado", "Parte superior del pecho", "Pecho", "Barra", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Press Declinado con Barra", "Parte inferior del pecho", "Pecho", "Barra", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Press Plano con Mancuernas", "Mayor rango de movimiento para el pectoral", "Pecho", "Mancuernas", "Intermedio", "https://youtu.be/VmB1G1K7v94?t=6"));
        dao.insertarEjercicio(new Ejercicios("Press Inclinado Mancuernas", "Pecho superior con recorrido amplio", "Pecho", "Mancuernas", "Intermedio", "https://youtu.be/VmB1G1K7v94?t=6"));
        dao.insertarEjercicio(new Ejercicios("Press Declinado Mancuernas", "Enfoque en pecho bajo", "Pecho", "Mancuernas", "Intermedio", "https://youtu.be/VmB1G1K7v94?t=6"));
        dao.insertarEjercicio(new Ejercicios("Aperturas en Pec Deck", "Aísla el pectoral completamente", "Pecho", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Aperturas con Mancuernas", "Aísla el pecho manteniendo brazos semirrígidos", "Pecho", "Mancuernas", "Intermedio", "https://youtu.be/8nrLh4GpS7w?t=3"));
        dao.insertarEjercicio(new Ejercicios("Press en Máquina Selector", "Movimiento guiado seguro", "Pecho", "Máquina", "Principiante", "https://www.youtube.com/watch?v=ADqg_IoKhME"));
        dao.insertarEjercicio(new Ejercicios("Press Inclinado en Máquina", "Énfasis en pectoral superior", "Pecho", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Cruces en Polea Alta", "Aísla y trabaja la parte interna del pecho", "Pecho", "Polea", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Cruces en Polea Baja", "Enfoque en pectoral superior", "Pecho", "Polea", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Dips en Paralelas para Pecho", "Fuerte trabajo de pecho inferior", "Pecho", "Paralelas", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Press Horizontal Hammer", "Movimiento muy estable, ideal para progresar", "Pecho", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Aperturas en Máquina Hammer", "Aísla pecho sin necesidad de controlar estabilidad", "Pecho", "Máquina", "Principiante", ""));

        // ============ ESPALDA ============
        dao.insertarEjercicio(new Ejercicios("Jalón al Pecho en Polea Alta", "Clásico para dorsal ancho", "Espalda", "Polea", "Principiante", "https://youtu.be/wIppFAhl6SE?t=7"));
        dao.insertarEjercicio(new Ejercicios("Jalón Trasnuca", "Variante menos usada pero efectiva", "Espalda", "Polea", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Remo Sentado en Máquina", "Espalda media", "Espalda", "Máquina", "Principiante", "https://youtu.be/GZbfZ033f74?t=13"));
        dao.insertarEjercicio(new Ejercicios("Remo Bajo en Polea", "Estimula la espalda media y baja", "Espalda", "Polea", "Intermedio", "https://youtu.be/GZbfZ033f74?t=13"));
        dao.insertarEjercicio(new Ejercicios("Remo Hammer", "Recorrido guiado para dorsales", "Espalda", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Remo con Mancuerna", "Aísla un lado de la espalda", "Espalda", "Mancuernas", "Intermedio", "https://www.youtube.com/watch?v=wIppFAhl6SE"));
        dao.insertarEjercicio(new Ejercicios("Remo con Barra", "Fuerte trabajo de dorsales y trapecio", "Espalda", "Barra", "Intermedio", "https://youtu.be/vT2GjY_Umpw?t=36"));
        dao.insertarEjercicio(new Ejercicios("Pull Over en Polea Alta", "Excelente para dorsales", "Espalda", "Polea", "Intermedio", "https://www.youtube.com/watch?v=z66PPQ_hGsc"));
        dao.insertarEjercicio(new Ejercicios("Peso Muerto Rumano", "Isquios y espalda baja", "Espalda", "Barra", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Peso Muerto Convencional", "Trabajo global de espalda entera", "Espalda", "Barra", "Avanzado", ""));
        dao.insertarEjercicio(new Ejercicios("Hiperextensiones Lumbar", "Espalda baja", "Espalda", "Banco Lumbar", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Jalón en V", "Trabaja dorsales internos", "Espalda", "Polea", "Intermedio", "https://youtu.be/7D2t1XnrW2s?t=5"));
        dao.insertarEjercicio(new Ejercicios("Remo con Barra T", "Espalda media muy fuerte", "Espalda", "Máquina/Barra", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Dominadas Asistidas", "Dorsales y bíceps", "Espalda", "Máquina Asistida", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Dominadas", "Movimiento maestro de espalda", "Espalda", "Peso corporal", "Avanzado", "https://www.youtube.com/watch?v=eGo4IYlbE5g"));

        // ============ PIERNAS ============
        dao.insertarEjercicio(new Ejercicios("Prensa 45°", "Trabajo completo de piernas", "Piernas", "Máquina", "Intermedio", "https://www.youtube.com/watch?v=D1FvjYNX9QI"));
        dao.insertarEjercicio(new Ejercicios("Sentadilla en Smith", "Cuádriceps y glúteos con guía", "Piernas", "Máquina Smith", "Intermedio", "https://youtu.be/LfEhHboTcow?t=12"));
        dao.insertarEjercicio(new Ejercicios("Sentadilla Hack", "Gran estímulo al cuádriceps", "Piernas", "Máquina", "Intermedio", "https://youtu.be/LfEhHboTcow?t=12"));
        dao.insertarEjercicio(new Ejercicios("Sentadilla Hack Invertida", "Enfoque más directo en cuádriceps", "Piernas", "Máquina", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Sentadilla Bulgara", "Unilaterales potentes", "Piernas", "Mancuernas", "Avanzado", "https://youtu.be/2C-uNgKwPLE?t=57"));
        dao.insertarEjercicio(new Ejercicios("Extensión de Piernas", "Aísla cuádriceps", "Piernas", "Máquina", "Principiante", "https://youtu.be/YyvSfVjQeL0?t=11"));
        dao.insertarEjercicio(new Ejercicios("Curl Femoral Sentado", "Isquios con reccorido seguro", "Piernas", "Máquina", "Principiante", "https://youtu.be/ELOCsoDSmrg?t=6"));
        dao.insertarEjercicio(new Ejercicios("Curl Femoral Acostado", "Isquiotibiales", "Piernas", "Máquina", "Principiante", "https://youtu.be/1Tq3QdYUuHs?t=13"));
        dao.insertarEjercicio(new Ejercicios("Prensa Horizontal", "Variante cómoda para todos los niveles", "Piernas", "Máquina", "Principiante", "https://youtu.be/IZxyjW7MPJQ?t=8"));
        dao.insertarEjercicio(new Ejercicios("Peso Muerto Rumano", "Isquios y glúteos", "Piernas", "Barra", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Elevación de Talones en Máquina", "Gemelos", "Piernas", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Elevación de Talones Sentado", "Sóleo", "Piernas", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Zancadas con Mancuernas", "Trabajo unilateral completo", "Piernas", "Mancuernas", "Intermedio", "https://youtu.be/D7KaRcUTQeE?t=19"));
        dao.insertarEjercicio(new Ejercicios("Glute Bridge en Máquina", "Glúteos directos", "Piernas", "Máquina", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Abductores en Máquina", "Trabajo lateral de glúteos", "Piernas", "Máquina", "Principiante", "https://youtu.be/2b97cvyH9sE?t=6"));

        // ============ HOMBROS ============
        dao.insertarEjercicio(new Ejercicios("Press Militar en Máquina", "Hombro frontal", "Hombros", "Máquina", "Principiante", "https://youtu.be/Wqq43dKW1TU?t=6"));
        dao.insertarEjercicio(new Ejercicios("Press Militar con Mancuernas", "Trabajo más libre del deltoide", "Hombros", "Mancuernas", "Intermedio", "https://www.youtube.com/watch?v=o5M9RZ-vWrc"));
        dao.insertarEjercicio(new Ejercicios("Press Militar con Barra", "Potencia y fuerza", "Hombros", "Barra", "Intermedio", "https://www.youtube.com/watch?v=o5M9RZ-vWrc"));
        dao.insertarEjercicio(new Ejercicios("Elevaciones Laterales con Mancuernas", "Deltoide lateral", "Hombros", "Mancuernas", "Principiante", "https://youtu.be/3VcKaXpzqRo?t=18"));
        dao.insertarEjercicio(new Ejercicios("Elevaciones Laterales en Máquina", "Aislamiento completo", "Hombros", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Pájaros con Mancuernas", "Deltoide posterior", "Hombros", "Mancuernas", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Pájaros en Máquina", "Foco directo en posterior", "Hombros", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Press Arnold", "Variación completa del hombro", "Hombros", "Mancuernas", "Intermedio", "https://youtu.be/vj2w851ZHRM?t=76"));
        dao.insertarEjercicio(new Ejercicios("Elevaciones Frontales con Mancuernas", "Deltoide anterior", "Hombros", "Mancuernas", "Principiante", "https://youtu.be/-t7fuZ0KhDA?t=7"));
        dao.insertarEjercicio(new Ejercicios("Elevaciones Frontales con Disco", "Variación popular", "Hombros", "Disco", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Remo al Mentón Barra Z", "Deltoides y trapecio", "Hombros", "Barra", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Remo al Mentón Mancuernas", "Unilateral suave", "Hombros", "Mancuernas", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Elevaciones Laterales en Polea Baja", "Máxima tensión", "Hombros", "Polea", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Face Pull en Polea", "Salud del hombro y posterior", "Hombros", "Polea", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Press Hammer para Hombros", "Movimiento estable y potente", "Hombros", "Máquina", "Principiante", ""));

        // ============ BRAZOS - BÍCEPS ============
        dao.insertarEjercicio(new Ejercicios("Curl con Barra Recta", "Bíceps clásico", "Brazos", "Barra", "Principiante", "https://youtu.be/LY1V6UbRHFM?t=8"));
        dao.insertarEjercicio(new Ejercicios("Curl con Barra Z", "Menos tensión en muñecas", "Brazos", "Barra", "Principiante", "https://youtu.be/LY1V6UbRHFM?t=8"));
        dao.insertarEjercicio(new Ejercicios("Curl Alternado con Mancuernas", "Movimiento natural y cómodo", "Brazos", "Mancuernas", "Principiante", "https://youtu.be/sAq_ocpRh_I?t=7"));
        dao.insertarEjercicio(new Ejercicios("Curl Martillo", "Braquial y antebrazo", "Brazos", "Mancuernas", "Principiante", "https://youtu.be/zC3nLlEvin4?t=20"));
        dao.insertarEjercicio(new Ejercicios("Curl en Banco Scott Máquina", "Aislamiento total", "Brazos", "Máquina", "Principiante", "https://youtu.be/M_uPvGrMx_o?t=6"));
        dao.insertarEjercicio(new Ejercicios("Curl en Banco Scott con Barra", "Variación libre", "Brazos", "Barra", "Intermedio", "https://youtu.be/M_uPvGrMx_o?t=6"));
        dao.insertarEjercicio(new Ejercicios("Curl Concentrado", "Bíceps puro unilateral", "Brazos", "Mancuernas", "Intermedio", "https://youtu.be/wIppFAhl6SE?t=286"));
        dao.insertarEjercicio(new Ejercicios("Curl en Polea Baja", "Tensión continua", "Brazos", "Polea", "Intermedio", "https://youtu.be/wIppFAhl6SE?t=227"));
        dao.insertarEjercicio(new Ejercicios("Curl en Polea Alta", "Variación unilateral", "Brazos", "Polea", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Curl con Cuerda en Polea", "Mayor trabajo de braquial", "Brazos", "Polea", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Curl Tipo Spider", "Mayor estiramiento", "Brazos", "Mancuernas", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Curl en Máquina Hammer", "Recorrido guiado", "Brazos", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Curl de Arrastre", "Tensión distinta, foco interno", "Brazos", "Barra", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Curl de Pie en Máquina", "Ideal para congestión final", "Brazos", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Curl Alternado en Polea Cruzada", "Unilateral suave", "Brazos", "Polea", "Intermedio", ""));

        // ============ BRAZOS - TRÍCEPS ============
        dao.insertarEjercicio(new Ejercicios("Extensión de Tríceps en Polea", "Movimiento básico", "Brazos", "Polea", "Principiante", "https://www.youtube.com/watch?v=_w-HpW70nSQ"));
        dao.insertarEjercicio(new Ejercicios("Tríceps en Polea con Cuerda", "Mayor activación de cabeza larga", "Brazos", "Polea", "Principiante", "https://www.youtube.com/watch?v=_w-HpW70nSQ"));
        dao.insertarEjercicio(new Ejercicios("Press Francés con Barra Z", "Tríceps completos", "Brazos", "Barra", "Intermedio", "https://youtu.be/d_KZxkY_0cM?t=12"));
        dao.insertarEjercicio(new Ejercicios("Press Francés Mancuernas", "Variación unilateral", "Brazos", "Mancuernas", "Intermedio", "https://youtu.be/sOxeVwRaoYc?t=250"));
        dao.insertarEjercicio(new Ejercicios("Extensión Unilateral en Polea Alta", "Control total", "Brazos", "Polea", "Intermedio", "https://www.youtube.com/shorts/SbYr_pGBJlU"));
        dao.insertarEjercicio(new Ejercicios("Fondos en Paralelas", "Trabaja tríceps y pecho", "Brazos", "Paralelas", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Fondos Asistidos", "Versión accesible", "Brazos", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Externsion de Tríceps con Mancuerna", "Aislamiento directo", "Brazos", "Mancuernas", "Intermedio", "https://youtu.be/-Vyt2QdsR7E?t=6"));
        dao.insertarEjercicio(new Ejercicios("Extensión en Máquina", "Movimiento guiado seguro", "Brazos", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Press Cerrado en Smith", "Tríceps potentes", "Brazos", "Máquina Smith", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("Extensión en Polea Baja", "Tensión distinta", "Brazos", "Polea", "Intermedio", "https://www.youtube.com/shorts/SbYr_pGBJlU"));
        dao.insertarEjercicio(new Ejercicios("Extensión de Tríceps al Rostro", "Mayor estiramiento", "Brazos", "Polea", "Intermedio", ""));
        dao.insertarEjercicio(new Ejercicios("JM Press", "Intermedio-avanzado, buen estímulo", "Brazos", "Barra", "Avanzado", ""));
        dao.insertarEjercicio(new Ejercicios("Press en Máquina Cerrado", "Tríceps con recorrido guiado", "Brazos", "Máquina", "Principiante", ""));
        dao.insertarEjercicio(new Ejercicios("Kickback en Polea Baja", "Versión con tensión constante", "Brazos", "Polea", "Intermedio", ""));
    }

}