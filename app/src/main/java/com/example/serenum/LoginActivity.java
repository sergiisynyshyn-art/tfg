package com.example.serenum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * LoginActivity proporciona la interfaz de inicio de sesión con Google y correo.
 * Se encarga de gestionar la autenticación de Google Sign-In y guardar los datos en SQLite.
 */
public class LoginActivity extends AppCompatActivity {

    // Cliente de Google Sign-In
    private GoogleSignInClient mGoogleSignInClient;

    // DatabaseHelper para gestionar la BD
    private DatabaseHelper databaseHelper;

    // Variables para almacenar los datos del usuario autenticado
    private GoogleSignInAccount googleSignInAccount;

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

        // Inicializar el DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Configurar Google Sign-In
        configurarGoogleSignIn();

        // Configurar los listeners de los botones
        setupLoginButtons();
    }

    /**
     * Configura Google Sign-In con las opciones necesarias.
     * Se configura para obtener el nombre, email e ID de Google del usuario.
     */
    private void configurarGoogleSignIn() {
        // Configurar las opciones de Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // Solicitar el email básico del usuario
                .requestEmail()
                // Opcional: solicitar ID del usuario de Google
                .requestId()
                // Opcional: solicitar perfil del usuario
                .requestProfile()
                .build();

        // Crear cliente de Google Sign-In
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * Configura los listeners (escuchadores) de los botones del login.
     */
    private void setupLoginButtons() {
        // Obtener referencias a los botones desde el layout XML (MaterialButton para consistencia)
        MaterialButton btnLoginEmail = findViewById(R.id.btnLoginEmail);
        MaterialButton btnGoogle = findViewById(R.id.btnGoogle);
        MaterialButton txtGoRegister = findViewById(R.id.txtGoRegister);

        // Crear el ActivityResultLauncher para Google Sign-In
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        handlerSignInResult(result.getData());
                    }
                }
        );

        // Listener para el botón Google
        btnGoogle.setOnClickListener(v -> {
            // Mostrar mensaje de inicio de sesión
            Toast.makeText(LoginActivity.this,
                    "Iniciando sesión con Google...",
                    Toast.LENGTH_SHORT).show();

            // Lanzar el intent de Google Sign-In
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });

        // Listener para el botón "Iniciar sesión con correo"
        btnLoginEmail.setOnClickListener(v -> {
            // Navegar a LoginEmailActivity
            Intent intent = new Intent(LoginActivity.this, LoginEmailActivity.class);
            startActivity(intent);
        });

        // Listener para el texto "¿No tienes cuenta?"
        txtGoRegister.setOnClickListener(v -> {
            // Navegar a RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Maneja el resultado del Google Sign-In.
     * Si el login es exitoso, extrae los datos del usuario, los guarda en SQLite
     * y envía un correo de bienvenida.
     *
     * @param data Intent con los datos del resultado
     */
    private void handlerSignInResult(Intent data) {
        try {
            // Obtener la tarea de Google Sign-In
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            // Si la tarea es exitosa
            googleSignInAccount = task.getResult(ApiException.class);

            if (googleSignInAccount != null) {
                // Extraer datos del usuario
                String nombre = googleSignInAccount.getDisplayName();
                String email = googleSignInAccount.getEmail();
                String googleId = googleSignInAccount.getId();

                // Validar que los datos no sean nulos
                if (nombre != null && email != null && googleId != null) {
                    // Mostrar mensaje de éxito de autenticación
                    Toast.makeText(this,
                            "¡Autenticación exitosa! " + nombre,
                            Toast.LENGTH_SHORT).show();

                    // Guardar datos en SQLite
                    guardarUsuarioEnBD(nombre, email, googleId);
                } else {
                    mostrarError("Algunos datos del usuario no están disponibles");
                }
            }

        } catch (ApiException e) {
            // Si hay error en Google Sign-In
            mostrarError("Error en Google Sign-In: " + e.getMessage());
        }
    }

    /**
     * Guarda los datos del usuario en la BD SQLite.
     * Si el usuario no existe, lo inserta y envía un correo de bienvenida.
     *
     * @param nombre Nombre del usuario
     * @param email Email del usuario
     * @param googleId ID de Google del usuario
     */
    private void guardarUsuarioEnBD(String nombre, String email, String googleId) {
        // Insertar usuario en la BD
        long resultado = databaseHelper.insertarUsuario(nombre, email, googleId);

        if (resultado > 0) {
            // Usuario insertado correctamente
            Toast.makeText(this,
                    "Usuario guardado en BD correctamente",
                    Toast.LENGTH_SHORT).show();

            // Enviar correo de bienvenida
            enviarCorreoBienvenida(email, nombre);

        } else if (resultado == -1) {
            // Usuario ya existe en la BD
            Toast.makeText(this,
                    "El usuario ya existe en el sistema",
                    Toast.LENGTH_SHORT).show();

            // Ir a la pantalla principal sin enviar correo
            navegarAMainActivity();
        } else {
            // Error en la inserción
            mostrarError("Error al guardar el usuario en la BD");
        }
    }

    /**
     * Envía un correo de bienvenida al usuario usando EmailSender.
     *
     * @param email Email del usuario
     * @param nombre Nombre del usuario
     */
    private void enviarCorreoBienvenida(String email, String nombre) {
        // Crear callback para el envío de correo
        EmailSender.EmailCallback callback = new EmailSender.EmailCallback() {
            @Override
            public void onSuccess() {
                // Correo enviado exitosamente
                Toast.makeText(LoginActivity.this,
                        "Correo de bienvenida enviado",
                        Toast.LENGTH_SHORT).show();

                // Navegar a la pantalla de bienvenida
                navegarAWelcomActivity();
            }

            @Override
            public void onError(String error) {
                // Error al enviar correo
                Toast.makeText(LoginActivity.this,
                        "Error al enviar correo: " + error,
                        Toast.LENGTH_LONG).show();

                // Navegar a la pantalla de bienvenida de todas formas
                navegarAWelcomActivity();
            }
        };

        // Enviar correo de bienvenida
        EmailSender.enviarCorreoBienvenida(email, nombre, callback);
    }

    /**
     * Navega a la pantalla principal (MainActivity).
     */
    private void navegarAMainActivity() {
        // Crear un intent para ir a MainActivity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        // Cuando vaya a MainActivity, no se puede volver atrás con el botón de atrás
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Iniciar MainActivity
        startActivity(intent);

        // Cerrar LoginActivity
        finish();
    }

    /**
     * Navega a la pantalla de bienvenida (WelcomeActivity).
     */
    private void navegarAWelcomActivity() {
        // Crear un intent para ir a WelcomeActivity
        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);

        // Pasar los datos del usuario a WelcomeActivity
        if (googleSignInAccount != null) {
            intent.putExtra("NOMBRE_USUARIO", googleSignInAccount.getDisplayName());
            intent.putExtra("EMAIL_USUARIO", googleSignInAccount.getEmail());
        }

        // Iniciar WelcomeActivity
        startActivity(intent);

        // Cerrar LoginActivity
        finish();
    }

    /**
     * Muestra un mensaje de error al usuario.
     *
     * @param mensaje Mensaje de error a mostrar
     */
    private void mostrarError(String mensaje) {
        Toast.makeText(this,
                mensaje,
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verificar si el usuario ya está autenticado
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Si el usuario ya está autenticado, ir a MainActivity
            navegarAMainActivity();
        }
    }
}
