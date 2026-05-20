package com.example.serenum.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * SessionManager registra actividades completadas en SharedPreferences.
 * Incrementa counters de progreso y añade entrada al historial.
 */
public class SessionManager {

    private static final String PREF_PROGRESS = "profile_progress";
    private static final String PREF_HISTORY = "profile_history";

    private static SharedPreferences progressPrefs;
    private static SharedPreferences historyPrefs;

    public SessionManager(Context context) {
        Context appContext = context.getApplicationContext();
        progressPrefs = appContext.getSharedPreferences(PREF_PROGRESS, Context.MODE_PRIVATE);
        historyPrefs = appContext.getSharedPreferences(PREF_HISTORY, Context.MODE_PRIVATE);
    }

    /**
     * Registra una sesión completada.
     *
     * @param type       "Meditacion", "Sonido", "Rutina"
     * @param title      Nombre de la actividad (ej: "Respiración consciente")
     * @param durationMinutes  Duración en minutos
     */
    public void recordSession(String type, String title, int durationMinutes) {
        // 1. Incrementar sesiones completadas
        int sessionsCompleted = progressPrefs.getInt("sessions_completed", 0);
        progressPrefs.edit().putInt("sessions_completed", sessionsCompleted + 1).apply();

        // 2. Incrementar tiempo total
        int totalMinutes = progressPrefs.getInt("total_minutes", 0);
        progressPrefs.edit().putInt("total_minutes", totalMinutes + durationMinutes).apply();

        // 3. Incrementar racha (simplificado: +1 día por sesión)
        int streakDays = progressPrefs.getInt("streak_days", 0);
        progressPrefs.edit().putInt("streak_days", streakDays + 1).apply();

        // 4. Añadir al historial
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        String dateStr = sdf.format(today);
        String entry = type + "|" + title + "|" + dateStr;

        Set<String> historySet = historyPrefs.getStringSet("history_set", new HashSet<>());
        historySet.add(entry);
        historyPrefs.edit().putStringSet("history_set", historySet).apply();
    }
}

