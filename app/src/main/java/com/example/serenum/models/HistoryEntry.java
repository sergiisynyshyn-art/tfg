package com.example.serenum.models;

/**
 * Represents a single history entry (activity performed).
 * Data persisted in SharedPreferences and used to populate RecyclerView.
 */
public class HistoryEntry {
    public String type;        // "Meditacion", "Sonido", "Rutina"
    public String title;       // "Respiración consciente"
    public String date;        // "2026-05-18"
    public int imageRes;       // Resource ID like R.drawable.medit1

    public HistoryEntry(String type, String title, String date, int imageRes) {
        this.type = type;
        this.title = title;
        this.date = date;
        this.imageRes = imageRes;
    }
}

