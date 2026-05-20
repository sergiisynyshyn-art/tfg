package com.example.serenum.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * FavoriteManager centraliza la gestion de favoritos en toda la app.
 *
 * Usa SharedPreferences porque es una solucion ligera y persistente para
 * guardar un conjunto pequeno de IDs sin requerir base de datos adicional.
 *
 * Para escalar en el futuro (sincronizacion cloud, metadatos, etc.), se puede
 * reemplazar internamente por Room/Firestore manteniendo esta misma API.
 */
public class FavoriteManager {

    private static final String PREFS_NAME = "favorites_prefs";
    private static final String KEY_FAVORITES = "favorite_ids";

    private static FavoriteManager instance;
    private final SharedPreferences sharedPreferences;

    private FavoriteManager(Context context) {
        Context appContext = context.getApplicationContext();
        sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized FavoriteManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoriteManager(context);
        }
        return instance;
    }

    public synchronized void addFavorite(String id) {
        if (id == null || id.trim().isEmpty()) return;
        Set<String> favorites = new HashSet<>(getAllFavorites());
        favorites.add(id);
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, favorites).apply();
    }

    public synchronized void removeFavorite(String id) {
        if (id == null || id.trim().isEmpty()) return;
        Set<String> favorites = new HashSet<>(getAllFavorites());
        favorites.remove(id);
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, favorites).apply();
    }

    public synchronized void toggleFavorite(String id) {
        if (isFavorite(id)) {
            removeFavorite(id);
        } else {
            addFavorite(id);
        }
    }

    public synchronized boolean isFavorite(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return getAllFavorites().contains(id);
    }

    public synchronized Set<String> getAllFavorites() {
        Set<String> stored = sharedPreferences.getStringSet(KEY_FAVORITES, new HashSet<>());
        return stored == null ? new HashSet<>() : new HashSet<>(stored);
    }
}

