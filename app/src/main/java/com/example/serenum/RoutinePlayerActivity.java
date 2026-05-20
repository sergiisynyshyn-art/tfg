package com.example.serenum;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.example.serenum.managers.SessionManager;

import java.util.List;
import java.util.Locale;

/**
 * RoutinePlayerActivity: pantalla que reproduce paso a paso la rutina seleccionada.
 * Maneja la reproducción de meditaciones, sonidos y mensajes de texto.
 */
public class RoutinePlayerActivity extends AppCompatActivity {

    private Routine currentRoutine;
    private List<RoutineStep> steps;
    private int currentStepIndex = 0;

    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();
    private Runnable updateSeekRunnable;
    private SessionManager sessionManager;
    private boolean routineSessionRecorded = false;

    // UI Components
    private ImageView imgStep;
    private TextView tvStepTitle;
    private TextView tvStepContent;
    private MaterialButton btnContinue;
    private ImageButton btnPlayPause;
    private SeekBar seekBar;
    private TextView tvElapsed;
    private TextView tvTotal;
    private ProgressBar progressBarRoutine;
    private TextView tvStepNumber;
    private FrameLayout playerContainer;
    private FrameLayout textContainer;

    private static final String EXTRA_ROUTINE = "routine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_player);

        // Obtener la rutina desde el Intent
        currentRoutine = (Routine) getIntent().getSerializableExtra(EXTRA_ROUTINE);

        if (currentRoutine == null) {
            Toast.makeText(this, "Error: Rutina no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        steps = currentRoutine.getSteps();
        sessionManager = new SessionManager(this);

        // Obtener referencias de UI
        initializeUI();

        // Cargar el primer paso
        loadStep(0);
    }

    /**
     * Inicializa todas las referencias de UI.
     */
    private void initializeUI() {
        imgStep = findViewById(R.id.imgRoutineStep);
        tvStepTitle = findViewById(R.id.tvRoutineStepTitle);
        tvStepContent = findViewById(R.id.tvRoutineStepContent);
        btnContinue = findViewById(R.id.btnRoutineContinue);
        btnPlayPause = findViewById(R.id.btnPlayPauseRoutine);
        seekBar = findViewById(R.id.seekBarRoutine);
        tvElapsed = findViewById(R.id.tvRoutineElapsed);
        tvTotal = findViewById(R.id.tvRoutineTotal);
        progressBarRoutine = findViewById(R.id.progressBarRoutine);
        tvStepNumber = findViewById(R.id.tvStepNumber);
        playerContainer = findViewById(R.id.playerContainer);
        textContainer = findViewById(R.id.textContainer);

        ImageButton btnBack = findViewById(R.id.btnBackRoutinePlayer);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> nextStep());
        }

        if (btnPlayPause != null) {
            btnPlayPause.setOnClickListener(v -> togglePlayPause());
        }

        // Configurar listener para el seekbar
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                boolean wasPlaying = false;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                        tvElapsed.setText(formatMillis(progress));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (mediaPlayer != null) {
                        wasPlaying = mediaPlayer.isPlaying();
                        if (wasPlaying) mediaPlayer.pause();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mediaPlayer != null && wasPlaying) {
                        mediaPlayer.start();
                    }
                }
            });
        }

        // Configurar runnable para actualizar el seekbar
        updateSeekRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && seekBar != null) {
                    int pos = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(pos);
                    tvElapsed.setText(formatMillis(pos));
                    handler.postDelayed(this, 500);
                }
            }
        };
    }

    /**
     * Carga y muestra un paso específico de la rutina.
     */
    private void loadStep(int stepIndex) {
        currentStepIndex = stepIndex;

        if (stepIndex < 0 || stepIndex >= steps.size()) {
            showRoutineCompleted();
            return;
        }

        RoutineStep step = steps.get(stepIndex);

        // Actualizar número de paso
        if (tvStepNumber != null) {
            tvStepNumber.setText("Paso " + (currentStepIndex + 1) + " de " + steps.size());
        }

        // Actualizar barra de progreso
        if (progressBarRoutine != null) {
            progressBarRoutine.setMax(steps.size());
            progressBarRoutine.setProgress(currentStepIndex + 1);
        }

        // Establecer imagen
        if (imgStep != null) {
            imgStep.setImageResource(step.getImageRes());
        }

        // Establecer título
        if (tvStepTitle != null) {
            tvStepTitle.setText(step.getTitle());
        }

        // Ocultar containers por defecto
        if (playerContainer != null) playerContainer.setVisibility(View.GONE);
        if (textContainer != null) textContainer.setVisibility(View.GONE);
        if (btnContinue != null) btnContinue.setVisibility(View.GONE);
        if (btnPlayPause != null) btnPlayPause.setVisibility(View.GONE);

        // Reproducir según el tipo de paso
        switch (step.getType()) {
            case RoutineStep.TYPE_MEDITATION:
                playMeditationStep(step);
                break;
            case RoutineStep.TYPE_SOUND:
                playSoundStep(step);
                break;
            case RoutineStep.TYPE_TEXT:
                playTextStep(step);
                break;
        }
    }

    /**
     * Reproduce un paso de tipo meditación.
     */
    private void playMeditationStep(RoutineStep step) {
        if (playerContainer != null) playerContainer.setVisibility(View.VISIBLE);

        // Obtener el resource ID del audio
        int resId = resolveAudioResource(step.getId());

        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(this, resId);

            if (mediaPlayer != null) {
                mediaPlayer.setLooping(false); // La meditación no es en loop

                seekBar.setMax(mediaPlayer.getDuration());
                tvTotal.setText(formatMillis(mediaPlayer.getDuration()));
                tvElapsed.setText(formatMillis(0));

                // Listener para cuando termine la meditación
                mediaPlayer.setOnCompletionListener(mp -> {
                    btnPlayPause.setImageResource(R.drawable.ic_play_white);
                    handler.removeCallbacks(updateSeekRunnable);
                    if (btnContinue != null) btnContinue.setVisibility(View.VISIBLE);
                });

                // Iniciar reproducción automáticamente
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause_white);
                handler.post(updateSeekRunnable);
            } else {
                Toast.makeText(this, "No se pudo cargar el audio", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al reproducir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Reproduce un paso de tipo sonido (infinito).
     */
    private void playSoundStep(RoutineStep step) {
        if (playerContainer != null) playerContainer.setVisibility(View.VISIBLE);

        int resId = resolveAudioResource(step.getId());

        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(this, resId);

            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true); // Sonido en loop

                seekBar.setMax(mediaPlayer.getDuration());
                tvTotal.setText(formatMillis(mediaPlayer.getDuration()));
                tvElapsed.setText(formatMillis(0));

                // Iniciar reproducción automáticamente
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause_white);
                handler.post(updateSeekRunnable);

                // Mostrar botón de continuar
                if (btnContinue != null) btnContinue.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "No se pudo cargar el sonido", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al reproducir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra un paso de tipo texto.
     */
    private void playTextStep(RoutineStep step) {
        if (textContainer != null) textContainer.setVisibility(View.VISIBLE);

        if (tvStepContent != null) {
            tvStepContent.setText(step.getContent());
        }

        // Mostrar botón de continuar
        if (btnContinue != null) {
            btnContinue.setVisibility(View.VISIBLE);
            btnContinue.setText("Continuar");
        }

        // Detener reproducción anterior si existe
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            handler.removeCallbacks(updateSeekRunnable);
        }
    }

    /**
     * Resuelve el ID del recurso de audio por su nombre.
     */
    private int resolveAudioResource(String audioName) {
        try {
            return getResources().getIdentifier(audioName, "raw", getPackageName());
        } catch (Resources.NotFoundException ignored) {
            return R.raw.medit1; // fallback
        }
    }

    /**
     * Alterna entre play y pausa del mediaPlayer.
     */
    private void togglePlayPause() {
        if (mediaPlayer == null) return;

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setImageResource(R.drawable.ic_play_white);
            handler.removeCallbacks(updateSeekRunnable);
        } else {
            mediaPlayer.start();
            btnPlayPause.setImageResource(R.drawable.ic_pause_white);
            handler.post(updateSeekRunnable);
        }
    }

    /**
     * Carga el siguiente paso de la rutina.
     */
    private void nextStep() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            handler.removeCallbacks(updateSeekRunnable);
        }

        currentStepIndex++;
        loadStep(currentStepIndex);
    }

    /**
     * Muestra la pantalla final cuando la rutina se completa.
     */
    private void showRoutineCompleted() {
        FrameLayout completionContainer = findViewById(R.id.completionContainer);

        if (completionContainer != null) {
            completionContainer.setVisibility(View.VISIBLE);
        }

        if (playerContainer != null) playerContainer.setVisibility(View.GONE);
        if (textContainer != null) textContainer.setVisibility(View.GONE);
        if (btnContinue != null) btnContinue.setVisibility(View.GONE);

        MaterialButton btnFinish = findViewById(R.id.btnFinishRoutine);
        if (btnFinish != null) {
            btnFinish.setVisibility(View.VISIBLE);
            btnFinish.setOnClickListener(v -> finish());
        }

        // Registrar sesión completada
        if (sessionManager != null && currentRoutine != null && !routineSessionRecorded) {
            sessionManager.recordSession("Rutina", currentRoutine.getTitle(), getCurrentRoutineDurationMinutes());
            routineSessionRecorded = true;
            Toast.makeText(this, "¡Rutina completada! Datos guardados.", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCurrentRoutineDurationMinutes() {
        String duration = currentRoutine.getDuration();
        try {
            if (duration != null) {
                String[] parts = duration.trim().split("\\s+");
                if (parts.length > 0) {
                    int minutes = Integer.parseInt(parts[0]);
                    return Math.max(minutes, 1);
                }
            }
        } catch (Exception ignored) {
        }
        return 5; // default
    }

    /**
     * Formatea milisegundos en formato MM:SS.
     */
    private String formatMillis(int ms) {
        int totalSeconds = ms / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (Exception ignored) {
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            handler.removeCallbacks(updateSeekRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Opcionalmente reanudar la reproducción
    }
}
