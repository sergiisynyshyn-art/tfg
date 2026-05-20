package com.example.serenum;

import java.io.Serializable;

/**
 * Representa un paso dentro de una rutina.
 * Puede ser: meditación, sonido, o texto.
 */
public class RoutineStep implements Serializable {
    public static final String TYPE_MEDITATION = "meditation";
    public static final String TYPE_SOUND = "sound";
    public static final String TYPE_TEXT = "text";

    private String type; // meditation, sound, text
    private String id; // nombre del archivo (para audio)
    private String content; // texto del paso (para text)
    private String title; // título del paso
    private int imageRes; // imagen asociada

    public RoutineStep(String type, String id, String title, int imageRes) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.imageRes = imageRes;
        this.content = "";
    }

    public RoutineStep(String type, String title, int imageRes, String content) {
        this.type = type;
        this.title = title;
        this.imageRes = imageRes;
        this.content = content;
        this.id = "";
    }

    public RoutineStep(String type, String id, String title, int imageRes, String content) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.imageRes = imageRes;
        this.content = content;
    }

    // Getters y Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}

