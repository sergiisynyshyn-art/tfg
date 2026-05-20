package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MeditationsActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_DURATION = "duration";
    private static final String EXTRA_DESCRIPTION = "description";
    private static final String EXTRA_AUDIO_NAME = "audio_name";
    private static final String EXTRA_IMAGE_RES = "image_res";

    // IDs usados para persistir favoritos (concuerdan con FavoritesActivity)
    private static final String ID_MEDIT1 = "med_medit1";
    private static final String ID_MEDIT2 = "med_medit2";
    private static final String ID_MEDIT3 = "med_medit3";

    private com.example.serenum.managers.FavoriteManager favoriteManager;
    private ImageButton btnFavoriteRespiracion;
    private ImageButton btnFavoriteMedit2;
    private ImageButton btnFavoriteMedit3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditations);
        View btnBack = findViewById(R.id.btnBackMeditations);
        MaterialCardView cardRespiracion = findViewById(R.id.cardMeditationRespiracion);
        MaterialButton btnOpenRespiracion = findViewById(R.id.btnOpenMeditationRespiracion);
        MaterialCardView cardMedit2 = findViewById(R.id.cardMeditationMedit2);
        MaterialButton btnOpenMedit2 = findViewById(R.id.btnOpenMeditationMedit2);
        MaterialCardView cardMedit3 = findViewById(R.id.cardMeditationMedit3);
        MaterialButton btnOpenMedit3 = findViewById(R.id.btnOpenMeditationMedit3);

        favoriteManager = com.example.serenum.managers.FavoriteManager.getInstance(this);
        btnFavoriteRespiracion = findViewById(R.id.btnFavoriteMeditationRespiracion);
        btnFavoriteMedit2 = findViewById(R.id.btnFavoriteMeditationMedit2);
        btnFavoriteMedit3 = findViewById(R.id.btnFavoriteMeditationMedit3);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View.OnClickListener openRespiracion = v -> openMeditation(
                getString(R.string.meditation_title),
                getString(R.string.meditation_duration),
                getString(R.string.meditation_description),
                "medit1",
                R.drawable.medit1
        );

        if (cardRespiracion != null) {
            cardRespiracion.setOnClickListener(openRespiracion);
        }

        if (btnOpenRespiracion != null) {
            btnOpenRespiracion.setOnClickListener(openRespiracion);
        }

        if (btnFavoriteRespiracion != null) {
            btnFavoriteRespiracion.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_MEDIT1);
                refreshFavoriteIcons();
                boolean nowFav = favoriteManager.isFavorite(ID_MEDIT1);
                Toast.makeText(this, nowFav ? "Añadido a favoritos" : getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
            });
        }

        // Segunda tarjeta: medit2
        View.OnClickListener openMedit2 = v -> openMeditation(
                getString(R.string.meditation2_title),
                getString(R.string.meditation2_duration),
                getString(R.string.meditation2_description),
                "medit2",
                R.drawable.medit2
        );

        if (cardMedit2 != null) cardMedit2.setOnClickListener(openMedit2);
        if (btnOpenMedit2 != null) btnOpenMedit2.setOnClickListener(openMedit2);

        if (btnFavoriteMedit2 != null) {
            btnFavoriteMedit2.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_MEDIT2);
                refreshFavoriteIcons();
                boolean nowFav = favoriteManager.isFavorite(ID_MEDIT2);
                Toast.makeText(this, nowFav ? "Añadido a favoritos" : getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
            });
        }

        // Tercera tarjeta: medit3 (dormir)
        View.OnClickListener openMedit3 = v -> openMeditation(
                getString(R.string.meditation3_title),
                getString(R.string.meditation3_duration),
                getString(R.string.meditation3_description),
                "medit3",
                R.drawable.medit3
        );

        if (cardMedit3 != null) cardMedit3.setOnClickListener(openMedit3);
        if (btnOpenMedit3 != null) btnOpenMedit3.setOnClickListener(openMedit3);

        if (btnFavoriteMedit3 != null) {
            btnFavoriteMedit3.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_MEDIT3);
                refreshFavoriteIcons();
                boolean nowFav = favoriteManager.isFavorite(ID_MEDIT3);
                Toast.makeText(this, nowFav ? "Añadido a favoritos" : getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
            });
        }

        refreshFavoriteIcons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFavoriteIcons();
    }

    private void refreshFavoriteIcons() {
        updateFavoriteIcon(btnFavoriteRespiracion, ID_MEDIT1);
        updateFavoriteIcon(btnFavoriteMedit2, ID_MEDIT2);
        updateFavoriteIcon(btnFavoriteMedit3, ID_MEDIT3);
    }

    private void updateFavoriteIcon(ImageButton button, String contentId) {
        if (button == null) return;
        boolean isFavorite = favoriteManager.isFavorite(contentId);
        button.setImageResource(isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
    }

    private void openMeditation(String title, String duration, String description, String audioName, int imageRes) {
        if (title == null || duration == null || description == null || audioName == null) {
            Toast.makeText(this, "No se pudo abrir la meditación", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MeditationActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DURATION, duration);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_AUDIO_NAME, audioName);
        intent.putExtra(EXTRA_IMAGE_RES, imageRes);
        startActivity(intent);
    }
}

