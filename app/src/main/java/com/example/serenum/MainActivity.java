package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.os.Handler;
import android.os.Looper;
import android.media.MediaPlayer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.serenum.managers.DailyRecommendationManager;
import com.example.serenum.models.DailyRecommendation;

/**
 * MainActivity es la pantalla principal de la aplicación Serenum.
 * Se muestra después de que el usuario ha iniciado sesión correctamente.
 */
public class MainActivity extends AppCompatActivity {

    // Cliente de Google Sign-In para cerrar sesión
    private GoogleSignInClient mGoogleSignInClient;
    private Handler dailyRecommendationHandler;
    private Runnable dailyRecommendationRefreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Usar el layout de la pantalla principal (home)
        setContentView(R.layout.activity_home);

        Log.d("MainActivity", "onCreate: activity_home.xml cargado");

        // Aplica los márgenes del sistema (notch, status bar, etc.) sobre el contenedor de contenido
        // (no sobre el root 'main') para preservar el padding definido en XML.
        View contentContainer = findViewById(R.id.contentContainer);
        if (contentContainer != null) {
            final int basePaddingLeft = contentContainer.getPaddingLeft();
            final int basePaddingTop = contentContainer.getPaddingTop();
            final int basePaddingRight = contentContainer.getPaddingRight();
            final int basePaddingBottom = contentContainer.getPaddingBottom();

            ViewCompat.setOnApplyWindowInsetsListener(contentContainer, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(
                        basePaddingLeft + systemBars.left,
                        basePaddingTop + systemBars.top,
                        basePaddingRight + systemBars.right,
                        basePaddingBottom + systemBars.bottom
                );
                return insets;
            });
        }

        // Configurar el saludo personalizado
        configurarSaludo();

        // Configurar Google Sign-In para cerrar sesión
        configurarGoogleSignIn();

        // Configurar listeners basicos del esqueleto Home
        setupButtons();
    }

    /**
     * Configura el saludo personalizado basado en el nombre del usuario.
     */
    private void configurarSaludo() {
        TextView txtGreeting = findViewById(R.id.txtGreeting);
        if (txtGreeting != null) {
            String nombre = getIntent().getStringExtra("NOMBRE_USUARIO");
            if (nombre != null && !nombre.isEmpty()) {
                txtGreeting.setText("Hola, " + nombre + " 👋");
            } else {
                // Si no hay nombre en el intent, intentar obtenerlo de Firebase
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                    txtGreeting.setText("Hola, " + user.getDisplayName() + " 👋");
                }
                // Si aún así no hay nombre, se queda el placeholder del XML
            }
        }
    }

    /**
     * Configura Google Sign-In para poder cerrar sesión.
     */
    private void configurarGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * Configura listeners basicos para la estructura Home.
     */
    private void setupButtons() {
        View cardMeditations = findViewById(R.id.cardMeditations);
        View cardSounds = findViewById(R.id.cardSounds);
        View cardRoutines = findViewById(R.id.cardRoutines);
        View navHome = findViewById(R.id.navHome);
        View navFavorites = findViewById(R.id.navFavorites);
        View navProfile = findViewById(R.id.navProfile);

        if (cardMeditations != null) {
            cardMeditations.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MeditationsActivity.class);
                startActivity(intent);
            });
        }

        if (cardSounds != null) {
            cardSounds.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SoundsActivity.class);
                startActivity(intent);
            });
        }

        if (cardRoutines != null) {
            cardRoutines.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RoutinesActivity.class);
                startActivity(intent);
            });
        }

        setToastClick(navHome, "Home");
        if (navFavorites != null) {
            navFavorites.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // Configurar tarjeta de recomendación del día
        setupDailyRecommendation();

    }

    /**
     * Configura la tarjeta de recomendación del día con rotación cada 20 segundos
     */
    private void setupDailyRecommendation() {
        // Elementos de la tarjeta de recomendación del día
        MaterialCardView cardRecommendation = findViewById(R.id.cardRecommendation);
        ImageView imgRecommendation = findViewById(R.id.imgRecommendation);
        TextView tvRecommendationSubtitle = findViewById(R.id.tvRecommendationSubtitle);
        TextView tvRecommendationTitle = findViewById(R.id.tvRecommendationTitle);
        TextView tvRecommendationDescription = findViewById(R.id.tvRecommendationDescription);
        TextView tvRecommendationDuration = findViewById(R.id.tvRecommendationDuration);
        MaterialButton btnOpenRecommendation = findViewById(R.id.btnOpenRecommendation);

        // Elementos de la tarjeta de contenido destacado
        MaterialCardView cardFeatured = findViewById(R.id.cardFeaturedContent);
        ImageView imgFeatured = findViewById(R.id.imgFeaturedContent);
        TextView tvFeaturedSubtitle = findViewById(R.id.tvFeaturedSubtitle);
        TextView tvFeaturedTitle = findViewById(R.id.tvFeaturedTitle);
        TextView tvFeaturedDescription = findViewById(R.id.tvFeaturedDescription);

        dailyRecommendationHandler = new Handler(Looper.getMainLooper());

        dailyRecommendationRefreshRunnable = new Runnable() {
            @Override
            public void run() {
                DailyRecommendation featuredRecommendation = DailyRecommendationManager.getCurrentFeaturedRecommendation();
                DailyRecommendation dailyRecommendation = DailyRecommendationManager.getCurrentRecommendation();
                updateRecommendationUI(cardFeatured, imgFeatured, tvFeaturedSubtitle, tvFeaturedTitle, tvFeaturedDescription, featuredRecommendation, true);
                updateDailyRecommendationButton(cardRecommendation, imgRecommendation, tvRecommendationSubtitle, tvRecommendationTitle, tvRecommendationDescription, tvRecommendationDuration, dailyRecommendation, true);
                dailyRecommendationHandler.postDelayed(this, 20000);
            }
        };

        dailyRecommendationHandler.post(dailyRecommendationRefreshRunnable);

        // Hacer las tarjetas clickables
        if (cardFeatured != null) {
            cardFeatured.setOnClickListener(v -> openRecommendation(DailyRecommendationManager.getCurrentFeaturedRecommendation()));
        }

        if (cardRecommendation != null) {
            cardRecommendation.setOnClickListener(v -> openRecommendation(DailyRecommendationManager.getCurrentRecommendation()));
        }

        if (btnOpenRecommendation != null) {
            btnOpenRecommendation.setOnClickListener(v -> openRecommendation(DailyRecommendationManager.getCurrentRecommendation()));
        }
    }

    /**
     * Actualiza la UI de la recomendación y opcionalmente anima el refresco.
     */
    private void updateRecommendationUI(MaterialCardView cardFeatured,
                                        ImageView imgFeatured,
                                        TextView tvFeaturedSubtitle,
                                        TextView tvFeaturedTitle,
                                        TextView tvFeaturedDescription,
                                        DailyRecommendation recommendation,
                                        boolean animate) {
        if (recommendation == null) return;

        if (cardFeatured != null && animate) {
            cardFeatured.animate().cancel();
            cardFeatured.setAlpha(0.35f);
        }

        if (imgFeatured != null) imgFeatured.setImageResource(recommendation.getImageResId());
        if (tvFeaturedSubtitle != null) tvFeaturedSubtitle.setText(recommendation.getSubtitle());
        if (tvFeaturedTitle != null) tvFeaturedTitle.setText(recommendation.getTitle());
        if (tvFeaturedDescription != null) tvFeaturedDescription.setText(recommendation.getDescription());

        if (cardFeatured != null && animate) {
            cardFeatured.animate().alpha(1f).setDuration(250).start();
        }
    }

    /**
     * Actualiza la tarjeta de recomendación del día con animación opcional.
     */
    private void updateDailyRecommendationButton(MaterialCardView cardRecommendation,
                                                  ImageView imgRecommendation,
                                                  TextView tvRecommendationSubtitle,
                                                  TextView tvRecommendationTitle,
                                                  TextView tvRecommendationDescription,
                                                  TextView tvRecommendationDuration,
                                                  DailyRecommendation recommendation,
                                                  boolean animate) {
        if (recommendation == null) return;

        if (cardRecommendation != null && animate) {
            cardRecommendation.animate().cancel();
            cardRecommendation.setAlpha(0.35f);
        }

        if (imgRecommendation != null) imgRecommendation.setImageResource(recommendation.getImageResId());
        if (tvRecommendationSubtitle != null) tvRecommendationSubtitle.setText(recommendation.getSubtitle());
        if (tvRecommendationTitle != null) tvRecommendationTitle.setText(recommendation.getTitle());
        if (tvRecommendationDescription != null) tvRecommendationDescription.setText(recommendation.getDescription());
        if (tvRecommendationDuration != null) tvRecommendationDuration.setText("Duración: " + recommendation.getDuration());

        if (cardRecommendation != null && animate) {
            cardRecommendation.animate().alpha(1f).setDuration(250).start();
        }
    }

    /**
     * Abre la recomendación correspondiente.
     */
    private void openRecommendation(DailyRecommendation recommendation) {
        if (recommendation == null) return;

        Intent intent = new Intent(MainActivity.this, recommendation.getTargetActivity());

        if ("meditation".equals(recommendation.getType()) || "sound".equals(recommendation.getType())) {
            intent.putExtra("title", recommendation.getTitle());
            intent.putExtra("duration", recommendation.getDuration());
            intent.putExtra("description", recommendation.getDescription());
            intent.putExtra("audio_name", recommendation.getAudioName());
            intent.putExtra("image_res", recommendation.getImageResId());
        }

        startActivity(intent);
    }

    private void setToastClick(View view, String message) {
        if (view != null) {
            view.setOnClickListener(v -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Cierra la sesión del usuario y vuelve a LoginActivity.
     */
    private void cerrarSesion() {
        // Asegurarse de cerrar sesión también en Firebase (sincronizado)
        try {
            FirebaseAuth.getInstance().signOut();
        } catch (Exception e) {
            // Ignorar si Firebase no está configurado
        }

        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(MainActivity.this,
                    "Sesión cerrada",
                    Toast.LENGTH_SHORT).show();

            // Ir a LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // Indicar que venimos de un logout para evitar que LoginActivity redirija
            // inmediatamente de vuelta a MainActivity por un cache temporal de cuenta.
            intent.putExtra("from_logout", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart: MainActivity visible");
        // No redirigir aquí - confía en LoginActivity para hacer el flujo correcto
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar Handler para evitar memory leaks
        if (dailyRecommendationHandler != null && dailyRecommendationRefreshRunnable != null) {
            dailyRecommendationHandler.removeCallbacks(dailyRecommendationRefreshRunnable);
        }
    }

    /**
     * Método público vinculado desde layouts XML (android:onClick="playMeditation").
     * Lee el `tag` del botón (si se ha puesto como referencia @raw/...) y reproduce el
     * recurso raw correspondiente. Si no se encuentra tag válido, usa `R.raw.medit1`.
     */
    public void playMeditation(View v) {
        int resId = R.raw.medit1; // fallback

        Object tag = v.getTag();
        if (tag != null) {
            if (tag instanceof Integer) {
                resId = (Integer) tag;
            } else {
                try {
                    resId = Integer.parseInt(tag.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        }

        try {
            MediaPlayer mp = MediaPlayer.create(this, resId);
            if (mp != null) {
                mp.setOnCompletionListener(MediaPlayer::release);
                mp.start();
                Toast.makeText(this, "Reproduciendo...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se pudo reproducir la meditación", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al reproducir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}