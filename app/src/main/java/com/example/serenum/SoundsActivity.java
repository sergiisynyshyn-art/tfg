package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.example.serenum.managers.FavoriteManager;

public class SoundsActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_DURATION = "duration";
    private static final String EXTRA_DESCRIPTION = "description";
    private static final String EXTRA_AUDIO_NAME = "audio_name";
    private static final String EXTRA_IMAGE_RES = "image_res";

    private static final String ID_SOUND_RAIN = "sound_lluvia";
    private static final String ID_SOUND_FIRE = "sound_fuego";
    private static final String ID_SOUND_FOREST = "sound_bosque";
    private static final String ID_SOUND_WIND = "sound_viento";
    private static final String ID_SOUND_WAVES = "sound_olas";

    private FavoriteManager favoriteManager;
    private ImageButton btnFavoriteRain;
    private ImageButton btnFavoriteFire;
    private ImageButton btnFavoriteForest;
    private ImageButton btnFavoriteWind;
    private ImageButton btnFavoriteWaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds);
        favoriteManager = FavoriteManager.getInstance(this);

        View btnBack = findViewById(R.id.btnBackSounds);
        MaterialCardView cardRain = findViewById(R.id.cardSoundRain);
        MaterialButton btnOpenRain = findViewById(R.id.btnOpenSoundRain);
        MaterialCardView cardFire = findViewById(R.id.cardSoundFire);
        MaterialButton btnOpenFire = findViewById(R.id.btnOpenSoundFire);
        MaterialCardView cardForest = findViewById(R.id.cardSoundForest);
        MaterialButton btnOpenForest = findViewById(R.id.btnOpenSoundForest);
        MaterialCardView cardWind = findViewById(R.id.cardSoundWind);
        MaterialButton btnOpenWind = findViewById(R.id.btnOpenSoundWind);
        MaterialCardView cardWaves = findViewById(R.id.cardSoundWaves);
        MaterialButton btnOpenWaves = findViewById(R.id.btnOpenSoundWaves);
        btnFavoriteRain = findViewById(R.id.btnFavoriteSoundRain);
        btnFavoriteFire = findViewById(R.id.btnFavoriteSoundFire);
        btnFavoriteForest = findViewById(R.id.btnFavoriteSoundForest);
        btnFavoriteWind = findViewById(R.id.btnFavoriteSoundWind);
        btnFavoriteWaves = findViewById(R.id.btnFavoriteSoundWaves);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View.OnClickListener openRain = v -> openSound(
                getString(R.string.sound_rain_title),
                getString(R.string.sound_rain_duration),
                getString(R.string.sound_rain_description),
                "lluvia",
                R.drawable.lluvia
        );

        if (cardRain != null) {
            cardRain.setOnClickListener(openRain);
        }

        if (btnOpenRain != null) {
            btnOpenRain.setOnClickListener(openRain);
        }
        if (btnFavoriteRain != null) {
            btnFavoriteRain.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_SOUND_RAIN);
                refreshFavoriteIcons();
            });
        }

        View.OnClickListener openFire = v -> openSound(
                getString(R.string.sound_fire_title),
                getString(R.string.sound_fire_duration),
                getString(R.string.sound_fire_description),
                "fuego",
                R.drawable.fuego
        );

        if (cardFire != null) {
            cardFire.setOnClickListener(openFire);
        }

        if (btnOpenFire != null) {
            btnOpenFire.setOnClickListener(openFire);
        }
        if (btnFavoriteFire != null) {
            btnFavoriteFire.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_SOUND_FIRE);
                refreshFavoriteIcons();
            });
        }

        View.OnClickListener openForest = v -> openSound(
                getString(R.string.sound_forest_title),
                getString(R.string.sound_forest_duration),
                getString(R.string.sound_forest_description),
                "bosque",
                R.drawable.bosque
        );

        if (cardForest != null) {
            cardForest.setOnClickListener(openForest);
        }

        if (btnOpenForest != null) {
            btnOpenForest.setOnClickListener(openForest);
        }
        if (btnFavoriteForest != null) {
            btnFavoriteForest.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_SOUND_FOREST);
                refreshFavoriteIcons();
            });
        }

        View.OnClickListener openWind = v -> openSound(
                getString(R.string.sound_wind_title),
                getString(R.string.sound_wind_duration),
                getString(R.string.sound_wind_description),
                "viento",
                R.drawable.viento
        );

        if (cardWind != null) {
            cardWind.setOnClickListener(openWind);
        }

        if (btnOpenWind != null) {
            btnOpenWind.setOnClickListener(openWind);
        }
        if (btnFavoriteWind != null) {
            btnFavoriteWind.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_SOUND_WIND);
                refreshFavoriteIcons();
            });
        }

        View.OnClickListener openWaves = v -> openSound(
                getString(R.string.sound_waves_title),
                getString(R.string.sound_waves_duration),
                getString(R.string.sound_waves_description),
                "olas",
                R.drawable.olas
        );

        if (cardWaves != null) {
            cardWaves.setOnClickListener(openWaves);
        }

        if (btnOpenWaves != null) {
            btnOpenWaves.setOnClickListener(openWaves);
        }
        if (btnFavoriteWaves != null) {
            btnFavoriteWaves.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_SOUND_WAVES);
                refreshFavoriteIcons();
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
        updateFavoriteIcon(btnFavoriteRain, ID_SOUND_RAIN);
        updateFavoriteIcon(btnFavoriteFire, ID_SOUND_FIRE);
        updateFavoriteIcon(btnFavoriteForest, ID_SOUND_FOREST);
        updateFavoriteIcon(btnFavoriteWind, ID_SOUND_WIND);
        updateFavoriteIcon(btnFavoriteWaves, ID_SOUND_WAVES);
    }

    private void updateFavoriteIcon(ImageButton button, String contentId) {
        if (button == null) return;
        boolean isFavorite = favoriteManager.isFavorite(contentId);
        button.setImageResource(isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
    }

    private void openSound(String title, String duration, String description, String audioName, int imageRes) {
        if (title == null || audioName == null) {
            Toast.makeText(this, "No se pudo abrir el sonido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Usamos SoundPlayerActivity para sonidos infinitos
        Intent intent = new Intent(this, SoundPlayerActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DURATION, duration);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_AUDIO_NAME, audioName);
        intent.putExtra(EXTRA_IMAGE_RES, imageRes);
        startActivity(intent);
    }
}
