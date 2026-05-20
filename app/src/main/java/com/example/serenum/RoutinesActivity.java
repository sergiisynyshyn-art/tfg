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

/**
 * RoutinesActivity: pantalla principal con la lista de rutinas disponibles.
 * Muestra todas las rutinas como tarjetas seleccionables.
 */
public class RoutinesActivity extends AppCompatActivity {

    private static final String EXTRA_ROUTINE = "routine";

    private static final String ID_ROUTINE_STRESS_RELIEF = "routine_stress_relief";
    private static final String ID_ROUTINE_START_DAY = "routine_start_day";
    private static final String ID_ROUTINE_SLEEP_BETTER = "routine_sleep_better";

    private FavoriteManager favoriteManager;
    private ImageButton btnFavoriteStressRelief;
    private ImageButton btnFavoriteStartDay;
    private ImageButton btnFavoriteSleepBetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);
        favoriteManager = FavoriteManager.getInstance(this);

        // Obtener referencias de UI
        View btnBack = findViewById(R.id.btnBackRoutines);

        // Cards para cada rutina
        MaterialCardView cardStressRelief = findViewById(R.id.cardStressRelief);
        MaterialButton btnOpenStressRelief = findViewById(R.id.btnOpenStressRelief);

        MaterialCardView cardStartDay = findViewById(R.id.cardStartDay);
        MaterialButton btnOpenStartDay = findViewById(R.id.btnOpenStartDay);

        MaterialCardView cardSleepBetter = findViewById(R.id.cardSleepBetter);
        MaterialButton btnOpenSleepBetter = findViewById(R.id.btnOpenSleepBetter);
        btnFavoriteStressRelief = findViewById(R.id.btnFavoriteStressRelief);
        btnFavoriteStartDay = findViewById(R.id.btnFavoriteStartDay);
        btnFavoriteSleepBetter = findViewById(R.id.btnFavoriteSleepBetter);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Rutina 1: Reducir estrés
        View.OnClickListener openStressRelief = v -> openRoutine(RoutinesDataManager.getStressReliefRoutine());
        if (cardStressRelief != null) cardStressRelief.setOnClickListener(openStressRelief);
        if (btnOpenStressRelief != null) btnOpenStressRelief.setOnClickListener(openStressRelief);
        if (btnFavoriteStressRelief != null) {
            btnFavoriteStressRelief.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_ROUTINE_STRESS_RELIEF);
                refreshFavoriteIcons();
            });
        }

        // Rutina 2: Empezar el día
        View.OnClickListener openStartDay = v -> openRoutine(RoutinesDataManager.getStartDayRoutine());
        if (cardStartDay != null) cardStartDay.setOnClickListener(openStartDay);
        if (btnOpenStartDay != null) btnOpenStartDay.setOnClickListener(openStartDay);
        if (btnFavoriteStartDay != null) {
            btnFavoriteStartDay.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_ROUTINE_START_DAY);
                refreshFavoriteIcons();
            });
        }

        // Rutina 3: Dormir mejor
        View.OnClickListener openSleepBetter = v -> openRoutine(RoutinesDataManager.getSleepBetterRoutine());
        if (cardSleepBetter != null) cardSleepBetter.setOnClickListener(openSleepBetter);
        if (btnOpenSleepBetter != null) btnOpenSleepBetter.setOnClickListener(openSleepBetter);
        if (btnFavoriteSleepBetter != null) {
            btnFavoriteSleepBetter.setOnClickListener(v -> {
                favoriteManager.toggleFavorite(ID_ROUTINE_SLEEP_BETTER);
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
        updateFavoriteIcon(btnFavoriteStressRelief, ID_ROUTINE_STRESS_RELIEF);
        updateFavoriteIcon(btnFavoriteStartDay, ID_ROUTINE_START_DAY);
        updateFavoriteIcon(btnFavoriteSleepBetter, ID_ROUTINE_SLEEP_BETTER);
    }

    private void updateFavoriteIcon(ImageButton button, String contentId) {
        if (button == null) return;
        boolean isFavorite = favoriteManager.isFavorite(contentId);
        button.setImageResource(isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
    }

    /**
     * Abre la pantalla de reproducción de rutina.
     */
    private void openRoutine(Routine routine) {
        if (routine == null) {
            Toast.makeText(this, "No se pudo abrir la rutina", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, RoutinePlayerActivity.class);
        intent.putExtra(EXTRA_ROUTINE, routine);
        startActivity(intent);
    }
}



