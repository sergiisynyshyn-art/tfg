package com.example.serenum;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import java.util.Locale;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.serenum.managers.SessionManager;

/**
 * Pantalla de reproducción de meditación.
 * Recibe por Intent: title, duration, description, audio_name (sin extensión), image_res (int opcional)
 */
public class MeditationActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private final Handler handler = new Handler();
    private ImageButton btnPlayPause;
    private TextView tvElapsed, tvTotal;
    private SessionManager sessionManager;
    private String meditationTitle;
    private int durationMinutes;

    private final Runnable updateSeekRunnable = new Runnable() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        ImageView img = findViewById(R.id.imgMeditationFull);
        TextView tvTitle = findViewById(R.id.tvMeditationFullTitle);
        TextView tvDuration = findViewById(R.id.tvMeditationFullDuration);
        TextView tvDescription = findViewById(R.id.tvMeditationFullDescription);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        ImageButton btnBack = findViewById(R.id.btnBackMeditation);
        seekBar = findViewById(R.id.seekBarMeditation);
        tvElapsed = findViewById(R.id.tvElapsed);
        tvTotal = findViewById(R.id.tvTotal);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        // Leer extras
        String title = getIntent().getStringExtra("title");
        String duration = getIntent().getStringExtra("duration");
        String description = getIntent().getStringExtra("description");
        String audioName = getIntent().getStringExtra("audio_name");
        int imageRes = getIntent().getIntExtra("image_res", R.drawable.meditation_image_placeholder);

        if (title != null) tvTitle.setText(title);
        if (duration != null) tvDuration.setText(duration);
        if (description != null) tvDescription.setText(description);
        img.setImageResource(imageRes);

        // Guardar title y duration para registrar sesión
        meditationTitle = title;
        try {
            // Extraer minutos del string "3 min" → 3
            if (duration != null) {
                String[] parts = duration.trim().split("\\s+");
                if (parts.length > 0) {
                    durationMinutes = Integer.parseInt(parts[0]);
                }
            }
        } catch (Exception e) {
            durationMinutes = 3; // default
        }

        // Inicializar SessionManager
        sessionManager = new SessionManager(this);

        // Resolver resource id del audio
        int resId = 0;
        if (audioName != null && !audioName.isEmpty()) {
            try {
                resId = getResources().getIdentifier(audioName, "raw", getPackageName());
            } catch (Resources.NotFoundException ignored) {
            }
        }
        if (resId == 0) {
            // fallback a medit1 si no existe
            resId = R.raw.medit1;
        }

        try {
            mediaPlayer = MediaPlayer.create(this, resId);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo cargar el audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            mediaPlayer = null;
        }

        if (mediaPlayer != null) {
            seekBar.setMax(mediaPlayer.getDuration());
            tvTotal.setText(formatMillis(mediaPlayer.getDuration()));

            mediaPlayer.setOnCompletionListener(mp -> {
                btnPlayPause.setImageResource(R.drawable.ic_play_white);
                handler.removeCallbacks(updateSeekRunnable);
                seekBar.setProgress(0);
                tvElapsed.setText(formatMillis(0));
                // Registrar sesión completada
                if (sessionManager != null && meditationTitle != null) {
                    sessionManager.recordSession("Meditacion", meditationTitle, durationMinutes);
                    Toast.makeText(MeditationActivity.this, "¡Meditación completada! Datos guardados.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            seekBar.setEnabled(false);
            btnPlayPause.setEnabled(false);
        }

        btnPlayPause.setOnClickListener(v -> togglePlayPause());

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
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            } catch (Exception ignored) {
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekRunnable);
    }
}


