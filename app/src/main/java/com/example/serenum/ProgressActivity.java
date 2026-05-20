package com.example.serenum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/** Muestra el progreso del usuario usando los mismos prefs simples. */
public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        ImageButton btnBack = findViewById(R.id.btnBackProgress);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        SharedPreferences prefs = getSharedPreferences("profile_progress", MODE_PRIVATE);
        int sessions = prefs.getInt("sessions_completed", 0);
        int totalMinutes = prefs.getInt("total_minutes", 0);
        int streak = prefs.getInt("streak_days", 0);

        View s1 = findViewById(R.id.p_stat1);
        View s2 = findViewById(R.id.p_stat2);
        View s3 = findViewById(R.id.p_stat3);

        if (s1 != null) {
            ((TextView) s1.findViewById(R.id.tvStatValue)).setText(String.valueOf(sessions));
            ((TextView) s1.findViewById(R.id.tvStatLabel)).setText(getString(R.string.profile_sessions_completed));
        }
        if (s2 != null) {
            ((TextView) s2.findViewById(R.id.tvStatValue)).setText(totalMinutes + " min");
            ((TextView) s2.findViewById(R.id.tvStatLabel)).setText(getString(R.string.profile_total_time));
        }
        if (s3 != null) {
            ((TextView) s3.findViewById(R.id.tvStatValue)).setText(String.valueOf(streak));
            ((TextView) s3.findViewById(R.id.tvStatLabel)).setText(getString(R.string.profile_streak));
        }
    }
}

