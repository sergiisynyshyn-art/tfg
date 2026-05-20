package com.example.serenum.managers;

import com.example.serenum.R;
import com.example.serenum.models.FeaturedContent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de contenidos destacados
 * TESTING: Rota cada minuto usando minutos actuales
 * PRODUCCIÓN: Cambiar a LocalDate.now().getDayOfYear() para cada 24 horas
 */
public class FeaturedContentManager {

    private static List<FeaturedContent> featuredContents;

    /**
     * Inicializa la lista de contenidos destacados
     */
    private static void initializeFeaturedContents() {
        if (featuredContents != null) {
            return;
        }

        featuredContents = new ArrayList<>();

        // Contenido 1: Paisaje sonoro del día
        featuredContents.add(new FeaturedContent(
                R.drawable.cont1,
                "Montañas al amanecer",
                "Paisaje sonoro del día",
                "Siente la brisa fría y el despertar de la naturaleza.",
                "sound",
                "olas"  // olas.mp3
        ));

        // Contenido 2: Sonido relajante del día
        featuredContents.add(new FeaturedContent(
                R.drawable.cont2,
                "Lluvia suave",
                "Sonido relajante del día",
                "Deja que el sonido de la lluvia limpie tu mente.",
                "sound",
                "lluvia"  // lluvia.mp3
        ));

        // Contenido 3: Momento de calma
        featuredContents.add(new FeaturedContent(
                R.drawable.cont3,
                "Bosque tranquilo",
                "Momento de calma",
                "Respira profundo y conecta con la serenidad del bosque.",
                "sound",
                "bosque"  // bosque.mp3
        ));
    }

    /**
     * Devuelve el contenido destacado
     * TESTING: Cambia cada minuto usando minutos actuales (0-59)
     * PRODUCCIÓN: Cambiar a LocalDate.now().getDayOfYear() para cada 24 horas
     */
    public static FeaturedContent getTodayFeaturedContent() {
        initializeFeaturedContents();

        // TESTING: Usar segundos actuales (cambia cada ~20 segundos en este ejemplo)
        int secondOfMinute = LocalDateTime.now().getSecond();
        int index = (secondOfMinute / 20) % featuredContents.size();

        return featuredContents.get(index);
    }

    /**
     * Devuelve todos los contenidos destacados
     */
    public static List<FeaturedContent> getAllFeaturedContents() {
        initializeFeaturedContents();
        return featuredContents;
    }

    /**
     * Devuelve un contenido por índice
     */
    public static FeaturedContent getFeaturedContentByIndex(int index) {
        initializeFeaturedContents();
        if (index >= 0 && index < featuredContents.size()) {
            return featuredContents.get(index);
        }
        return featuredContents.get(0);
    }
}

