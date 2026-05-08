package com.example.serenum;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        // Ahora configuramos los listeners (escuchadores) de los botones
        setupLoginButtons();
    }

    /**
     * Este método configura los listeners para los botones del login
     */
    private void setupLoginButtons() {
        // Obtener referencias a los botones desde el layout XML
        Button btnLoginEmail = findViewById(R.id.btnLoginEmail);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button txtGoRegister = findViewById(R.id.txtGoRegister);

        // Listener para el botón Google
        btnGoogle.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,
                "Google Sign In - pendiente de implementar",
                Toast.LENGTH_SHORT).show();
        });

        // Listener para el botón "Iniciar sesión con correo"
        btnLoginEmail.setOnClickListener(v -> {
            // Cuando el usuario toca el botón, muestra un mensaje temporal (Toast)
            Toast.makeText(MainActivity.this,
                "Login con correo - pendiente de implementar",
                Toast.LENGTH_SHORT).show();

            // TODO: Aquí irá la lógica para abrir la pantalla de login con email
            // Por ahora solo mostramos el mensaje
        });

        // Listener para el texto "¿No tienes cuenta?"
        txtGoRegister.setOnClickListener(v -> {
            // Cuando el usuario toca el texto, muestra otro mensaje
            Toast.makeText(MainActivity.this,
                "Ir a registro - pendiente de implementar",
                Toast.LENGTH_SHORT).show();

            // TODO: Aquí irá la lógica para abrir la pantalla de registro
        });
    }
}