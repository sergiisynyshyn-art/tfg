package com.example.serenum;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.serenum.managers.SessionManager;

import java.util.Locale;

public class SoundPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private final Handler handler = new Handler();
    private ImageButton btnPlayPause;
    private TextView tvElapsed, tvTotal;
    private SessionManager sessionManager;
    private String soundTitle;
    private long sessionStartTime = 0;
    private boolean sessionRecorded = false;

    private final Runnable updateSeekRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sound_player);

        ImageView img = findViewById(R.id.imgSoundFull);
        TextView tvTitle = findViewById(R.id.tvSoundFullTitle);
        TextView tvDescription = findViewById(R.id.tvSoundFullDesc);
        btnPlayPause = findViewById(R.id.btnSoundPlayPause);
        ImageButton btnBack = findViewById(R.id.btnBackSoundPlayer);
        seekBar = findViewById(R.id.seekBarSound);
        tvElapsed = findViewById(R.id.tvSoundElapsed);
        tvTotal = findViewById(R.id.tvSoundTotal);

        btnBack.setOnClickListener(v -> finish());

        // Leer datos del Intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String audioName = getIntent().getStringExtra("audio_name");
        int imageRes = getIntent().getIntExtra("image_res", R.drawable.meditation_image_placeholder);

        if (title != null) tvTitle.setText(title);
        if (description != null) tvDescription.setText(description);
        img.setImageResource(imageRes);

        soundTitle = title;
        sessionManager = new SessionManager(this);

        // Resolver audio
        int resId = 0;
        if (audioName != null && !audioName.isEmpty()) {
            resId = getResources().getIdentifier(audioName, "raw", getPackageName());
        }
        if (resId == 0) resId = R.raw.lluvia;

        try {
            mediaPlayer = MediaPlayer.create(this, resId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true); // ¡Sonido infinito papi!
                seekBar.setMax(mediaPlayer.getDuration());
                tvTotal.setText(formatMillis(mediaPlayer.getDuration()));
                
                // Empezar a reproducir automáticamente
                togglePlayPause();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar audio", Toast.LENGTH_SHORT).show();
        }

        btnPlayPause.setOnClickListener(v -> togglePlayPause());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    tvElapsed.setText(formatMillis(progress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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
            if (sessionStartTime == 0) sessionStartTime = System.currentTimeMillis();
            handler.post(updateSeekRunnable);
        }
    }

    private String formatMillis(int ms) {
        int seconds = (ms / 1000) % 60;
        int minutes = (ms / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Registrar sesión si estuvo reproduciendo más de 30 segundos
        if (sessionManager != null && soundTitle != null && !sessionRecorded && sessionStartTime > 0) {
            long elapsed = System.currentTimeMillis() - sessionStartTime;
            if (elapsed > 30000) { // más de 30 segundos
                sessionManager.recordSession("Sonido", soundTitle, 1); // 1 minuto
                sessionRecorded = true;
                Toast.makeText(this, "Sesión de sonido registrada", Toast.LENGTH_SHORT).show();
            }
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekRunnable);
    }
}
