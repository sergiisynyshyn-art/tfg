package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * WelcomeActivity muestra una pantalla de bienvenida después de una conexión exitosa con Google.
 * Muestra información del usuario y luego navega automáticamente a MainActivity.
 */
public class WelcomeActivity extends AppCompatActivity {

    // Handler para el temporizador
    private Handler handler = new Handler();

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

        // Obtener datos del intent
        String nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");
        String emailUsuario = getIntent().getStringExtra("EMAIL_USUARIO");

        // Configurar el mensaje de bienvenida
        configurarMensajeBienvenida(nombreUsuario, emailUsuario);

        // Navegar automáticamente a MainActivity después de 3 segundos
        handler.postDelayed(() -> {
            navegarAMainActivity();
        }, 3000); // 3 segundos
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
            txtBienvenida.setText("¡Bienvenido a Serenum, " + nombre + "!");
        } else {
            txtBienvenida.setText("¡Bienvenido a Serenum!");
        }

        if (email != null && !email.isEmpty()) {
            txtMensaje.setText("Tu cuenta de Google (" + email + ") se ha conectado correctamente.\n\nTe estamos redirigiendo a la aplicación...");
        } else {
            txtMensaje.setText("Tu cuenta de Google se ha conectado correctamente.\n\nTe estamos redirigiendo a la aplicación...");
        }
    }

    /**
     * Navega a la pantalla principal (MainActivity).
     */
    private void navegarAMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        // Limpiar el stack de activities
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
