package com.example.serenum;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de datos de rutinas. Proporciona las 3 rutinas iniciales.
 */
public class RoutinesDataManager {

    /**
     * Obtiene todas las rutinas disponibles.
     */
    public static List<Routine> getAllRoutines() {
        List<Routine> routines = new ArrayList<>();
        routines.add(getStressReliefRoutine());
        routines.add(getStartDayRoutine());
        routines.add(getSleepBetterRoutine());
        return routines;
    }

    /**
     * Rutina 1: Reducir estrés (5 min)
     */
    public static Routine getStressReliefRoutine() {
        Routine routine = new Routine(
                "stress_relief",
                "Reducir estrés",
                "5 min",
                "Una secuencia guiada para calmar tu mente y cuerpo.",
                R.drawable.medit1
        );

        // Paso 1: Meditación - Respiración consciente
        RoutineStep step1 = new RoutineStep(
                RoutineStep.TYPE_MEDITATION,
                "medit1",
                "Respiración consciente",
                R.drawable.medit1
        );

        // Paso 2: Sonido - Lluvia
        RoutineStep step2 = new RoutineStep(
                RoutineStep.TYPE_SOUND,
                "lluvia",
                "Sonido de lluvia",
                R.drawable.meditation_image_placeholder
        );

        // Paso 3: Texto - Mensaje final
        RoutineStep step3 = new RoutineStep(
                RoutineStep.TYPE_TEXT,
                "message_calm",
                "Mensaje de calma",
                R.drawable.meditation_image_placeholder,
                "Respira profundamente. Ya has hecho un excelente trabajo cuidando de ti mismo. La calma está dentro de ti, y ahora está más cerca que nunca. Sigue adelante con esta paz en tu corazón."
        );

        routine.addStep(step1);
        routine.addStep(step2);
        routine.addStep(step3);

        return routine;
    }

    /**
     * Rutina 2: Empezar el día (5-6 min)
     */
    public static Routine getStartDayRoutine() {
        Routine routine = new Routine(
                "start_day",
                "Empezar el día",
                "5-6 min",
                "Activa tu mente y cuerpo cuando comienza el día.",
                R.drawable.medit2
        );

        // Paso 1: Texto - Activación suave
        RoutineStep step1 = new RoutineStep(
                RoutineStep.TYPE_TEXT,
                "morning_activation",
                "Activación matutina",
                R.drawable.medit2,
                "Buenos días. Hoy es un nuevo comienzo lleno de posibilidades. Abre los ojos lentamente, estira tu cuerpo, y siéntete lleno de energía y propósito. ¡Estás listo para conquistar el día!"
        );

        // Paso 2: Meditación - Medit2
        RoutineStep step2 = new RoutineStep(
                RoutineStep.TYPE_MEDITATION,
                "medit2",
                "Meditación energizante",
                R.drawable.medit2
        );

        // Paso 3: Sonido - Bosque
        RoutineStep step3 = new RoutineStep(
                RoutineStep.TYPE_SOUND,
                "bosque",
                "Sonido del bosque",
                R.drawable.meditation_image_placeholder
        );

        routine.addStep(step1);
        routine.addStep(step2);
        routine.addStep(step3);

        return routine;
    }

    /**
     * Rutina 3: Dormir mejor (6 min)
     */
    public static Routine getSleepBetterRoutine() {
        Routine routine = new Routine(
                "sleep_better",
                "Dormir mejor",
                "6 min",
                "Prepárate para una noche de descanso profundo y reparador.",
                R.drawable.dormir
        );

        // Paso 1: Meditación - Relajación corporal (medit3)
        RoutineStep step1 = new RoutineStep(
                RoutineStep.TYPE_MEDITATION,
                "medit3",
                "Relajación corporal",
                R.drawable.dormir
        );

        // Paso 2: Sonido - Fuego
        RoutineStep step2 = new RoutineStep(
                RoutineStep.TYPE_SOUND,
                "fuego",
                "Sonido de fuego",
                R.drawable.meditation_image_placeholder
        );

        // Paso 3: Texto - Inducción del sueño
        RoutineStep step3 = new RoutineStep(
                RoutineStep.TYPE_TEXT,
                "sleep_induction",
                "Mensaje para el sueño",
                R.drawable.dormir,
                "Tu cuerpo se siente pesado y cómodo. Tus párpados son cada vez más pesados. Con cada respiración, te adentras más profundamente en el sueño. Que descanses y recuperes toda tu energía. Buenas noches."
        );

        routine.addStep(step1);
        routine.addStep(step2);
        routine.addStep(step3);

        return routine;
    }
}

