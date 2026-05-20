package com.example.serenum.models;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Modelo de recomendación del día para la pantalla principal.
 */
public class DailyRecommendation {
    private final String title;
    private final String subtitle;
    private final String description;
    private final String type; // meditation, sound, exercise
    private final Class<? extends AppCompatActivity> targetActivity;
    private final int imageResId;
    private final String audioName;
    private final String duration;

    public DailyRecommendation(
            String title,
            String subtitle,
            String description,
            String type,
            Class<? extends AppCompatActivity> targetActivity,
            int imageResId,
            String audioName,
            String duration) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.type = type;
        this.targetActivity = targetActivity;
        this.imageResId = imageResId;
        this.audioName = audioName;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public Class<? extends AppCompatActivity> getTargetActivity() { return targetActivity; }
    public int getImageResId() { return imageResId; }
    public String getAudioName() { return audioName; }
    public String getDuration() { return duration; }
}

