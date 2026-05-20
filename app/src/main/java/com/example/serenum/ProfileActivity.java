package com.example.serenum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.button.MaterialButton;
import com.example.serenum.adapters.HistoryAdapter;
import com.example.serenum.models.HistoryEntry;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private RecyclerView historyContainer;
    private MaterialButton btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        configurarGoogleSignIn();

        // Aplicar Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar datos del usuario
        setupUserData();

        // Referencias UI adicionales
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view_profile);
        historyContainer = findViewById(R.id.historyContainer);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Setup RecyclerView
        if (historyContainer != null) {
            historyContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        // Abrir drawer
        ImageButton btnOpenDrawer = findViewById(R.id.btnOpenDrawer);
        if (btnOpenDrawer != null) {
            btnOpenDrawer.setOnClickListener(v -> {
                if (drawerLayout != null) drawerLayout.open();
            });
        }

        // Navegación lateral
        if (navView != null) {
            navView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                Intent intent = null;
                if (id == R.id.menu_faq) intent = new Intent(ProfileActivity.this, FaqActivity.class);
                else if (id == R.id.menu_about) intent = new Intent(ProfileActivity.this, AboutActivity.class);
                else if (id == R.id.menu_privacy) intent = new Intent(ProfileActivity.this, PrivacyActivity.class);
                else if (id == R.id.menu_support) intent = new Intent(ProfileActivity.this, SupportActivity.class);
                else if (id == R.id.menu_settings_advanced) {
                    Toast.makeText(ProfileActivity.this, "Ajustes avanzados (próximamente)", Toast.LENGTH_SHORT).show();
                }
                if (intent != null) startActivity(intent);
                drawerLayout.close();
                return true;
            });
        }

        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));
        }

        // Listeners
        findViewById(R.id.btnBackProfile).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.btnProfileLogout).setOnClickListener(v -> cerrarSesion());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data (profile, progress, history)
        setupUserData();
        loadProgressAndHistory();
    }

    private void configurarGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupUserData() {
        TextView txtName = findViewById(R.id.txtProfileName);
        TextView txtEmail = findViewById(R.id.txtProfileEmail);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                txtName.setText(user.getDisplayName());
            }
            if (user.getEmail() != null) {
                txtEmail.setText(user.getEmail());
            }
        }

        // También cargar datos guardados en SharedPreferences (edición manual)
        SharedPreferences prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE);
        String savedName = prefs.getString("profile_name", null);
        String savedEmail = prefs.getString("profile_email", null);
        if (savedName != null && !savedName.isEmpty()) txtName.setText(savedName);
        if (savedEmail != null && !savedEmail.isEmpty()) txtEmail.setText(savedEmail);
    }

    /**
     * Carga el progreso y el historial desde SharedPreferences. Si no existe, crea
     * valores mock de ejemplo y los persiste.
     */
    private void loadProgressAndHistory() {
        SharedPreferences prefs = getSharedPreferences("profile_progress", MODE_PRIVATE);
        int sessions = prefs.getInt("sessions_completed", -1);
        int totalMinutes = prefs.getInt("total_minutes", -1);
        int streak = prefs.getInt("streak_days", -1);

        SharedPreferences.Editor editor = prefs.edit();
        if (sessions == -1) { sessions = 0; editor.putInt("sessions_completed", sessions); }
        if (totalMinutes == -1) { totalMinutes = 0; editor.putInt("total_minutes", totalMinutes); }
        if (streak == -1) { streak = 0; editor.putInt("streak_days", streak); }
        editor.apply();

        // Actualizar vistas de estadísticas (usando los includes con ids statSessions, statTime, statStreak)
        View stat1 = findViewById(R.id.statSessions);
        View stat2 = findViewById(R.id.statTime);
        View stat3 = findViewById(R.id.statStreak);
        if (stat1 != null) {
            TextView tvVal = stat1.findViewById(R.id.tvStatValue);
            TextView tvLabel = stat1.findViewById(R.id.tvStatLabel);
            tvVal.setText(String.valueOf(sessions));
            tvLabel.setText(getString(R.string.profile_sessions_completed));
        }
        if (stat2 != null) {
            TextView tvVal = stat2.findViewById(R.id.tvStatValue);
            TextView tvLabel = stat2.findViewById(R.id.tvStatLabel);
            tvVal.setText(totalMinutes + " min");
            tvLabel.setText(getString(R.string.profile_total_time));
        }
        if (stat3 != null) {
            TextView tvVal = stat3.findViewById(R.id.tvStatValue);
            TextView tvLabel = stat3.findViewById(R.id.tvStatLabel);
            tvVal.setText(String.valueOf(streak));
            tvLabel.setText(getString(R.string.profile_streak));
        }

        // Historial: cargar desde SharedPreferences y conectar a RecyclerView
        SharedPreferences histPrefs = getSharedPreferences("profile_history", MODE_PRIVATE);
        java.util.Set<String> stored = histPrefs.getStringSet("history_set", null);
        if (stored == null || stored.isEmpty()) {
            // crear mock
            java.util.Set<String> mock = new java.util.HashSet<>();
            mock.add("Meditacion|Respiración consciente|2026-05-18");
            mock.add("Sonido|Lluvia relajante|2026-05-17");
            mock.add("Rutina|Empezar el día|2026-05-15");
            histPrefs.edit().putStringSet("history_set", mock).apply();
            stored = mock;
        }

        // Renderizar historial con RecyclerView
        if (historyContainer != null) {
            java.util.List<HistoryEntry> entries = new java.util.ArrayList<>();
            for (String entry : stored) {
                String[] parts = entry.split("\\|");
                if (parts.length >= 3) {
                    String type = parts[0];
                    String title = parts[1];
                    String date = parts[2];
                    int imageRes = mapHistoryImageByTitle(type, title);
                    entries.add(new HistoryEntry(type, title, date, imageRes));
                }
            }
            HistoryAdapter adapter = new HistoryAdapter(entries);
            historyContainer.setAdapter(adapter);
        }
    }

    /**
     * Mapea título/tipo a drawable para tarjetas de historial.
     */
    private int mapHistoryImageByTitle(String type, String title) {
        if ("Meditacion".equals(type)) {
            if (title.contains("Respiración")) return R.drawable.medit1;
            if (title.contains("estrés")) return R.drawable.medit2;
            if (title.contains("dormir")) return R.drawable.medit3;
            return R.drawable.medit1;
        }
        if ("Sonido".equals(type)) {
            if (title.contains("Lluvia")) return R.drawable.lluvia;
            if (title.contains("Fuego")) return R.drawable.fuego;
            if (title.contains("Bosque")) return R.drawable.bosque;
            if (title.contains("Viento")) return R.drawable.viento;
            if (title.contains("Olas")) return R.drawable.olas;
            return R.drawable.lluvia;
        }
        if ("Rutina".equals(type)) {
            if (title.contains("estrés")) return R.drawable.mount;
            if (title.contains("día")) return R.drawable.mount;
            if (title.contains("Dormir")) return R.drawable.mount;
            return R.drawable.mount;
        }
        return R.drawable.mount;
    }

    private void cerrarSesion() {
        // Sign out from Firebase
        mAuth.signOut();

        // Sign out from Google
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(ProfileActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            
            // Regresar al Login y limpiar el stack de actividades
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.putExtra("from_logout", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
