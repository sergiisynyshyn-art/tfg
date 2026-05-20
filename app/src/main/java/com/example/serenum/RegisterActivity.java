package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * RegisterActivity permite a los usuarios registrarse con email y contraseña.
 * Valida los datos y guarda al usuario en la base de datos.
 */
public class RegisterActivity extends AppCompatActivity {

    // Componentes de la UI
    private EditText etNombre, etEmail, etPassword, etConfirmPassword;
    private Button btnRegistrar, btnVolver;
    private ImageButton btnBackRegister;

    // DatabaseHelper para gestionar la BD
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

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
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);
        btnBackRegister = findViewById(R.id.btnBackRegister);
    }

    /**
     * Configura los listeners de los botones.
     */
    private void configurarBotones() {
        // Listener para el botón Registrar
        btnRegistrar.setOnClickListener(v -> {
            if (validarDatos()) {
                registrarUsuario();
            }
        });

        // Listener para el botón Volver
        btnVolver.setOnClickListener(v -> {
            // Navegar a LoginEmailActivity
            Intent intent = new Intent(RegisterActivity.this, LoginEmailActivity.class);
            startActivity(intent);
            finish(); // Cerrar RegisterActivity
        });

        if (btnBackRegister != null) {
            btnBackRegister.setOnClickListener(v -> onBackPressed());
        }
    }

    /**
     * Valida que todos los campos estén completos y correctos.
     *
     * @return true si todos los datos son válidos, false en caso contrario
     */
    private boolean validarDatos() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validar nombre
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            etNombre.requestFocus();
            return false;
        }

        if (nombre.length() < 2) {
            etNombre.setError("El nombre debe tener al menos 2 caracteres");
            etNombre.requestFocus();
            return false;
        }

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

        if (password.length() < 6) {
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            etPassword.requestFocus();
            return false;
        }

        // Validar confirmación de contraseña
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Confirma tu contraseña");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Registra al usuario en la base de datos.
     */
    private void registrarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Intentar registrar al usuario
        long resultado = databaseHelper.registrarUsuario(nombre, email, password);

        if (resultado > 0) {
            // Registro exitoso
            Toast.makeText(this,
                    "¡Registro exitoso! Bienvenido " + nombre,
                    Toast.LENGTH_LONG).show();

            // Enviar correo de bienvenida
            enviarCorreoBienvenida(email, nombre);

            // Ir a la pantalla de bienvenida
            navegarAWelcomActivity();

        } else if (resultado == -1) {
            // Usuario ya existe
            etEmail.setError("Este email ya está registrado");
            etEmail.requestFocus();
        } else {
            // Error en el registro
            Toast.makeText(this,
                    "Error al registrar usuario. Inténtalo de nuevo.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Envía un correo de bienvenida al usuario registrado.
     *
     * @param email Email del usuario
     * @param nombre Nombre del usuario
     */
    private void enviarCorreoBienvenida(String email, String nombre) {
        EmailSender.EmailCallback callback = new EmailSender.EmailCallback() {
            @Override
            public void onSuccess() {
                // Correo enviado exitosamente
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this,
                        "Correo de bienvenida enviado",
                        Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(String error) {
                // Error al enviar correo
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this,
                        "Error al enviar correo: " + error,
                        Toast.LENGTH_LONG).show());
            }
        };

        EmailSender.enviarCorreoBienvenida(RegisterActivity.this, email, nombre, callback);
    }

    /**
     * Navega a la pantalla de bienvenida.
     */
    private void navegarAWelcomActivity() {
        Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
        intent.putExtra("NOMBRE_USUARIO", etNombre.getText().toString().trim());
        intent.putExtra("EMAIL_USUARIO", etEmail.getText().toString().trim());
        startActivity(intent);
        finish(); // Cerrar RegisterActivity
    }
}
