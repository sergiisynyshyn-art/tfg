package com.example.serenum.models;

/**
 * Modelo para representar un contenido destacado
 */
public class FeaturedContent {
    public int imageResId;
    public String title;
    public String subtitle;
    public String description;
    public String contentType;  // "meditation" o "sound"
    public String contentId;    // ID del contenido asociado (medit1, lluvia, etc)

    public FeaturedContent(int imageResId, String title, String subtitle,
                          String description, String contentType, String contentId) {
        this.imageResId = imageResId;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.contentType = contentType;
        this.contentId = contentId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}

