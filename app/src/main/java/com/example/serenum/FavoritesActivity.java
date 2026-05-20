package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.serenum.managers.FavoriteManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * FavoritesActivity muestra los contenidos guardados como favoritos.
 *
 * Se reconstruye la lista desde IDs persistidos en SharedPreferences para que
 * los favoritos sobrevivan entre cierres de app/sesion sin depender de red.
 */
public class FavoritesActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_DURATION = "duration";
    private static final String EXTRA_DESCRIPTION = "description";
    private static final String EXTRA_AUDIO_NAME = "audio_name";
    private static final String EXTRA_IMAGE_RES = "image_res";
    private static final String EXTRA_ROUTINE = "routine";

    private FavoriteManager favoriteManager;
    private LinearLayout favoritesContainer;
    private TextView tvEmptyFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoriteManager = FavoriteManager.getInstance(this);
        favoritesContainer = findViewById(R.id.favoritesContainer);
        tvEmptyFavorites = findViewById(R.id.tvEmptyFavorites);

        ImageButton btnBack = findViewById(R.id.btnBackFavorites);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        renderFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderFavorites();
    }

    private void renderFavorites() {
        if (favoritesContainer == null || tvEmptyFavorites == null) return;

        favoritesContainer.removeAllViews();
        List<String> favoriteIds = new ArrayList<>(favoriteManager.getAllFavorites());
        Collections.sort(favoriteIds);

        if (favoriteIds.isEmpty()) {
            tvEmptyFavorites.setVisibility(View.VISIBLE);
            return;
        }

        tvEmptyFavorites.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (String favoriteId : favoriteIds) {
            FavoriteUiData data = buildUiData(favoriteId);
            if (data == null) continue;

            View itemView = inflater.inflate(R.layout.item_favorite_content, favoritesContainer, false);
            MaterialCardView card = itemView.findViewById(R.id.cardFavoriteItem);
            ImageView image = itemView.findViewById(R.id.imgFavoriteItem);
            TextView subtitle = itemView.findViewById(R.id.tvFavoriteType);
            TextView title = itemView.findViewById(R.id.tvFavoriteTitle);
            TextView description = itemView.findViewById(R.id.tvFavoriteDescription);
            ImageButton btnFavorite = itemView.findViewById(R.id.btnFavoriteToggle);
            MaterialButton btnOpen = itemView.findViewById(R.id.btnOpenFavorite);

            image.setImageResource(data.imageRes);
            subtitle.setText(data.typeLabel);
            title.setText(data.title);
            description.setText(data.description);

            View.OnClickListener openListener = v -> openFavoriteById(favoriteId);
            card.setOnClickListener(openListener);
            btnOpen.setOnClickListener(openListener);

            // En pantalla de favoritos, la estrella siempre parte en "llena" y permite quitar.
            btnFavorite.setImageResource(R.drawable.ic_star_filled);
            btnFavorite.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(favoriteId);
                renderFavorites();
                Toast.makeText(this, R.string.favorite_removed, Toast.LENGTH_SHORT).show();
            });

            favoritesContainer.addView(itemView);
        }

        if (favoritesContainer.getChildCount() == 0) {
            tvEmptyFavorites.setVisibility(View.VISIBLE);
        }
    }

    private FavoriteUiData buildUiData(String favoriteId) {
        if (favoriteId == null) return null;

        if (favoriteId.startsWith("med_")) {
            String meditationId = favoriteId.substring("med_".length());
            switch (meditationId) {
                case "medit1":
                    return new FavoriteUiData(
                            getString(R.string.favorite_type_meditation),
                            getString(R.string.meditation_title),
                            getString(R.string.meditation_description),
                            R.drawable.medit1
                    );
                case "medit2":
                    return new FavoriteUiData(
                            getString(R.string.favorite_type_meditation),
                            getString(R.string.meditation2_title),
                            getString(R.string.meditation2_description),
                            R.drawable.medit2
                    );
                case "medit3":
                    return new FavoriteUiData(
                            getString(R.string.favorite_type_meditation),
                            getString(R.string.meditation3_title),
                            getString(R.string.meditation3_description),
                            R.drawable.medit3
                    );
                default:
                    return null;
            }
        }

        if (favoriteId.startsWith("sound_")) {
            String soundId = favoriteId.substring("sound_".length());
            switch (soundId) {
                case "lluvia":
                    return new FavoriteUiData(getString(R.string.favorite_type_sound), getString(R.string.sound_rain_title), getString(R.string.sound_rain_description), R.drawable.lluvia);
                case "fuego":
                    return new FavoriteUiData(getString(R.string.favorite_type_sound), getString(R.string.sound_fire_title), getString(R.string.sound_fire_description), R.drawable.fuego);
                case "bosque":
                    return new FavoriteUiData(getString(R.string.favorite_type_sound), getString(R.string.sound_forest_title), getString(R.string.sound_forest_description), R.drawable.bosque);
                case "viento":
                    return new FavoriteUiData(getString(R.string.favorite_type_sound), getString(R.string.sound_wind_title), getString(R.string.sound_wind_description), R.drawable.viento);
                case "olas":
                    return new FavoriteUiData(getString(R.string.favorite_type_sound), getString(R.string.sound_waves_title), getString(R.string.sound_waves_description), R.drawable.olas);
                default:
                    return null;
            }
        }

        if (favoriteId.startsWith("routine_")) {
            String routineId = favoriteId.substring("routine_".length());
            Routine routine = resolveRoutine(routineId);
            if (routine == null) return null;
            return new FavoriteUiData(
                    getString(R.string.favorite_type_routine),
                    routine.getTitle(),
                    routine.getDescription(),
                    routine.getImageRes()
            );
        }

        return null;
    }

    private void openFavoriteById(String favoriteId) {
        if (favoriteId == null) return;

        if (favoriteId.startsWith("med_")) {
            openMeditationById(favoriteId.substring("med_".length()));
            return;
        }

        if (favoriteId.startsWith("sound_")) {
            openSoundById(favoriteId.substring("sound_".length()));
            return;
        }

        if (favoriteId.startsWith("routine_")) {
            Routine routine = resolveRoutine(favoriteId.substring("routine_".length()));
            if (routine != null) {
                Intent intent = new Intent(this, RoutinePlayerActivity.class);
                intent.putExtra(EXTRA_ROUTINE, routine);
                startActivity(intent);
            }
        }
    }

    private void openMeditationById(String meditationId) {
        Intent intent = new Intent(this, MeditationActivity.class);

        switch (meditationId) {
            case "medit1":
                intent.putExtra(EXTRA_TITLE, getString(R.string.meditation_title));
                intent.putExtra(EXTRA_DURATION, getString(R.string.meditation_duration));
                intent.putExtra(EXTRA_DESCRIPTION, getString(R.string.meditation_description));
                intent.putExtra(EXTRA_AUDIO_NAME, "medit1");
                intent.putExtra(EXTRA_IMAGE_RES, R.drawable.medit1);
                break;
            case "medit2":
                intent.putExtra(EXTRA_TITLE, getString(R.string.meditation2_title));
                intent.putExtra(EXTRA_DURATION, getString(R.string.meditation2_duration));
                intent.putExtra(EXTRA_DESCRIPTION, getString(R.string.meditation2_description));
                intent.putExtra(EXTRA_AUDIO_NAME, "medit2");
                intent.putExtra(EXTRA_IMAGE_RES, R.drawable.medit2);
                break;
            case "medit3":
                intent.putExtra(EXTRA_TITLE, getString(R.string.meditation3_title));
                intent.putExtra(EXTRA_DURATION, getString(R.string.meditation3_duration));
                intent.putExtra(EXTRA_DESCRIPTION, getString(R.string.meditation3_description));
                intent.putExtra(EXTRA_AUDIO_NAME, "medit3");
                intent.putExtra(EXTRA_IMAGE_RES, R.drawable.medit3);
                break;
            default:
                Toast.makeText(this, R.string.favorite_content_not_available, Toast.LENGTH_SHORT).show();
                return;
        }

        startActivity(intent);
    }

    private void openSoundById(String soundId) {
        Intent intent = new Intent(this, SoundPlayerActivity.class);

        switch (soundId) {
            case "lluvia":
                intent.putExtra(EXTRA_TITLE, getString(R.string.sound_rain_title));
                intent.putExtra(EXTRA_DURATION, getString(R.string.sound_rain_duration));
                intent.putExtra(EXTRA_DESCRIPTION, getString(R.string.sound_rain_description));
                intent.putExtra(EXTRA_AUDIO_NAME, "lluvia");
                intent.putExtra(EXTRA_IMAGE_RES, R.drawable.lluvia);
                break;
            case "fuego":
                intent.putExtra(EXTRA_TITLE, getString(R.string.sound_fire_title));
                intent.putExtra(EXTRA_DURATION, getString(R.string.sound_fire_duration));
                intent.putExtra(EXTRA_DESCRIPTION, getString(R.string.sound_fire_description));
                intent.putExtra(EXTRA_AUDIO_NAME, "fuego");
                intent.putExtra(EXTRA_IMAGE_RES, R.drawable.fuego);
                break;
            case "bosque":
                intent.putExtra(EXTRA_TITLE, getString(R.string.sound_forest_title));
                intent.putExtra(EXTRA_DURATION, getString(R.string.sound_forest_duration));
                intent.putExtra(EXTRA_DESCRIPTION, getString(R.string.sound_forest_description));
                intent.putExtra(EXTRA_AUDIO_NAME, "bosque");
                intent.putExtra(EXTRA_IMAGE_RES, R.drawable.bosque);
                break;
            case "viento":
                intent.putExtra(EXTRA_TITLE, getString(R.string.sound_wind_title));
                intent.putExtra(EXTRA_DURATION, getString(R.string.sound_wind_duration));
                intent.putExtra(EXTRA_DESCRIPTION, getString(R.string.sound_wind_description));
                intent.putExtra(EXTRA_AUDIO_NAME, "viento");
                intent.putExtra(EXTRA_IMAGE_RES, R.drawable.viento);
                break;
            case "olas":
                intent.putExtra(EXTRA_TITLE, getString(R.string.sound_waves_title));
                intent.putExtra(EXTRA_DURATION, getString(R.string.sound_waves_duration));
                intent.putExtra(EXTRA_DESCRIPTION, getString(R.string.sound_waves_description));
                intent.putExtra(EXTRA_AUDIO_NAME, "olas");
                intent.putExtra(EXTRA_IMAGE_RES, R.drawable.olas);
                break;
            default:
                Toast.makeText(this, R.string.favorite_content_not_available, Toast.LENGTH_SHORT).show();
                return;
        }

        startActivity(intent);
    }

    private Routine resolveRoutine(String routineId) {
        if (routineId == null) return null;
        switch (routineId) {
            case "stress_relief":
                return RoutinesDataManager.getStressReliefRoutine();
            case "start_day":
                return RoutinesDataManager.getStartDayRoutine();
            case "sleep_better":
                return RoutinesDataManager.getSleepBetterRoutine();
            default:
                return null;
        }
    }

    private static class FavoriteUiData {
        final String typeLabel;
        final String title;
        final String description;
        final int imageRes;

        FavoriteUiData(String typeLabel, String title, String description, int imageRes) {
            this.typeLabel = typeLabel;
            this.title = title;
            this.description = description;
            this.imageRes = imageRes;
        }
    }
}

