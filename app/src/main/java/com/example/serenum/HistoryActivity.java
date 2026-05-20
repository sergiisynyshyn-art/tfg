package com.example.serenum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serenum.adapters.HistoryAdapter;
import com.example.serenum.models.HistoryEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity que muestra todo el historial con RecyclerView visual (tarjetas YouTube-like).
 */
public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageButton btnBack = findViewById(R.id.btnBackHistory);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        RecyclerView rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        List<HistoryEntry> entries = loadHistoryEntries();
        HistoryAdapter adapter = new HistoryAdapter(entries);
        rvHistory.setAdapter(adapter);
    }

    /**
     * Carga historial desde SharedPreferences y lo convierte a HistoryEntry con imágenes.
     */
    private List<HistoryEntry> loadHistoryEntries() {
        List<HistoryEntry> result = new ArrayList<>();
        SharedPreferences histPrefs = getSharedPreferences("profile_history", MODE_PRIVATE);
        java.util.Set<String> stored = histPrefs.getStringSet("history_set", new java.util.HashSet<>());

        for (String entry : stored) {
            String[] parts = entry.split("\\|");
            if (parts.length < 3) continue;
            String type = parts[0];
            String title = parts[1];
            String date = parts[2];
            int imageRes = mapHistoryImageByTitle(type, title);
            result.add(new HistoryEntry(type, title, date, imageRes));
        }

        return result;
    }

    /**
     * Mapea el título/tipo a un drawable para la tarjeta visual.
     */
    private int mapHistoryImageByTitle(String type, String title) {
        if ("Meditacion".equals(type)) {
            if (title.contains("Respiración")) return R.drawable.medit1;
            if (title.contains("estrés")) return R.drawable.medit2;
            if (title.contains("dormir")) return R.drawable.medit3;
            return R.drawable.medit1;
        }
        if ("Sonido".equals(type)) {
            if (title.contains("Lluvia")) return R.drawable.lluvia;
            if (title.contains("Fuego")) return R.drawable.fuego;
            if (title.contains("Bosque")) return R.drawable.bosque;
            if (title.contains("Viento")) return R.drawable.viento;
            if (title.contains("Olas")) return R.drawable.olas;
            return R.drawable.lluvia;
        }
        if ("Rutina".equals(type)) {
            if (title.contains("estrés")) return R.drawable.mount;
            if (title.contains("día")) return R.drawable.mount;
            if (title.contains("Dormir")) return R.drawable.mount;
            return R.drawable.mount;
        }
        return R.drawable.mount; // default
    }
}

