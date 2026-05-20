package com.example.serenum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * WelcomeActivity muestra una pantalla de bienvenida después de una conexión exitosa con Google.
 * Muestra información del usuario y luego navega a la encuesta inicial.
 */
public class WelcomeActivity extends AppCompatActivity {

    // Handler para el temporizador
    private final Handler handler = new Handler();
    private ImageView imgProfile;
    private MaterialButton btnContinue;
    private MaterialButton btnSignOut;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // Aplica los márgenes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Obtener vistas
        imgProfile = findViewById(R.id.imgProfile);
        btnContinue = findViewById(R.id.btnContinue);
        btnSignOut = findViewById(R.id.btnSignOut);
        ImageButton btnBackWelcome = findViewById(R.id.btnBackWelcome);

        if (btnBackWelcome != null) {
            btnBackWelcome.setOnClickListener(v -> onBackPressed());
        }

        // Obtener datos del intent
        String nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");
        String emailUsuario = getIntent().getStringExtra("EMAIL_USUARIO");

        // Configurar el mensaje de bienvenida
        configurarMensajeBienvenida(nombreUsuario, emailUsuario);

        // Intent: obtener la cuenta de Google si aún está disponible para mostrar foto y metadatos
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Cargar foto si existe
            Uri photo = account.getPhotoUrl();
            if (photo != null) {
                new ImageLoadTask(photo.toString(), imgProfile).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }

        // Mostrar último inicio de sesión si Firebase tiene metadata
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null && currentUser.getMetadata() != null) {
            long lastSignIn = currentUser.getMetadata().getLastSignInTimestamp();
            if (lastSignIn > 0) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy HH:mm", java.util.Locale.getDefault());
                String last = sdf.format(new java.util.Date(lastSignIn));
                TextView txtRedirigiendo = findViewById(R.id.txtRedirigiendo);
                txtRedirigiendo.setText(getString(R.string.last_access, last));
            }
        }

        // Configurar listeners de botones
        btnContinue.setOnClickListener(v -> navegarAEncuestaInicial());

        btnSignOut.setOnClickListener(v -> {
            // Desconectar de Firebase y Google
            if (firebaseAuth != null) {
                firebaseAuth.signOut();
            }

            // Crear un GoogleSignInClient básico para hacer signOut
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            googleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                Toast.makeText(WelcomeActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                // Indicar que venimos de logout para evitar que LoginActivity redirija
                intent.putExtra("from_logout", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        });

        // Nota: eliminada la navegación automática a MainActivity para que la pantalla
        // de bienvenida permanezca hasta que el usuario pulse "Continuar" o cierre sesión.
        // Si se desea un fallback temporal, se puede reactivar con handler.postDelayed.
    }

    /**
     * Configura el mensaje de bienvenida con los datos del usuario.
     *
     * @param nombre Nombre del usuario
     * @param email Email del usuario
     */
    private void configurarMensajeBienvenida(String nombre, String email) {
        TextView txtBienvenida = findViewById(R.id.txtBienvenida);
        TextView txtMensaje = findViewById(R.id.txtMensaje);

        if (nombre != null && !nombre.isEmpty()) {
            txtBienvenida.setText(getString(R.string.welcome_title_with_name, nombre));
        } else {
            txtBienvenida.setText(R.string.welcome_title);
        }

        if (email != null && !email.isEmpty()) {
            txtMensaje.setText(getString(R.string.welcome_message_with_email, email));
        } else {
            txtMensaje.setText(R.string.welcome_message);
        }
    }

    /**
     * Navega a la pantalla de onboarding (antes de la encuesta inicial).
     */
    private void navegarAEncuestaInicial() {
        Intent intent = new Intent(WelcomeActivity.this, OnboardingActivity.class);
        // Pasar el nombre del usuario para mantener la referencia de datos
        intent.putExtra("NOMBRE_USUARIO", getIntent().getStringExtra("NOMBRE_USUARIO"));
        // Pasar también el email para marcar el onboarding como completado
        intent.putExtra("EMAIL_USUARIO", getIntent().getStringExtra("EMAIL_USUARIO"));
        startActivity(intent);
    }

    /**
     * Tarea simple para cargar una imagen desde URL en segundo plano y ponerla en un ImageView.
     */
    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;

        ImageLoadTask(String url, ImageView iv) {
            this.url = url;
            this.imageView = iv;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try (java.io.InputStream in = new java.net.URL(url).openStream()) {
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar el handler para evitar memory leaks
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
