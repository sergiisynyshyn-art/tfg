package com.example.serenum;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BreathingActivity extends AppCompatActivity {

    private TextView txtInstruction, txtTimer;
    private View circle, circleOuter;
    private CountDownTimer mainTimer;
    private ObjectAnimator breathingAnimator;
    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_breathing);

        txtInstruction = findViewById(R.id.txtBreathingInstruction);
        txtTimer = findViewById(R.id.txtBreathingTimer);
        circle = findViewById(R.id.breathingCircle);
        circleOuter = findViewById(R.id.breathingCircleOuter);

        findViewById(R.id.btnBackBreathing).setOnClickListener(v -> finish());

        startSession();
    }

    private void startSession() {
        // Timer de 1 minuto
        mainTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                txtTimer.setText(String.format(Locale.getDefault(), "0:%02d", seconds));
            }

            @Override
            public void onFinish() {
                isRunning = false;
                txtTimer.setText("0:00");
                txtInstruction.setText("Sesión completada ✨");
                if (breathingAnimator != null) breathingAnimator.cancel();
                Toast.makeText(BreathingActivity.this, "¡Bien hecho!", Toast.LENGTH_LONG).show();
            }
        }.start();

        runBreathingCycle();
    }

    private void runBreathingCycle() {
        if (!isRunning) return;

        // Inhala (4 segundos)
        txtInstruction.setText("Inhala...");
        animateCircle(1.0f, 1.8f, 4000, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Mantén (2 segundos)
                txtInstruction.setText("Mantén...");
                circle.postDelayed(() -> {
                    if (!isRunning) return;
                    // Exhala (4 segundos)
                    txtInstruction.setText("Exhala...");
                    animateCircle(1.8f, 1.0f, 4000, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Repite el ciclo
                            runBreathingCycle();
                        }
                    });
                }, 2000);
            }
        });
    }

    private void animateCircle(float start, float end, long duration, AnimatorListenerAdapter listener) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, start, end);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, start, end);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, start == 1.0f ? 0.6f : 1.0f, end == 1.8f ? 1.0f : 0.6f);

        breathingAnimator = ObjectAnimator.ofPropertyValuesHolder(circle, scaleX, scaleY, alpha);
        breathingAnimator.setDuration(duration);
        
        // Efecto glow también se anima un poco desfasado o con mayor escala
        circleOuter.animate().scaleX(end * 1.2f).scaleY(end * 1.2f).setDuration(duration).start();

        if (listener != null) {
            breathingAnimator.addListener(listener);
        }
        breathingAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (mainTimer != null) mainTimer.cancel();
        if (breathingAnimator != null) breathingAnimator.cancel();
    }
}
