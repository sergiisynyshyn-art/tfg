# 🔧 Guía Rápida: Agregar Font Quattrocento (Paso a Paso Exacto)

## ⚠️ Problema Actual
```
Error: resource font/quattrocento_bold not found
Error: resource font/quattrocento_regular not found
```

**Causa:** Los archivos .ttf no están en la ubicación correcta.

---

## ✅ Solución: 4 Pasos Simples

### PASO 1: Verificar que tienes los archivos

Después de descargar y descomprimir el ZIP de Google Fonts, deberías tener:

```
Tu carpeta de descargas:
├── Quattrocento-Bold.ttf
├── Quattrocento-Regular.ttf
└── (otros archivos de fuente)
```

**✓ Verifica que existan estos dos archivos**

---

### PASO 2: Abre Android Studio File Explorer

En Android Studio:

1. Click en **View** (menú superior)
2. Click en **Tool Windows** 
3. Click en **Project** (o presiona `Alt+1`)

Deberías ver el árbol de carpetas a la izquierda:

```
Serenum (nombre del proyecto)
└── app
    └── src
        └── main
            ├── AndroidManifest.xml
            ├── java/
            ├── res/
            │   ├── drawable/
            │   ├── layout/
            │   ├── mipmap/
            │   ├── values/
            │   └── font/  ← AQUÍ DEBE ESTAR
            │       └── (vacío o con archivos)
```

---

### PASO 3: Crear carpeta "font" (Si NO existe)

Si ves que **NO existe** la carpeta `font`:

1. Click DERECHO en la carpeta `res/`
2. Selecciona **New** → **Directory**
3. Escribe: `font`
4. Presiona ENTER

Ahora deberías ver:
```
res/
├── drawable/
├── font/        ← NUEVA CARPETA
├── layout/
├── mipmap/
└── values/
```

---

### PASO 4: Copiar archivos .ttf a la carpeta font

**Opción A: Desde el File Explorer del SO (Windows)**

1. Abre el Explorador de Archivos de Windows
2. Navega a tu carpeta de descargas
3. Encuentra: `Quattrocento-Bold.ttf` y `Quattrocento-Regular.ttf`
4. **COPIA ambos archivos**
5. Navega a:
   ```
   C:\Users\serhii.synyshyn\AndroidStudioProjects\Serenum\app\src\main\res\font\
   ```
6. **PEGA los archivos aquí**

---

**Opción B: Desde Android Studio (Drag & Drop)**

1. En Android Studio, haz click derecho en la carpeta `font/`
2. Click en **Show in Explorer** (o similar)
3. Se abre el Explorador de Windows
4. Copia los archivos .ttf desde descargas
5. Pégalos aquí

---

### PASO 5: Renombrar (MUY IMPORTANTE)

Android requiere que los nombres de font sean **minúsculas sin espacios**.

**ANTES:**
```
Quattrocento-Bold.ttf
Quattrocento-Regular.ttf
```

**DESPUÉS:**
```
quattrocento_bold.ttf
quattrocento_regular.ttf
```

**Cómo renombrar en Windows:**

1. Click DERECHO en `Quattrocento-Bold.ttf`
2. Selecciona **Rename**
3. Cambia a: `quattrocento_bold.ttf`
4. Presiona ENTER
5. Repite para `Quattrocento-Regular.ttf` → `quattrocento_regular.ttf`

---

### PASO 6: Verificar en Android Studio

La carpeta `font/` debería verse así:

```
font/
├── quattrocento_bold.ttf
└── quattrocento_regular.ttf
```

---

## 🔄 Reactivar los Fonts en el XML

Una vez que los archivos estén en el lugar correcto:

1. Abre `app/src/main/res/layout/activity_main.xml`
2. Encuentra los dos `<TextView>` (líneas ~24 y ~35)
3. Cambia:

```xml
<!-- DE: -->
android:fontFamily="serif"

<!-- A: -->
android:fontFamily="@font/quattrocento_bold"  <!-- Para el título -->
android:fontFamily="@font/quattrocento_regular"  <!-- Para el subtítulo -->
```

---

## ✅ Chequeo Final

Después de todo, debería verse así:

```xml
<!-- activity_main.xml -->
<TextView
    android:id="@+id/txtAppName"
    android:text="Serenum"
    android:fontFamily="@font/quattrocento_bold"  ✅
    .../>

<TextView
    android:id="@+id/txtSubtitle"
    android:text="Tu compañero de bienestar"
    android:fontFamily="@font/quattrocento_regular"  ✅
    .../>
```

---

## 🚀 Compilar y Ejecutar

1. **Android Studio**: `File` → `Sync Now` (o Ctrl+Shift+L)
2. **Build**: `Build` → `Make Project` (Ctrl+F9)
3. **Run**: `Run` → `Run 'app'` (Shift+F10)

✅ **No debería haber errores ahora**

---

## 📁 Estructura Final Correcta

```
C:\Users\serhii.synyshyn\AndroidStudioProjects\Serenum\
└── app/
    └── src/
        └── main/
            ├── AndroidManifest.xml
            └── res/
                ├── drawable/
                ├── font/
                │   ├── quattrocento_bold.ttf      ✅
                │   └── quattrocento_regular.ttf   ✅
                ├── layout/
                │   └── activity_main.xml
                ├── mipmap/
                ├── values/
                │   ├── colors.xml
                │   ├── strings.xml
                │   └── themes.xml
```

---

## ❓ Problemas Comunes (y Soluciones)

### Problema: "Build failed - Resource not found"
**Solución:**
- Verifica que los archivos estén en: `app/src/main/res/font/`
- Verifica los nombres (minúsculas): `quattrocento_bold.ttf`
- Click derecho en carpeta `font/` → **Invalidate and Refresh**
- Restart Android Studio

### Problema: No veo la carpeta `font/` en Android Studio
**Solución:**
- Abre el Explorador: `app/src/main/res/` → Click derecho → **Show in Explorer**
- Crea la carpeta manualmente si es necesario
- O en Android Studio: `app/src/main/res/` → Click derecho → **New** → **Directory** → `font`

### Problema: El font se ve diferente de lo esperado
**Solución:**
- Es normal - puede variar según el dispositivo/emulador
- Verifica que sea serif (no sans-serif)
- Aumenta/disminuye el `android:textSize` si es necesario

---

## 📝 Versión Alternativa (Si los fonts no funcionan)

Si después de todo sigue sin funcionar, usa fonts del sistema:

```xml
<!-- Opción 1: Serif (similar a Quattrocento) -->
android:fontFamily="serif"

<!-- Opción 2: Sans-serif (limpio y moderno) -->
android:fontFamily="sans-serif"

<!-- Opción 3: Monospace (código) -->
android:fontFamily="monospace"
```

Para tu proyecto:
```xml
<TextView
    android:fontFamily="serif"  <!-- Parecido a Quattrocento -->
    .../>
```

---

## ✨ Resultado Final

Cuando todo esté correcto, verás:

```
┌──────────────────────────────┐
│  Fondo pastel (#F8F9FB)      │
│                              │
│  Serenum                    │ ← Quattrocento Bold 48sp
│  (serif elegante)           │
│                              │
│  Tu compañero de bienestar  │ ← Quattrocento Regular 16sp
│  (más suave)                │
│                              │
│  [Google]  [Email]          │
│  [¿No tienes cuenta?...]    │
│                              │
└──────────────────────────────┘
```

---

💡 **CONSEJO:** Si aún tienes dudas, puedes dejarlos como `serif` por ahora y cambiar después cuando tengas más experiencia con Android.

---

**Contacto para resolver:** Si sigue sin funcionar, adjunta una captura del error exacto.

