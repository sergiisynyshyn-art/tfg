package com.example.serenum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class InitialSurveyActivity extends AppCompatActivity {

    private MaterialCardView cardWellbeing;
    private MaterialCardView cardStress;
    private MaterialCardView cardSleep;
    private MaterialCardView cardBalance;
    private MaterialCardView cardFocus;
    private MaterialButton btnContinue;
    private ImageButton btnBackSurvey;

    private final Set<String> selectedGoals = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_initial_survey);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindViews();
        setupOptionCards();

        btnContinue.setOnClickListener(v -> proceedToMain());
    }

    private void bindViews() {
        cardWellbeing = findViewById(R.id.cardWellbeing);
        cardStress = findViewById(R.id.cardStress);
        cardSleep = findViewById(R.id.cardSleep);
        cardBalance = findViewById(R.id.cardBalance);
        cardFocus = findViewById(R.id.cardFocus);
        btnContinue = findViewById(R.id.btnSurveyContinue);
        btnBackSurvey = findViewById(R.id.btnBackSurvey);

        if (btnBackSurvey != null) {
            btnBackSurvey.setOnClickListener(v -> onBackPressed());
        }
    }

    private void setupOptionCards() {
        cardWellbeing.setOnClickListener(v -> toggleGoal(getString(R.string.survey_goal_wellbeing)));
        cardStress.setOnClickListener(v -> toggleGoal(getString(R.string.survey_goal_stress)));
        cardSleep.setOnClickListener(v -> toggleGoal(getString(R.string.survey_goal_sleep)));
        cardBalance.setOnClickListener(v -> toggleGoal(getString(R.string.survey_goal_balance)));
        cardFocus.setOnClickListener(v -> toggleGoal(getString(R.string.survey_goal_focus)));

        refreshCardStates();
        btnContinue.setEnabled(false);
    }

    private void toggleGoal(String goal) {
        if (selectedGoals.contains(goal)) {
            selectedGoals.remove(goal);
        } else {
            selectedGoals.add(goal);
        }

        refreshCardStates();
        btnContinue.setEnabled(!selectedGoals.isEmpty());
    }

    private void refreshCardStates() {
        applyCardState(cardWellbeing, selectedGoals.contains(getString(R.string.survey_goal_wellbeing)));
        applyCardState(cardStress, selectedGoals.contains(getString(R.string.survey_goal_stress)));
        applyCardState(cardSleep, selectedGoals.contains(getString(R.string.survey_goal_sleep)));
        applyCardState(cardBalance, selectedGoals.contains(getString(R.string.survey_goal_balance)));
        applyCardState(cardFocus, selectedGoals.contains(getString(R.string.survey_goal_focus)));
    }

    private void applyCardState(MaterialCardView card, boolean selected) {
        int fillColor = ContextCompat.getColor(this, selected ? R.color.survey_card_selected : R.color.survey_card_default);
        int strokeColor = selected ? ContextCompat.getColor(this, R.color.primary_light)
                : ContextCompat.getColor(this, android.R.color.transparent);
        card.setCardBackgroundColor(fillColor);
        card.setStrokeWidth(dpToPx(selected ? 2 : 1));
        card.setStrokeColor(strokeColor);
        card.setCardElevation(dpToPx(selected ? 8 : 4));
        card.setScaleX(selected ? 1.01f : 1.0f);
        card.setScaleY(selected ? 1.01f : 1.0f);
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()));
    }

    private void proceedToMain() {
        if (selectedGoals.isEmpty()) {
            Toast.makeText(this, R.string.survey_select_one_or_more, Toast.LENGTH_SHORT).show();
            return;
        }

        // Marcar que el usuario completó el onboarding
        markOnboardingCompleted();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putStringArrayListExtra("SURVEY_GOALS", new ArrayList<>(selectedGoals));
        // Pasar finalmente el nombre a la pantalla principal
        intent.putExtra("NOMBRE_USUARIO", getIntent().getStringExtra("NOMBRE_USUARIO"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Marca en SharedPreferences que el usuario completó el onboarding
     */
    private void markOnboardingCompleted() {
        String email = getIntent().getStringExtra("EMAIL_USUARIO");
        if (email != null && !email.isEmpty()) {
            String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
            SharedPreferences prefs = getSharedPreferences("onboarding_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("onboarding_completed_" + normalizedEmail, true);
            editor.apply();
        }
    }
}


