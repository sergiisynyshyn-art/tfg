package com.example.serenum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de Rutina: secuencia guiada que combina meditaciones, sonidos y mensajes.
 */
public class Routine implements Serializable {
    private String id;
    private String title;
    private String duration;
    private String description;
    private int imageRes;
    private List<RoutineStep> steps;

    public Routine(String id, String title, String duration, String description, int imageRes) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.imageRes = imageRes;
        this.steps = new ArrayList<>();
    }

    public Routine(String id, String title, String duration, String description, int imageRes, List<RoutineStep> steps) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.imageRes = imageRes;
        this.steps = steps;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public List<RoutineStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RoutineStep> steps) {
        this.steps = steps;
    }

    public void addStep(RoutineStep step) {
        this.steps.add(step);
    }
}

