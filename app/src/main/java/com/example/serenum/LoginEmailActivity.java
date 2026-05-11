package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * LoginEmailActivity permite a los usuarios iniciar sesión con email y contraseña.
 * Valida las credenciales y navega a la pantalla principal si el login es exitoso.
 */
public class LoginEmailActivity extends AppCompatActivity {

    // Componentes de la UI
    private EditText etEmail, etPassword;
    private Button btnLogin, btnVolver;

    // DatabaseHelper para gestionar la BD
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_email);

        // Aplica los márgenes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Inicializar componentes de la UI
        inicializarVistas();

        // Configurar listeners de botones
        configurarBotones();
    }

    /**
     * Inicializa las referencias a los componentes de la UI.
     */
    private void inicializarVistas() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnVolver = findViewById(R.id.btnVolver);
    }

    /**
     * Configura los listeners de los botones.
     */
    private void configurarBotones() {
        // Listener para el botón Login
        btnLogin.setOnClickListener(v -> {
            if (validarDatos()) {
                iniciarSesion();
            }
        });

        // Listener para el botón Volver
        btnVolver.setOnClickListener(v -> {
            // Navegar a RegisterActivity
            Intent intent = new Intent(LoginEmailActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish(); // Cerrar LoginEmailActivity
        });
    }

    /**
     * Valida que los campos de email y contraseña estén completos.
     *
     * @return true si los datos son válidos, false en caso contrario
     */
    private boolean validarDatos() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validar email
        if (email.isEmpty()) {
            etEmail.setError("El email es obligatorio");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Ingresa un email válido");
            etEmail.requestFocus();
            return false;
        }

        // Validar contraseña
        if (password.isEmpty()) {
            etPassword.setError("La contraseña es obligatoria");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     */
    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Intentar login
        String[] usuario = databaseHelper.loginUsuario(email, password);

        if (usuario != null) {
            // Login exitoso
            String nombreUsuario = usuario[1]; // nombre
            Toast.makeText(this,
                    "¡Bienvenido de vuelta, " + nombreUsuario + "!",
                    Toast.LENGTH_LONG).show();

            // Ir a la pantalla de bienvenida
            navegarAWelcomActivity(nombreUsuario, email);

        } else {
            // Login fallido
            Toast.makeText(this,
                    "Email o contraseña incorrectos",
                    Toast.LENGTH_LONG).show();

            // Limpiar campo de contraseña
            etPassword.setText("");
            etPassword.requestFocus();
        }
    }

    /**
     * Navega a la pantalla de bienvenida.
     */
    private void navegarAWelcomActivity(String nombre, String email) {
        Intent intent = new Intent(LoginEmailActivity.this, WelcomeActivity.class);
        intent.putExtra("NOMBRE_USUARIO", nombre);
        intent.putExtra("EMAIL_USUARIO", email);
        startActivity(intent);
        finish(); // Cerrar LoginEmailActivity
    }
}
