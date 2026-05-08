# 📱 Documentación: Implementación de Material Design 3 en Serenum

## 📋 Resumen de Cambios

Se ha actualizado la pantalla de login de la aplicación Serenum implementando **Material Design 3** con estilo moderno, minimalista y pastel.

---

## 🎨 Cambios de Diseño

### 1. **Paleta de Colores Pastel**
Se ha creado un sistema de colores elegante y profesional:

```xml
<!-- Colores primarios - Azul pastel suave -->
<color name="primary">#8B9DC3</color>
<color name="primary_light">#B5C6E0</color>
<color name="primary_dark">#5F7A9B</color>

<!-- Colores secundarios - Coral pastel -->
<color name="secondary">#D4A5A5</color>
<color name="secondary_light">#E8C9C9</color>

<!-- Colores de texto -->
<color name="text_primary">#2C3E50</color>
<color name="text_secondary">#7F8C8D</color>

<!-- Fondo suave -->
<color name="background_soft">#F8F9FB</color>
```

**Ubicación:** `app/src/main/res/values/colors.xml`

---

### 2. **Layout XML Modernizado**

#### Cambios Principales:

| Aspecto | Antes | Después |
|--------|-------|---------|
| **Contenedor raíz** | `LinearLayout` | `ScrollView` + `LinearLayout` |
| **Fondo** | Blanco sólido | Color pastel suave |
| **Botón Email** | `Button` genérico | `MaterialButton` con esquinas redondeadas |
| **Botón Registro** | `TextView` | `MaterialButton` con estilo de texto |
| **Espaciado** | Estándar | Amplios y profesionales (32dp padding) |
| **Altura botones** | `wrap_content` | `56dp` (Material Design estándar) |
| **Esquinas botones** | Ninguna | `12dp` redondeadas |
| **Elevación** | Ninguna | `4dp` (sombra sutil) |

#### Nuevo contenido visual:

1. **ScrollView** - Permite desplazar si el contenido es largo
2. **Espaciadores estratégicos** - 60dp arriba, 30dp intermedio, 60dp abajo
3. **Subtítulo** - "Tu compañero de bienestar"
4. **Divisor visual** - Línea sutil entre botones
5. **Ripple effect** - Efecto de toque en botones

**Ubicación:** `app/src/main/res/layout/activity_main.xml`

---

### 3. **Componentes Material**

#### MaterialButton - Botón Email
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnLoginEmail"
    android:layout_width="match_parent"
    android:layout_height="56dp"
    android:text="Iniciar sesión con correo"
    app:cornerRadius="12dp"
    app:backgroundTint="@color/primary"
    android:textColor="@color/white"
    app:elevation="4dp" />
```

**Características:**
- ✅ Esquinas redondeadas (12dp)
- ✅ Color pastel azul como fondo
- ✅ Sombra sutil (elevación de 4dp)
- ✅ Altura estándar Material (56dp)
- ✅ Texto blanco legible

#### MaterialButton TextButton - Registro
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/txtGoRegister"
    style="@style/Widget.MaterialComponents.Button.TextButton"
    android:text="¿No tienes cuenta? Crear una nueva"
    app:rippleColor="@color/primary_light" />
```

**Características:**
- ✅ Estilo solo texto (sin fondo)
- ✅ Efecto ripple al tocar
- ✅ Color primario
- ✅ Sin elevación (flotante visual)

---

### 4. **Dependencias Añadidas**

#### `gradle/libs.versions.toml`
```toml
[versions]
material = "1.11.0"
materialComponents = "1.11.0"

[libraries]
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
material-components = { group = "com.google.android.material", name = "material", version.ref = "materialComponents" }
```

#### `app/build.gradle.kts`
```kotlin
dependencies {
    implementation(libs.material)
    // ... otras dependencias
}
```

---

### 5. **Permisos Necesarios**

Se añadió permiso de internet en `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

**Razón:** Necesario para autenticación con Google y correo.

---

## 📐 Especificaciones de Diseño

### Dimensiones
- **Padding del contenedor:** 32dp
- **Altura de botones:** 56dp (Material Design)
- **Esquinas redondeadas:** 12dp
- **Espaciado entre elementos:** 16dp-20dp
- **Tamaño título:** 48sp
- **Tamaño subtítulo:** 16sp
- **Tamaño botones:** 16sp

### Colores
- **Fondo principal:** #F8F9FB (pastel suave)
- **Botón primario:** #8B9DC3 (azul pastel)
- **Texto primario:** #2C3E50 (gris profundo)
- **Texto secundario:** #7F8C8D (gris claro)

### Efectos
- **Elevación:** 4dp (botones)
- **Ripple:** Animación Material al tocar
- **Transiciones:** Suaves (Android nativo)

---

## 🔄 Flujo de Ejecución

### En el Usuario:

1. **App inicia** → `MainActivity.onCreate()`
2. **Layout carga** → `setContentView(R.layout.activity_main)`
3. **Se aplican colores** desde `colors.xml`
4. **Se configuran listeners** en `setupLoginButtons()`
5. **Usuario toca un botón** → Se ejecuta lógica en listener

### En Android Studio:

1. **Sync Gradle** → Descarga dependencias Material
2. **Build** → Compila con Material Components
3. **Run** → Emulador/dispositivo muestra layout

---

## 💻 Cómo Compilar y Ejecutar

### Opción 1: Android Studio
```
1. File → Sync Now (o Gradle → Sync Projects)
2. Build → Make Project
3. Run → Run 'app' (o Shift+F10)
```

### Opción 2: Terminal
```bash
cd C:\Users\serhii.synyshyn\AndroidStudioProjects\Serenum
gradlew.bat clean build
gradlew.bat installDebug
```

---

## 📸 Estructura Final

```
activity_main.xml
├── ScrollView (contenedor con scroll)
│   └── LinearLayout (contenedor vertical)
│       ├── View (espaciador 60dp)
│       ├── TextView (Título "Serenum" 48sp)
│       ├── TextView (Subtítulo "Tu compañero..." 16sp)
│       ├── View (espaciador 30dp)
│       ├── SignInButton (Google)
│       ├── MaterialButton (Email - azul pastel)
│       ├── View (divisor 1dp)
│       ├── MaterialButton.TextButton (Registro)
│       └── View (espaciador 60dp)
```

---

## 🎯 Especificaciones Material Design 3

✅ **Adoptadas:**
- Button height: 56dp
- Corner radius: 12dp
- Material colors system
- Elevation & shadows
- Ripple effects
- Touch feedback

---

## 📝 Para tu TFG

### Sección sugerida en documentación:

> **"Implementación de Material Design 3"**
> 
> Se implementó Material Design 3 con el objetivo de proporcionar una interfaz moderna, intuitiva y accesible. La paleta de colores utiliza tonos pastel para una experiencia visual suave y profesional. Los botones emplean `MaterialButton` con altura estándar de 56dp y esquinas redondeadas de 12dp para mejorar la usabilidad. Se añadió una `ScrollView` para adaptarse a diferentes tamaños de pantalla, y se utilizaron espacios en blanco amplios (32dp de padding) siguiendo los principios de diseño minimalista.

---

## 🔗 Referencias

- [Material Design 3 Guidelines](https://m3.material.io/)
- [Android Material Components](https://material.io/develop/android)
- [Material Button Docs](https://material.io/develop/android/components/buttons)

---

**Versión:** 1.0  
**Fecha:** Mayo 2026  
**Desarrollador:** Serenum Team

