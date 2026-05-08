
# 📖 Guía Completa del Código - Material Design Login

## 📁 Archivo: `activity_main.xml`

### Estructura Completa Comentada

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Declaración XML estándar -->

<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    <!-- ScrollView: Contenedor que permite desplazar el contenido -->
    <!-- Útil para pantallas pequeñas donde el contenido completo no cabe -->
    
    xmlns:app="http://schemas.android.com/apk/res-auto"
    <!-- Namespace 'app': Para atributos Material especializados -->
    <!-- Ejemplo: app:cornerRadius, app:elevation -->
    
    xmlns:tools="http://schemas.android.com/tools"
    <!-- Namespace 'tools': Para ayuda en el preview del editor -->
    
    android:layout_width="match_parent"
    <!-- Ancho: Ocupa todo el ancho disponible de la pantalla -->
    
    android:layout_height="match_parent"
    <!-- Alto: Ocupa todo el alto disponible de la pantalla -->
    
    android:background="@color/background_soft">
    <!-- Fondo: Referencia a color pastel definido en colors.xml -->
    <!-- Color: #F8F9FB (gris azulado muy claro, minimiza fatiga ocular) -->

    <LinearLayout
        android:id="@+id/main"
        <!-- ID: Identificador único para referencias en Java -->
        <!-- Se usa con findViewById(R.id.main) en MainActivity -->
        
        android:layout_width="match_parent"
        <!-- Ancho: Igual al padre (ScrollView) -->
        
        android:layout_height="wrap_content"
        <!-- Alto: Ajustado al contenido dentro -->
        <!-- Permite que ScrollView haga scroll si es necesario -->
        
        android:orientation="vertical"
        <!-- Orientación: Los elementos se apilan verticalmente (arriba → abajo) -->
        
        android:padding="32dp"
        <!-- Padding: Espaciado interno de 32dp en todos los lados -->
        <!-- Genera márgenes internos respecto a los bordes de ScrollView -->
        
        android:gravity="center_horizontal"
        <!-- Gravedad: Centra los elementos horizontalmente -->
        <!-- Nota: center_horizontal usa solo eje X -->
        
        tools:context=".MainActivity">
        <!-- tools:context: Le dice al editor que este layout es para MainActivity -->
        <!-- Solo para preview, no afecta ejecución real -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 1: ESPACIADOR SUPERIOR (60dp) -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <View
            android:layout_width="match_parent"
            <!-- Ancho: Llena el ancho disponible -->
            
            android:layout_height="60dp" />
            <!-- Alto: 60dp de espacio vacío -->
            <!-- Efecto: Centra verticalmente el contenido -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 2: TÍTULO PRINCIPAL -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <TextView
            android:id="@+id/txtAppName"
            <!-- ID: Para acceder desde Java si necesitas cambiar el texto -->
            
            android:layout_width="wrap_content"
            <!-- Ancho: Solo el necesario para el texto -->
            
            android:layout_height="wrap_content"
            <!-- Alto: Solo el necesario para el texto -->
            
            android:text="Serenum"
            <!-- Texto a mostrar (puede extraerse a strings.xml para traduciones) -->
            
            android:textSize="48sp"
            <!-- Tamaño: 48 scale-independent pixels (se ajusta a densidad de pantalla) -->
            <!-- Grande para jerarquía visual clara -->
            
            android:textStyle="bold"
            <!-- Estilo: Texto en negrita para mayor peso visual -->
            
            android:textColor="@color/text_primary"
            <!-- Color: Hace referencia al color gris oscuro #2C3E50 -->
            <!-- Suficiente contraste con fondo pastel para accesibilidad -->
            
            android:layout_marginBottom="16dp" />
            <!-- Margen inferior: 16dp de espacio debajo del título -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 3: SUBTÍTULO DESCRIPTIVO -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <TextView
            android:id="@+id/txtSubtitle"
            <!-- ID: Útil si necesitas cambiar dinámicamente el subtítulo -->
            
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            
            android:text="Tu compañero de bienestar"
            <!-- Texto descriptivo de la app (tagline) -->
            
            android:textSize="16sp"
            <!-- Tamaño: 16sp (más pequeño que el título) -->
            
            android:textColor="@color/text_secondary"
            <!-- Color: Gris más claro #7F8C8D (jerarquía visual) -->
            
            android:layout_marginBottom="60dp"
            <!-- Margen: Espacio grande abajo (separa del botones) -->
            
            android:alpha="0.8" />
            <!-- Alpha: 80% de opacidad (20% transparencia) -->
            <!-- Efecto: Subtítulo "más suave" visualmente -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 4: ESPACIADOR INTERMEDIO -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <View
            android:layout_width="match_parent"
            android:layout_height="30dp" />
            <!-- Espacio de 30dp entre subtítulo y botones -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 5: BOTÓN GOOGLE SIGN IN -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <com.google.android.gms.common.SignInButton
            <!-- Componente especial de Google Play Services -->
            <!-- Proporciona un botón estilizado "Sign in with Google" -->
            
            android:id="@+id/btnGoogle"
            <!-- ID: Para acceder desde Java y configurar listeners -->
            
            android:layout_width="match_parent"
            <!-- Ancho: Ocupa todo el ancho disponible -->
            
            android:layout_height="56dp"
            <!-- Alto: 56dp (altura estándar Material para botones) -->
            
            android:layout_marginBottom="16dp"
            <!-- Margen: Espacio de 16dp debajo (separa del botón email) -->
            
            tools:ignore="MissingClass" />
            <!-- tools:ignore: Suprime advertencia en editor about SignInButton -->
            <!-- La clase solo existe en tiempo de ejecución si GMS está instalado -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 6: BOTÓN EMAIL - MATERIAL BUTTON -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <com.google.android.material.button.MaterialButton
            <!-- MaterialButton: Componente Material Design moderno -->
            <!-- Vs Button genérico: Más atributos, mejor UX, ripple effect -->
            
            android:id="@+id/btnLoginEmail"
            <!-- ID: Para listeners en MainActivity -->
            
            android:layout_width="match_parent"
            <!-- Ancho: Llena todo el ancho disponible -->
            
            android:layout_height="56dp"
            <!-- Alto: 56dp (estándar Material para targets táctiles) -->
            
            android:text="Iniciar sesión con correo"
            <!-- Texto a mostrar en el botón -->
            
            android:textSize="16sp"
            <!-- Tamaño: 16sp (legible, no demasiado grande/pequeño) -->
            
            app:cornerRadius="12dp"
            <!-- Esquinas redondeadas: 12dp (look moderno, no demasiado) -->
            <!-- Material recomienda 12dp como medio feliz -->
            
            app:backgroundTint="@color/primary"
            <!-- Tinta de fondo: Color azul pastel #8B9DC3 -->
            <!-- Tint es más flexible que backgroundColor directo -->
            
            android:textColor="@color/white"
            <!-- Color texto: Blanco para máximo contraste -->
            <!-- Contraste #8B9DC3 (azul) con blanco = excelete legibilidad -->
            
            android:layout_marginBottom="20dp"
            <!-- Margen: 20dp debajo (separa del divisor visual) -->
            
            app:elevation="4dp"
            <!-- Elevación: Sombra de 4dp -->
            <!-- Efecto: El botón "flota" sobre el fondo, parece clickable -->
            
            app:strokeWidth="0dp" />
            <!-- Ancho del stroke: 0dp (sin borde, solo relleno sólido) -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 7: DIVISOR VISUAL SUTIL -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <View
            android:layout_width="match_parent"
            <!-- Ancho: Llena el ancho disponible -->
            
            android:layout_height="1dp"
            <!-- Alto: 1dp (línea muy fina) -->
            
            android:background="@color/text_hint"
            <!-- Color: Gris claro #BDC3C7 (para hints/textos diminutos) -->
            
            android:alpha="0.3"
            <!-- Alpha: 30% opacidad (muy sutil, no abrumador) -->
            
            android:layout_marginBottom="20dp" />
            <!-- Margen: 20dp de espacio debajo -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 8: BOTÓN REGISTRO (TEXT BUTTON) -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <com.google.android.material.button.MaterialButton
            <!-- MaterialButton otra vez, pero con estilo diferente (text button) -->
            
            android:id="@+id/txtGoRegister"
            <!-- ID: Para listeners en MainActivity -->
            <!-- Nota: Llamado 'txtGoRegister' pero es MaterialButton, no TextView -->
            
            android:layout_width="wrap_content"
            <!-- Ancho: Solo el necesario para el texto -->
            
            android:layout_height="wrap_content"
            <!-- Alto: Solo el necesario para el texto -->
            
            android:text="¿No tienes cuenta? Crear una nueva"
            <!-- Texto: Enlace para ir a registro -->
            
            android:textSize="14sp"
            <!-- Tamaño: 14sp (más pequeño que botón principal) -->
            
            style="@style/Widget.MaterialComponents.Button.TextButton"
            <!-- Estilo: Predefinido de Material que hace que sea "text button" -->
            <!-- Efecto: Sin fondo visible, solo texto de color -->
            
            app:rippleColor="@color/primary_light"
            <!-- Color ripple: Azul pastel claro #B5C6E0 -->
            <!-- Cuando tocas el botón, expande esta onda de color -->
            
            android:textColor="@color/primary"
            <!-- Color texto: Azul pastel principal #8B9DC3 -->
            
            android:layout_marginTop="12dp" />
            <!-- Margen superior: 12dp (pequeño espacio con divisor) -->

        <!-- ═══════════════════════════════════════════════════ -->
        <!-- SECCIÓN 9: ESPACIADOR INFERIOR -->
        <!-- ═══════════════════════════════════════════════════ -->
        
        <View
            android:layout_width="match_parent"
            android:layout_height="60dp" />
            <!-- Espacio de 60dp en la parte inferior -->
            <!-- Efecto: Desplaza todo hacia arriba, centra visualmente -->

    </LinearLayout>

</ScrollView>
```

---

## 🎨 Archivo: `colors.xml`

### Explicación de Colores

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    
    <!-- ════════════════════════════════════════════ -->
    <!-- COLORES PRIMARIOS - Azul Pastel -->
    <!-- ════════════════════════════════════════════ -->
    
    <color name="primary">#8B9DC3</color>
    <!-- Uso: Botón email, links, acentos principales -->
    <!-- RGB: 139, 157, 195 (mitad saturación, elegante) -->
    <!-- Percepción: Confianza, profesionalidad, serenidad -->
    
    <color name="primary_light">#B5C6E0</color>
    <!-- Uso: Efectos hover, ripple colors, estados secondary -->
    <!-- RGB: 181, 198, 224 (más claro) -->
    <!-- Percepción: Suave, menos intimidante -->
    
    <color name="primary_dark">#5F7A9B</color>
    <!-- Uso: Estados pressed, elementos más oscuros -->
    <!-- RGB: 95, 122, 155 (más oscuro) -->
    <!-- Percepción: Seriedad, contraste -->

    <!-- ════════════════════════════════════════════ -->
    <!-- COLORES SECUNDARIOS - Coral Pastel -->
    <!-- ════════════════════════════════════════════ -->
    
    <color name="secondary">#D4A5A5</color>
    <!-- Uso: Acentos opuestos, errores suaves, alerts -->
    <!-- RGB: 212, 165, 165 (rojo apagado) -->
    
    <color name="secondary_light">#E8C9C9</color>
    <!-- Uso: Fondos suaves para secundario -->
    <!-- RGB: 232, 201, 201 (muy claro) -->
    
    <color name="secondary_dark">#B87E7E</color>
    <!-- Uso: Estados dark para secundario -->
    <!-- RGB: 184, 126, 126 -->

    <!-- ════════════════════════════════════════════ -->
    <!-- COLORES TERCIARIOS - Verde Pastel -->
    <!-- ════════════════════════════════════════════ -->
    
    <color name="tertiary">#A8C68F</color>
    <!-- Uso: Éxito, confirmaciones, estados positivos -->
    <!-- RGB: 168, 198, 143 (verde suave) -->
    
    <color name="tertiary_light">#C4DEB3</color>
    <!-- Uso: Fondos en colores terciarios -->
    
    <color name="tertiary_dark">#7FA967</color>
    <!-- Uso: States dark terciario -->

    <!-- ════════════════════════════════════════════ -->
    <!-- COLORES DE FONDO -->
    <!-- ════════════════════════════════════════════ -->
    
    <color name="background_soft">#F8F9FB</color>
    <!-- Uso: Fondo principal de layouts -->
    <!-- RGB: 248, 249, 251 (gris azulado muy claro) -->
    <!-- Propósito: Minimizar fatiga ocular, elegante -->
    
    <color name="background_white">#FFFFFF</color>
    <!-- Uso: Superficies, tarjetas, contenedores -->
    <!-- RGB: 255, 255, 255 (blanco puro) -->
    
    <color name="surface_light">#F0F2F7</color>
    <!-- Uso: Superficies secundarias, containers -->
    <!-- RGB: 240, 242, 247 (gris azulado claro) -->

    <!-- ════════════════════════════════════════════ -->
    <!-- COLORES DE TEXTO -->
    <!-- ════════════════════════════════════════════ -->
    
    <color name="text_primary">#2C3E50</color>
    <!-- Uso: Títulos, textos principales -->
    <!-- RGB: 44, 62, 80 (gris muy oscuro) -->
    <!-- Contraste con background_soft: Excelente (WCAG AAA) -->
    
    <color name="text_secondary">#7F8C8D</color>
    <!-- Uso: Subtítulos, textos secundarios -->
    <!-- RGB: 127, 140, 141 (gris medio) -->
    <!-- Jerarquía: Menos importante que primary -->
    
    <color name="text_hint">#BDC3C7</color>
    <!-- Uso: Hints, placeholders, divisores sutiles -->
    <!-- RGB: 189, 195, 199 (gris claro) -->
    <!-- Propósito: Barely visible, suggests rather than dominates -->

    <!-- ════════════════════════════════════════════ -->
    <!-- COLORES ESPECIALES / SEMÁNTICOS -->
    <!-- ════════════════════════════════════════════ -->
    
    <color name="google_blue">#4A90E2</color>
    <!-- Uso: Google Sign In Button (color oficial de Google) -->
    <!-- RGB: 74, 144, 226 (azul vivo, más saturado) -->
    
    <color name="success">#A8C68F</color>
    <!-- Uso: Estados de éxito, confirmaciones -->
    <!-- RGB: Verde pastel (igual a tertiary) -->
    
    <color name="error">#D4A5A5</color>
    <!-- Uso: Errores, alertas, estados negativos -->
    <!-- RGB: Coral pastel (igual a secondary) -->
    
    <color name="warning">#E9C589</color>
    <!-- Uso: Advertencias, estados caution -->
    <!-- RGB: 233, 197, 137 (amarillo pastel) -->

    <!-- ════════════════════════════════════════════ -->
    <!-- COLORES DE COMPATIBILIDAD -->
    <!-- ════════════════════════════════════════════ -->
    
    <color name="black">#000000</color>
    <!-- Android puede pedir este color genérico -->
    
    <color name="white">#FFFFFF</color>
    <!-- Android puede pedir este color genérico -->

</resources>
```

---

## 💻 Archivo: `MainActivity.java`

```java
package com.example.serenum;

import android.os.Bundle;
// Bundle: Contenedor para pasar datos entre activities

import android.widget.Button;
// Button: Importamos (aunque ahora usamos MaterialButton)

import android.widget.TextView;
// TextView: Para textos que pueden ser interactivos

import android.widget.Toast;
// Toast: Muestra notificaciones flotantes temporales

import androidx.activity.EdgeToEdge;
// EdgeToEdge: Permite que el contenido llegue hasta los bordes (include notch)

import androidx.appcompat.app.AppCompatActivity;
// AppCompatActivity: Clase base para activities con soporte backwards compatible

import androidx.core.graphics.Insets;
// Insets: Maneja el espacio de system bars (status bar, navigation bar)

import androidx.core.view.ViewCompat;
// ViewCompat: Proporciona métodos de View compatibles con versiones antiguas

import androidx.core.view.WindowInsetsCompat;
// WindowInsetsCompat: Maneja insets de forma compatible

public class MainActivity extends AppCompatActivity {
    // MainActivity: Primera pantalla que ve el usuario
    // La declara AndroidManifest.xml con <action android:name="android.intent.action.MAIN" />

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // onCreate: Llamado cuando la activity se crea
        // savedInstanceState: Datos guardados si la activity fue destruida/recreada
        
        super.onCreate(savedInstanceState);
        // Llamar al constructor padre
        
        EdgeToEdge.enable(this);
        // Activar modo edge-to-edge
        // Permite que el contenido se extienda bajo la status bar y navigation bar
        // Necesita ViewCompat.setOnApplyWindowInsetsListener para manejar insets
        
        setContentView(R.layout.activity_main);
        // Cargar el layout XML
        // R.layout.activity_main hace referencia al archivo activity_main.xml
        // Android lo infla (convierte XML a objetos Java)

        // ───────────────────────────────────────────
        // MANEJO DE SYSTEM INSETS (status bar, notch, etc)
        // ───────────────────────────────────────────
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // ViewCompat.setOnApplyWindowInsetsListener:
            // Registra un listener que se ejecuta cuando cambian los window insets
            // Params:
            //   - View: El view donde aplicar (R.id.main = LinearLayout root)
            //   - Listener: Lambda que recibe (view, insets)
            
            findViewById(R.id.main)
            // Obtener referencias al view raíz (LinearLayout con id "main")
            
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Obtener los insets relativos a system bars
            // systemBars incluye: status bar + navigation bar
            // Retorna objeto Insets con: left, top, right, bottom
            // Valores en píxeles que representan el espacio ocupado por el sistema
            
            v.setPadding(
                systemBars.left,    // Padding izquierdo (por notch u otro)
                systemBars.top,     // Padding superior (por status bar)
                systemBars.right,   // Padding derecho (por notch u otro)
                systemBars.bottom   // Padding inferior (por navigation bar)
            );
            // Asignar padding al view para que el contenido no se oculte
            
            return insets;
            // Retornar los insets (puede procesarlos el siguiente listener en la cadena)
        });

        // ───────────────────────────────────────────
        // CONFIGURACIÓN DE BOTONES
        // ───────────────────────────────────────────
        
        setupLoginButtons();
        // Llamar método que configura listeners de botones
        // Separamos en otro método por claridad (separación de responsabilidades)
    }

    /**
     * Método que configura los listeners (escuchadores) para los botones del login
     * Separado del onCreate para mayor legibilidad y mantenibilidad
     */
    private void setupLoginButtons() {
        
        // ───────────────────────────────────────────
        // OBTENER REFERENCIAS A LOS BOTONES
        // ───────────────────────────────────────────
        
        Button btnLoginEmail = findViewById(R.id.btnLoginEmail);
        // findViewById busca en el layout inflado el elemento con id "btnLoginEmail"
        // Retorna una referencia al botón para manipularlo desde Java
        
        TextView txtGoRegister = findViewById(R.id.txtGoRegister);
        // Buscar el elemento con id "txtGoRegister" (que es un MaterialButton)
        // Lo usamos como TextView porque MaterialButton es subclase de TextView

        // ───────────────────────────────────────────
        // LISTENER PARA BOTÓN EMAIL
        // ───────────────────────────────────────────
        
        btnLoginEmail.setOnClickListener(v -> {
            // setOnClickListener: Registrar un listener que se ejecuta cuando tocas el botón
            // Lambda (v -> { ... }): v es la View tocada
            
            Toast.makeText(
                MainActivity.this,
                // Context: 'this' hace referencia a MainActivity
                // MainActivity.this es explícito (importante dentro de lambdas)
                
                "Login con correo - pendiente de implementar",
                // Texto: Mensaje a mostrar
                
                Toast.LENGTH_SHORT
                // Duración: SHORT (~ 2 segundos)
            ).show();
            // show(): Mostrar el Toast en pantalla
            
            // TODO: Aquí va la lógica real
            // - Abrir nueva activity para login con email
            // - Validar credenciales
            // - Conectar con servidor backend
        });

        // ───────────────────────────────────────────
        // LISTENER PARA LINK REGISTRO
        // ───────────────────────────────────────────
        
        txtGoRegister.setOnClickListener(v -> {
            // Mismo patrón: listener de click
            
            Toast.makeText(
                MainActivity.this,
                "Ir a registro - pendiente de implementar",
                Toast.LENGTH_SHORT
            ).show();
            
            // TODO: Abrir la activity de registro
            // Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            // startActivity(intent);
        });
    }
    // Fin de setupLoginButtons()

}
// Fin de MainActivity
```

---

## 📝 Archivo: `AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Declaración XML estándar -->

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    <!-- Declarar que es un manifest de Android -->
    
    xmlns:tools="http://schemas.android.com/tools">
    <!-- Namespace tools para utilidades de desarrollo -->

    <!-- ═══════════════════════════════════════════ -->
    <!-- SECCIÓN: PERMISOS DE APLICACIÓN -->
    <!-- ═══════════════════════════════════════════ -->

    <uses-permission android:name="android.permission.INTERNET" />
    <!-- Permiso: ACCEDER A INTERNET -->
    <!-- Necesario para: -->
    <!--   1. Google Sign In (conectar con servidores de Google) -->
    <!--   2. Autenticación con Email (conectar con servidor backend) -->
    <!--   3. Sincronización de datos -->
    <!-- Aparición: En la lista de permisos cuando instalas la app desde Play Store -->

    <!-- ═══════════════════════════════════════════ -->
    <!-- SECCIÓN: CONFIGURACIÓN DE APLICACIÓN -->
    <!-- ═══════════════════════════════════════════ -->

    <application
        <!-- Tag: Define la aplicación y sus componentes -->
        
        android:allowBackup="true"
        <!-- allowBackup: Permite que Google respalde datos de la app -->
        <!-- true: Sí respaldar en Google Drive del usuario -->
        
        android:dataExtractionRules="@xml/data_extraction_rules"
        <!-- dataExtractionRules: Reglas qué datos se pueden extraer/respaldar -->
        <!-- Referencia archivo XML en res/xml/data_extraction_rules.xml -->
        
        android:fullBackupContent="@xml/backup_rules"
        <!-- fullBackupContent: Qué archivos respaldar completos -->
        
        android:icon="@mipmap/ic_launcher"
        <!-- icon: Icono de la app que ve el usuario en home -->
        <!-- @mipmap asegura que se escala según densidad de pantalla -->
        
        android:label="@string/app_name"
        <!-- label: Nombre de la app visible al usuario -->
        <!-- @string/app_name: Referencia a strings.xml (permite traducción) -->
        
        android:roundIcon="@mipmap/ic_launcher_round"
        <!-- roundIcon: Icono redondeado (algunos launchers lo usan) -->
        
        android:supportsRtl="true"
        <!-- supportsRtl: Soportar idiomas de derecha-a-izquierda (árabe, hebreo) -->
        <!-- true: Voltear layouts automáticamente si es necesario -->
        
        android:theme="@style/Theme.Serenum">
        <!-- theme: Tema visual global (colores, tipografía, estilos) -->
        <!-- Se aplica a todas las activities a menos que overrides localmente -->

        <!-- ═══════════════════════════════════════════ -->
        <!-- SECCIÓN: ACTIVITIES -->
        <!-- ═══════════════════════════════════════════ -->

        <activity
            android:name=".MainActivity"
            <!-- name: Nombre calificado de la activity -->
            <!-- ".MainActivity" =  com.example.serenum.MainActivity -->
            
            android:exported="true">
            <!-- exported: ¿Puede otro app iniciar esta activity? -->
            <!-- true: Sí (necesario para activity de inicio) -->

            <intent-filter>
            <!-- intent-filter: Define qué intents esta activity puede manejar -->
            <!-- Necesario: especificar cuál es la activity de inicio -->
            
                <action android:name="android.intent.action.MAIN" />
                <!-- action MAIN: Esta es la activity de inicio -->
                <!-- Android abre esta cuando tocas el icono de la app -->
                
                <category android:name="android.intent.category.LAUNCHER" />
                <!-- category LAUNCHER: Mostrar en launcher (home) -->
            
            </intent-filter>

        </activity>
        <!-- Fin de MainActivity -->

    </application>
    <!-- Fin de application -->

</manifest>
<!-- Fin de manifest -->
```

---

## 📊 Resumen: Flujo de Ejecución

```
1. USUARIO TOCA ICONO DE APP
   ↓
2. Android lee AndroidManifest.xml
   ├─ Busca activity con MAIN + LAUNCHER
   ├─ Encuentra: MainActivity
   └─ Ejecuta: MainActivity.onCreate()
   ↓
3. MainActivity.onCreate()
   ├─ savedInstanceState = null (primera vez)
   ├─ EdgeToEdge.enable(this)
   ├─ setContentView(R.layout.activity_main)
   │  ├─ Lee activity_main.xml
   │  ├─ Infla (convierte) XML a objetos Java
   │  ├─ Asigna colores desde colors.xml
   │  └─ Renderiza en pantalla
   ├─ ViewCompat.setOnApplyWindowInsetsListener (maneja status bar)
   └─ setupLoginButtons()
   ↓
4. setupLoginButtons()
   ├─ findViewById(R.id.btnLoginEmail) → Obtiene referencia al botón
   ├─ findViewById(R.id.txtGoRegister) → Obtiene referencia al link
   ├─ btnLoginEmail.setOnClickListener(...) → Registra escuchador
   └─ txtGoRegister.setOnClickListener(...) → Registra escuchador
   ↓
5. USUARIO VE LA PANTALLA DE LOGIN LISTA
   ├─ Título "Serenum"
   ├─ Subtítulo "Tu compañero de bienestar"
   ├─ Botón Google
   ├─ Botón Email (Material Design)
   └─ Link Registro (MaterialButton TextButton)
   ↓
6. USUARIO TOCA BOTÓN EMAIL
   ├─ setOnClickListener() se ejecuta
   ├─ Toast.makeText(...).show()
   └─ Usuario ve: "Login con correo - pendiente de implementar"
```

---

## 🎓 Conceptos Clave

### Binding de Vistas
```java
// OLD (sin binding):
Button btn = findViewById(R.id.btnEmail);
btn.setText("Hola");

// NEW (mejor):
// Usar View Binding en build.gradle:
// buildFeatures { viewBinding true }
// Luego: binding.btnEmail.setText("Hola");
```

### lambdas vs Anonymous Classes
```java
// Antiguo (Java 7 en adelante):
btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(MainActivity.this, "Hola", Toast.LENGTH_SHORT).show();
    }
});

// Moderno (Java 8+, recomendado en Android):
btn.setOnClickListener(v -> {
    Toast.makeText(MainActivity.this, "Hola", Toast.LENGTH_SHORT).show();
});
```

### Referencia de Context
```java
// Dentro de lambda dentro de MainActivity:
Toast.makeText(MainActivity.this, "Text", Toast.LENGTH_SHORT).show();
// MainActivity.this = explícito (en Lambda, 'this' es ambiguo)

// En método normal de MainActivity:
Toast.makeText(this, "Text", Toast.LENGTH_SHORT).show();
// this = MainActivity directamente
```

---

¡Documento completo para tu TFG! 📚

