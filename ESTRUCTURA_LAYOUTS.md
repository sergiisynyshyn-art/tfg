# 📱 ESTRUCTURA DE LAYOUTS Y ACTIVITIES - Serenum

## 🎯 Mapeo Completo

```
APLICACIÓN SERENUM
│
├─ LOGIN FLOW (Pantalla Inicial)
│  │
│  ├─ 📄 activity_main.xml
│  │  ├─ Imagen de fondo (mount.png)
│  │  ├─ Título: "Serenum" (Font Quattrocento Bold)
│  │  ├─ Subtítulo: "Tu compañero de bienestar"
│  │  │
│  │  └─ Botones Principales:
│  │     ├─ 🔘 btnGoogle (ID: btnGoogle)
│  │     │  └─ Listener: Google Sign-In → Facebook/Google OAuth
│  │     │
│  │     ├─ 🔘 btnLoginEmail (ID: btnLoginEmail)
│  │     │  └─ Listener: → LoginEmailActivity
│  │     │
│  │     └─ 🔘 txtGoRegister (ID: txtGoRegister)
│  │        └─ Listener: → RegisterActivity
│  │
│  └─ 🔧 LoginActivity.java (LAUNCHER - punto de entrada)
│     ├─ setContentView(R.layout.activity_main) ✓ CORRECTO
│     ├─ Carga: activity_main.xml
│     ├─ Configura: Google Sign-In, Firebase Auth
│     ├─ Lógica: Procesa login y redirige a WelcomeActivity
│     └─ onStart():
│        └─ Si usuario autenticado → Redirige a MainActivity
│
├─ REGISTRO (Nuevo Usuario)
│  │
│  ├─ 📄 activity_register.xml
│  │  └─ Formulario de registro
│  │
│  └─ 🔧 RegisterActivity.java
│
├─ LOGIN POR EMAIL (Alternativa)
│  │
│  ├─ 📄 activity_login_email.xml
│  │  └─ Formulario de correo/contraseña
│  │
│  └─ 🔧 LoginEmailActivity.java
│
├─ BIENVENIDA (Post-Autenticación)
│  │
│  ├─ 📄 activity_welcome.xml
│  │  └─ Pantalla informativa
│  │
│  └─ 🔧 WelcomeActivity.java
│
└─ HOME PRINCIPAL ⭐ (Pantalla de Usuario Autenticado)
   │
   ├─ 📄 activity_home.xml
   │  ├─ Título: "Serenum" (28sp bold)
   │  │
   │  └─ Botones Principales:
   │     ├─ 🔘 btnPerfil (ID: btnPerfil)
   │     │  └─ Estado: Toast "Abrir perfil (pendiente)"
   │     │
   │     ├─ 🔘 btnSettings (ID: btnSettings)
   │     │  └─ Estado: Toast "Abrir ajustes (pendiente)"
   │     │
   │     └─ 🔘 btnLogout (ID: btnLogout)
   │        └─ Listener: cerrarSesion() → LoginActivity
   │
   └─ 🔧 MainActivity.java ✅ CORREGIDA
      ├─ setContentView(R.layout.activity_home) ✓ CORRECTO
      ├─ Carga: activity_home.xml
      ├─ Configura: Google Sign-In (solo para logout)
      ├─ Lógica: Pantalla principal después de login
      ├─ setupButtons():
      │  ├─ ✅ Busca btnPerfil, btnSettings, btnLogout
      │  ├─ ✅ Valida que existan (null checks)
      │  ├─ ✅ Muestra error si algo falta
      │  └─ ✅ Asigna listeners
      └─ onStart():
         ├─ Verifica: GoogleSignInAccount
         ├─ Si null → Redirige a LoginActivity
         └─ Si existe → Muestra MainActivity normal
```

---

## 🔄 FLUJO DE NAVEGACIÓN

```
START
 │
 ▼
┌──────────────────────┐
│  LoginActivity       │
│ (activity_main.xml)  │  ← LAUNCHER en AndroidManifest.xml
└──────────────────────┘
 │        │        │
 │        │        └──── [Botón Registrarse]
 │        │              ▼
 │        │         ┌──────────────────┐
 │        │         │ RegisterActivity │
 │        │         └──────────────────┘
 │        │
 │        └──── [Botón Email]
 │              ▼
 │         ┌──────────────────────────┐
 │         │ LoginEmailActivity       │
 │         └──────────────────────────┘
 │
 └──── [Botón Google]
       ▼
    ✅ Autenticación exitosa
       ▼
  ┌──────────────────────┐
  │ WelcomeActivity      │
  │ (Pantalla opcional)  │
  └──────────────────────┘
       ▼
  ┌──────────────────────┐
  │ MainActivity         │  ← PANTALLA PRINCIPAL
  │ (activity_home.xml)  │  ← Usuario autenticado
  └──────────────────────┘
       │        │         │
       │        │         └──── [btnLogout] ──┐
       │        │                               │
       │        └──── [btnSettings]             │
       │                                        │
       └──── [btnPerfil]                        │
                                                ▼
                                         ┌──────────────────┐
                                         │ LoginActivity    │
                                         │ (Sesión cerrada) │
                                         └──────────────────┘
```

---

## 📋 TABLA COMPARATIVA: activity_main.xml vs activity_home.xml

| Aspecto | activity_main.xml | activity_home.xml |
|---------|-------------------|-------------------|
| **Propósito** | Pantalla de LOGIN | Pantalla de HOME |
| **Usado por** | LoginActivity | MainActivity |
| **Layout tipo** | FrameLayout + ScrollView | LinearLayout |
| **Fondo** | Imagen + Overlay | Sin fondo |
| **Botones** | Google, Email, Register | Perfil, Settings, Logout |
| **Estado** | Usuario NO autenticado | Usuario autenticado |
| **Font** | Quattrocento (Bold/Regular) | Sistema |

---

## ✅ VERIFICACIÓN DE CONEXIONES

### MainActivity ← → activity_home.xml

```xml
<!-- activity_home.xml línea 11 -->
<LinearLayout
    ...
    tools:context=".MainActivity">  ✓ Correcto
```

```java
// MainActivity.java línea 35
setContentView(R.layout.activity_home);  ✓ Correcto
```

```xml
<!-- activity_home.xml líneas 22-27 -->
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnPerfil"
    android:text="Perfil" />

<!-- activity_home.xml líneas 29-34 -->
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnSettings"
    android:text="Ajustes" />

<!-- activity_home.xml líneas 36-41 -->
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnLogout"
    android:text="Cerrar sesión" />
```

```java
// MainActivity.java líneas 71-73
MaterialButton btnPerfil = findViewById(R.id.btnPerfil);      ✓
MaterialButton btnSettings = findViewById(R.id.btnSettings);  ✓
MaterialButton btnLogout = findViewById(R.id.btnLogout);      ✓
```

---

## 🔍 LOGS DE EJECUCIÓN (Logcat)

### Cuando abre LoginActivity:
```
D/LoginActivity: onCreate: iniciando
D/LoginActivity: onCreate: layout establecido
D/LoginActivity: onCreate: window insets configurado
D/LoginActivity: onCreate: DatabaseHelper inicializado
D/LoginActivity: onCreate: Firebase inicializado
D/LoginActivity: onCreate: Google Sign-In configurado
D/LoginActivity: onCreate: Botones configurados
D/LoginActivity: onCreate: completado exitosamente
```

### Cuando hace login y abre MainActivity:
```
D/MainActivity: onCreate: activity_home.xml cargado
D/MainActivity: setupButtons: Todos los botones encontrados correctamente
D/MainActivity: onStart: Verificando autenticación
D/MainActivity: onStart: Usuario autenticado: [Nombre Usuario]
```

### Si hay error de layout:
```
E/MainActivity: ERROR: Uno o más botones no se encuentran en activity_home.xml
```

---

## 🚀 COMPILACIÓN Y EJECUCIÓN

```bash
# Compilar proyecto
gradlew build

# Instalar en dispositivo/emulador
gradlew installDebug

# O desde Android Studio:
# Run → Run 'app'
```

---

## 🎨 RECURSOS UTILIZADOS

### Drawables:
- `mount.png` - Imagen de fondo en activity_main.xml

### Fonts:
- `quattrocentobold.ttf` - Título "Serenum"
- `quattrocentoregular.ttf` - Subtítulo

### Colors:
- `@color/primary` - Color principal
- `@color/secondary` - Color secundario
- `@color/white` - Blanco
- `@color/text_hint` - Gris hints

---

## ✨ ESTADO ACTUAL: ✅ CORRECTO

Todas las conexiones entre Activities y Layouts están verificadas y funcionales.


