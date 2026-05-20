package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

/**
 * OnboardingActivity muestra una secuencia de 3 pantallas de onboarding (introducción emocional,
 * beneficios, y filosofía de Serenum) antes de pasar a la encuesta inicial.
 */
public class OnboardingActivity extends AppCompatActivity {

    private int currentPage = 1; // 1, 2, o 3
    private FrameLayout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        mainContainer = new FrameLayout(this);
        mainContainer.setId(R.id.main_onboarding_container);
        setContentView(mainContainer);

        ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showPage(1);
    }

    private void showPage(int page) {
        currentPage = page;
        mainContainer.removeAllViews();

        int layoutId;
        MaterialButton button;
        MaterialButton btnBack;

        switch (page) {
            case 1:
                layoutId = R.layout.activity_onboarding_page_1;
                break;
            case 2:
                layoutId = R.layout.activity_onboarding_page_2;
                break;
            case 3:
                layoutId = R.layout.activity_onboarding_page_3;
                break;
            default:
                layoutId = R.layout.activity_onboarding_page_1;
        }

        LayoutInflater.from(this).inflate(layoutId, mainContainer, true);

        // Botón Atrás - ahora disponible en todas las pantallas
        btnBack = mainContainer.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setVisibility(android.view.View.VISIBLE);
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        if (page == 1) {
            button = mainContainer.findViewById(R.id.btnNext);
            button.setOnClickListener(v -> showPage(2));
        } else if (page == 2) {
            button = mainContainer.findViewById(R.id.btnContinue);
            button.setOnClickListener(v -> showPage(3));
        } else if (page == 3) {
            button = mainContainer.findViewById(R.id.btnStart);
            button.setOnClickListener(v -> proceedToSurvey());
        }
    }

    private void proceedToSurvey() {
        Intent intent = new Intent(OnboardingActivity.this, InitialSurveyActivity.class);
        // Mantener la referencia del nombre del usuario
        intent.putExtra("NOMBRE_USUARIO", getIntent().getStringExtra("NOMBRE_USUARIO"));
        // Pasar también el email para marcar el onboarding como completado
        intent.putExtra("EMAIL_USUARIO", getIntent().getStringExtra("EMAIL_USUARIO"));
        startActivity(intent);
        // No cerramos OnboardingActivity para permitir volver atrás
    }

    @Override
    public void onBackPressed() {
        if (currentPage > 1) {
            showPage(currentPage - 1);
        } else {
            super.onBackPressed();
        }
    }
}

