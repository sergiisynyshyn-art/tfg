package com.example.serenum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

/**
 * LoginEmailActivity permite a los usuarios iniciar sesión con email y contraseña.
 * Valida las credenciales y navega a la pantalla principal si el login es exitoso.
 */
public class LoginEmailActivity extends AppCompatActivity {

    // Componentes de la UI
    private EditText etEmail, etPassword;
    private Button btnLogin, btnVolver;
    private ImageButton btnBackLoginEmail;
    private ImageButton btnTogglePassword;

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
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnVolver = findViewById(R.id.btnVolver);
        btnBackLoginEmail = findViewById(R.id.btnBackLoginEmail);
        // Mejorar compatibilidad con el Autofill Framework (gestores de contraseñas)
        try {
            etEmail.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
            etPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        } catch (Exception ignored) {
            // En API antiguas puede no existir el método; ignorar.
        }
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

        if (btnBackLoginEmail != null) {
            btnBackLoginEmail.setOnClickListener(v -> onBackPressed());
        }

        // Toggle para mostrar/ocultar contraseña (mantiene aspecto visual igual al campo de email)
        if (btnTogglePassword != null && etPassword != null) {
            btnTogglePassword.setOnClickListener(v -> {
                // Si la contraseña está oculta, mostrarla
                if (etPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnTogglePassword.setImageResource(R.drawable.m3_avd_show_password);
                    btnTogglePassword.setContentDescription("Ocultar contraseña");
                } else {
                    // Ocultar contraseña
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnTogglePassword.setImageResource(R.drawable.m3_avd_hide_password);
                    btnTogglePassword.setContentDescription("Mostrar contraseña");
                }
                // Mantener el cursor al final
                etPassword.setSelection(etPassword.getText() != null ? etPassword.getText().length() : 0);
            });
            // Asegurar que inicialmente esté oculta
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btnTogglePassword.setImageResource(R.drawable.m3_avd_hide_password);
        }
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
            if (hasUserCompletedOnboarding(email)) {
                Toast.makeText(this,
                        "¡Bienvenido de vuelta, " + nombreUsuario + "!",
                        Toast.LENGTH_LONG).show();
                navegarAMainActivity(nombreUsuario);
            } else {
                Toast.makeText(this,
                        "¡Bienvenido, " + nombreUsuario + "!",
                        Toast.LENGTH_LONG).show();
                // Solo usuarios nuevos o sin onboarding completo pasan por bienvenida/onboarding
                navegarAWelcomActivity(nombreUsuario, email);
            }

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

    private void navegarAMainActivity(String nombre) {
        Intent intent = new Intent(LoginEmailActivity.this, MainActivity.class);
        intent.putExtra("NOMBRE_USUARIO", nombre);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean hasUserCompletedOnboarding(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        SharedPreferences prefs = getSharedPreferences("onboarding_prefs", MODE_PRIVATE);
        return prefs.getBoolean("onboarding_completed_" + normalizedEmail, false);
    }
}
