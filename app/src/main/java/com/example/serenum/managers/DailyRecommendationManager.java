package com.example.serenum.managers;

import com.example.serenum.BreathingActivity;
import com.example.serenum.MeditationActivity;
import com.example.serenum.R;
import com.example.serenum.SoundPlayerActivity;
import com.example.serenum.models.DailyRecommendation;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de recomendaciones del día.
 * - Recomendación del día: Solo BreathingActivity
 * - Contenido destacado: Rota entre todas las recomendaciones
 */
public class DailyRecommendationManager {

    // Lista para "Recomendación del día" - solo BreathingActivity
    private static final List<DailyRecommendation> dailyRecommendationList = new ArrayList<>();

    // Lista para "Contenido destacado" - todas las recomendaciones
    private static final List<DailyRecommendation> featuredRecommendationList = new ArrayList<>();

    static {
        // RECOMENDACIÓN DEL DÍA - Solo BreathingActivity
        dailyRecommendationList.add(new DailyRecommendation(
                "Ejercicio de respiración",
                "1 minuto para centrarte",
                "Inhala, mantén y exhala para recuperar el foco.",
                "exercise",
                BreathingActivity.class,
                R.drawable.cont1,
                null,
                "1 min"
        ));

        // CONTENIDO DESTACADO - Todas las recomendaciones en rotación
        featuredRecommendationList.add(new DailyRecommendation(
                "Meditación del día",
                "Un momento para respirar",
                "Vuelve al presente con una pausa guiada.",
                "meditation",
                MeditationActivity.class,
                R.drawable.cont2,
                "medit1",
                "3 min"
        ));

        featuredRecommendationList.add(new DailyRecommendation(
                "Sonido relajante del día",
                "Escucha y suelta tensión",
                "Deja que un ambiente suave te acompañe.",
                "sound",
                SoundPlayerActivity.class,
                R.drawable.cont3,
                "lluvia",
                "Infinito"
        ));

        featuredRecommendationList.add(new DailyRecommendation(
                "Ejercicio de respiración",
                "1 minuto para centrarte",
                "Inhala, mantén y exhala para recuperar el foco.",
                "exercise",
                BreathingActivity.class,
                R.drawable.cont1,
                null,
                "1 min"
        ));
    }

    /**
     * Obtiene la recomendación del día (solo BreathingActivity)
     */
    public static DailyRecommendation getCurrentRecommendation() {
        long index = (System.currentTimeMillis() / 20000L) % dailyRecommendationList.size();
        return dailyRecommendationList.get((int) index);
    }

    /**
     * Obtiene el contenido destacado (rota entre todas las recomendaciones)
     */
    public static DailyRecommendation getCurrentFeaturedRecommendation() {
        long index = (System.currentTimeMillis() / 20000L) % featuredRecommendationList.size();
        return featuredRecommendationList.get((int) index);
    }

    public static List<DailyRecommendation> getAllRecommendations() {
        return dailyRecommendationList;
    }

    public static List<DailyRecommendation> getAllFeaturedRecommendations() {
        return featuredRecommendationList;
    }
}

