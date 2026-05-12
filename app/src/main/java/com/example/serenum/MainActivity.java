package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * MainActivity es la pantalla principal de la aplicación Serenum.
 * Se muestra después de que el usuario ha iniciado sesión correctamente.
 */
public class MainActivity extends AppCompatActivity {

    // Cliente de Google Sign-In para cerrar sesión
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Aplica los márgenes del sistema (notch, status bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar Google Sign-In para cerrar sesión
        configurarGoogleSignIn();

        // Configurar los listeners de los botones
        setupButtons();
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
     * Configura los listeners de los botones de la pantalla principal.
     */
    private void setupButtons() {
        // TODO: Reemplazar con los botones reales de tu layout
        // Obtener referencias a los botones desde el layout XML
        // Button btnLogout = findViewById(R.id.btnLogout);
        // Button btnPerfil = findViewById(R.id.btnPerfil);

        // Listener para cerrar sesión (ejemplo)
        // btnLogout.setOnClickListener(v -> cerrarSesion());
    }

    /**
     * Cierra la sesión del usuario y vuelve a LoginActivity.
     */
    private void cerrarSesion() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(MainActivity.this,
                    "Sesión cerrada",
                    Toast.LENGTH_SHORT).show();

            // Ir a LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verificar si el usuario está autenticado
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            // Si no está autenticado, ir a LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}