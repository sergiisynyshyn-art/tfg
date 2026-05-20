package com.example.serenum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import android.util.Log;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Locale;

/**
 * LoginActivity proporciona la interfaz de inicio de sesión con Google y correo.
 * Se encarga de gestionar la autenticación de Google Sign-In y guardar los datos en SQLite.
 */
public class LoginActivity extends AppCompatActivity {

    // Cliente de Google Sign-In
    private GoogleSignInClient mGoogleSignInClient;

    // Firebase Auth para autenticar realmente al usuario
    private FirebaseAuth firebaseAuth;

    // Web client id generado por google-services; puede no existir si Firebase no está completo
    private String webClientId;

    // DatabaseHelper para gestionar la BD
    private DatabaseHelper databaseHelper;

    // Variables para almacenar los datos del usuario autenticado
    private GoogleSignInAccount googleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d("LoginActivity", "onCreate: iniciando");
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            Log.d("LoginActivity", "onCreate: layout establecido");

            // Aplica los márgenes del sistema (notch, status bar, etc.)
            android.view.View mainContainer = findViewById(R.id.main);
            final int basePaddingLeft = mainContainer.getPaddingLeft();
            final int basePaddingTop = mainContainer.getPaddingTop();
            final int basePaddingRight = mainContainer.getPaddingRight();
            final int basePaddingBottom = mainContainer.getPaddingBottom();

            ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(
                        basePaddingLeft + systemBars.left,
                        basePaddingTop + systemBars.top,
                        basePaddingRight + systemBars.right,
                        basePaddingBottom + systemBars.bottom
                );
                return insets;
            });
            Log.d("LoginActivity", "onCreate: window insets configurado");

            // Inicializar el DatabaseHelper
            try {
                databaseHelper = new DatabaseHelper(this);
                Log.d("LoginActivity", "onCreate: DatabaseHelper inicializado");
            } catch (Exception e) {
                Log.e("LoginActivity", "Error inicializando DatabaseHelper", e);
                Toast.makeText(this, "Error BD: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // Inicializar FirebaseAuth
            try {
                firebaseAuth = FirebaseAuth.getInstance();
                Log.d("LoginActivity", "onCreate: Firebase inicializado");
            } catch (Exception e) {
                Log.e("LoginActivity", "Error inicializando Firebase", e);
                Toast.makeText(this, "Error Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // Configurar Google Sign-In
            try {
                configurarGoogleSignIn();
                Log.d("LoginActivity", "onCreate: Google Sign-In configurado");
            } catch (Exception e) {
                Log.e("LoginActivity", "Error configurando Google Sign-In", e);
                Toast.makeText(this, "Error GoogleSignIn: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // Configurar los listeners de los botones
            try {
                setupLoginButtons();
                Log.d("LoginActivity", "onCreate: Botones configurados");
            } catch (Exception e) {
                Log.e("LoginActivity", "Error configurando botones", e);
                Toast.makeText(this, "Error botones: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            Log.d("LoginActivity", "onCreate: completado exitosamente");
        } catch (Exception e) {
            Log.e("LoginActivity", "Error fatal en onCreate", e);
            Toast.makeText(this, "Error fatal: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Configura Google Sign-In con las opciones necesarias.
     * Se configura para obtener el nombre, email e ID de Google del usuario.
     */
    private void configurarGoogleSignIn() {
        webClientId = obtenerWebClientId();

        // Configurar las opciones de Google Sign-In
        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // Solicitar el email básico del usuario
                .requestEmail()
                // Opcional: solicitar ID del usuario de Google
                .requestId()
                // Opcional: solicitar perfil del usuario
                .requestProfile();

        // Si Firebase generó un web client id, lo usamos para obtener el idToken
        if (!TextUtils.isEmpty(webClientId)) {
            gsoBuilder.requestIdToken(webClientId);
        }

        GoogleSignInOptions gso = gsoBuilder.build();

        // Crear cliente de Google Sign-In
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * Configura los listeners (escuchadores) de los botones del login.
     */
    private void setupLoginButtons() {
        try {
            // Obtener referencias a los botones desde el layout XML (MaterialButton para consistencia)
            MaterialButton btnLoginEmail = findViewById(R.id.btnLoginEmail);
            MaterialButton btnGoogle = findViewById(R.id.btnGoogle);
            MaterialButton txtGoRegister = findViewById(R.id.txtGoRegister);

            if (btnLoginEmail == null || btnGoogle == null || txtGoRegister == null) {
                Log.e("LoginActivity", "Una o más vistas no encontradas en el layout");
                Toast.makeText(this, "Error: Layout incorrecto", Toast.LENGTH_LONG).show();
                return;
            }

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
                if (mGoogleSignInClient == null) {
                    mostrarError("Google Sign-In no está configurado correctamente");
                    return;
                }

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
        } catch (Exception e) {
            Log.e("LoginActivity", "Error en setupLoginButtons", e);
            Toast.makeText(this, "Error configurando botones: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
                // Extraer datos del usuario de Google
                String nombre = googleSignInAccount.getDisplayName();
                String email = googleSignInAccount.getEmail();
                String googleId = googleSignInAccount.getId();

                // Validar que los datos no sean nulos
                if (nombre == null || email == null || googleId == null) {
                    mostrarError("Algunos datos del usuario no están disponibles");
                    return;
                }

                // Si existe idToken, autenticamos con Firebase. Si no, seguimos con el flujo existente.
                String idToken = googleSignInAccount.getIdToken();
                if (!TextUtils.isEmpty(idToken)) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, authTask -> {
                        if (authTask.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(this,
                                        "¡Autenticación Firebase exitosa! " + nombre,
                                        Toast.LENGTH_SHORT).show();
                                guardarUsuarioEnBD(nombre, email, googleId);
                            } else {
                                mostrarError("Firebase autenticó pero el usuario no está disponible");
                            }
                        } else {
                            String error = authTask.getException() != null ? authTask.getException().getMessage() : "error desconocido";
                            mostrarError("Error en Firebase Auth: " + error);
                            // Fallback: si Google Sign-In funciona pero Firebase no está completo, mantenemos el flujo existente.
                            guardarUsuarioEnBD(nombre, email, googleId);
                        }
                    });
                } else {
                    Toast.makeText(this,
                            "Google Sign-In completado, pero falta configurar Firebase Auth (idToken no disponible)",
                            Toast.LENGTH_LONG).show();

                    // Fallback para no romper el flujo si Firebase aún no está completo
                    guardarUsuarioEnBD(nombre, email, googleId);
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
            // Enviar igualmente un correo notificando la conexión (sin bloquear la navegación)
            Log.d("LoginActivity", "guardarUsuarioEnBD: usuario existe, enviando correo de notificacion a " + email);
            EmailSender.EmailCallback notifyCallback = new EmailSender.EmailCallback() {
                @Override
                public void onSuccess() {
                    Log.d("LoginActivity", "Correo de notificación enviado correctamente a " + email);
                }

                @Override
                public void onError(String error) {
                    Log.e("LoginActivity", "Error enviando correo de notificación: " + error);
                }
            };
            EmailSender.enviarCorreoBienvenida(LoginActivity.this, email, nombre, notifyCallback);

            // Usuario ya existente: no repetir onboarding. Si faltaba marca local,
            // la registramos como migración para evitar ciclos.
            if (!hasUserCompletedOnboarding(email)) {
                markUserOnboardingCompleted(email);
            }
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
                navegarAWelcomActivity(nombre, email);
            }

            @Override
            public void onError(String error) {
                // Error al enviar correo
                Toast.makeText(LoginActivity.this,
                        "Error al enviar correo: " + error,
                        Toast.LENGTH_LONG).show();

                // Navegar a la pantalla de bienvenida de todas formas
                navegarAWelcomActivity(nombre, email);
            }
        };

        // Enviar correo de bienvenida
        EmailSender.enviarCorreoBienvenida(LoginActivity.this, email, nombre, callback);
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
    private void navegarAWelcomActivity(String nombre, String email) {
        // Crear un intent para ir a WelcomeActivity
        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);

        // Pasar los datos del usuario a WelcomeActivity
        intent.putExtra("NOMBRE_USUARIO", nombre);
        intent.putExtra("EMAIL_USUARIO", email);

        // Iniciar WelcomeActivity
        startActivity(intent);

        // Cerrar LoginActivity
        finish();
    }

    /**
     * Navega a la pantalla de onboarding.
     */
    private void navegarAOnboarding(String nombre, String email) {
        Intent intent = new Intent(LoginActivity.this, OnboardingActivity.class);
        intent.putExtra("NOMBRE_USUARIO", nombre);
        intent.putExtra("EMAIL_USUARIO", email);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Verifica si el usuario ya completó el onboarding.
     */
    private boolean hasUserCompletedOnboarding(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        SharedPreferences prefs = getSharedPreferences("onboarding_prefs", MODE_PRIVATE);
        return prefs.getBoolean("onboarding_completed_" + normalizedEmail, false);
    }

    /**
     * Marca onboarding como completado para usuarios existentes (migración de flujo).
     */
    private void markUserOnboardingCompleted(String email) {
        if (email == null || email.isEmpty()) {
            return;
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        SharedPreferences prefs = getSharedPreferences("onboarding_prefs", MODE_PRIVATE);
        prefs.edit().putBoolean("onboarding_completed_" + normalizedEmail, true).apply();
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
        Log.d("LoginActivity", "onStart: Verificando autenticación");
        // Si la actividad fue lanzada tras un logout explícito, no redirigir
        boolean fromLogout = getIntent() != null && getIntent().getBooleanExtra("from_logout", false);
        if (fromLogout) {
            Log.d("LoginActivity", "onStart: Lanzado desde logout - no redirigir");
            // Limpiar el intent para que futuros onStart no vean este flag
            setIntent(new Intent());
            return;
        }
        // Verificar si el usuario ya está autenticado
        FirebaseUser currentUser = firebaseAuth != null ? firebaseAuth.getCurrentUser() : null;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (currentUser != null || account != null) {
            // Si ya hay sesión cacheada, solo auto-redirigimos a Main para cuentas conocidas.
            // Nunca forzamos onboarding automático al arrancar la app.
            String email = currentUser != null ? currentUser.getEmail() : (account != null ? account.getEmail() : null);
            Log.d("LoginActivity", "onStart: Usuario autenticado con email: " + email);

            if (TextUtils.isEmpty(email)) {
                Log.d("LoginActivity", "onStart: sesión cacheada sin email, mantener pantalla de login");
                return;
            }

            boolean completedOnboarding = hasUserCompletedOnboarding(email);
            boolean existsInLocalDb = databaseHelper != null && databaseHelper.usuarioExiste(email);

            if (completedOnboarding || existsInLocalDb) {
                if (!completedOnboarding && existsInLocalDb) {
                    // Backfill para usuarios antiguos que ya existían antes de la marca de onboarding.
                    markUserOnboardingCompleted(email);
                }
                Log.d("LoginActivity", "onStart: cuenta conocida, navegando a MainActivity");
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    navegarAMainActivity();
                }, 500);
            } else {
                // Cuenta no registrada localmente: esperar login explícito para arrancar el flujo nuevo.
                Log.d("LoginActivity", "onStart: cuenta no reconocida, mantener pantalla de login");
            }
        }
    }

    /**
     * Obtiene el web client id generado por Google Services si existe.
     *
     * @return web client id o null si aún no está disponible
     */
    private String obtenerWebClientId() {
        int resId = getResources().getIdentifier("default_web_client_id", "string", getPackageName());
        if (resId == 0) {
            return null;
        }
        return getString(resId);
    }
}
