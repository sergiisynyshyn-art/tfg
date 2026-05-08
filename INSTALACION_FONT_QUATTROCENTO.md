# 📝 Cómo Instalar Font Quattrocento en Serenum

## 🎯 Objetivo
Agregar el font 'Quattrocento' al proyecto para usar en los títulos y subtítulos de la pantalla de login.

---

## 📥 Opción 1: Descargar de Google Fonts (Recomendado)

### Paso 1: Descargar el Font
1. Ir a: **https://fonts.google.com/specimen/Quattrocento**
2. Click en "Download family" (botón rojo arriba a la derecha)
3. Se descargará un ZIP llamado `Quattrocento.zip`

### Paso 2: Extraer los archivos
1. Descomprime el ZIP
2. Dentro encontrarás:
   - `Quattrocento-Bold.ttf`
   - `Quattrocento-Regular.ttf`

### Paso 3: Crear carpeta res/font
1. En Android Studio, navega a:
   ```
   app/src/main/res/
   ```
2. Click derecho → **New** → **Directory**
3. Nombre: **`font`**
4. Click OK

### Paso 4: Copiar archivos .ttf
1. Copia los archivos descargados:
   - `Quattrocento-Bold.ttf`
   - `Quattrocento-Regular.ttf`

2. Pégalos en la carpeta que creaste:
   ```
   app/src/main/res/font/
   ```

3. Android Studio te pedirá renombar (minúsculas, sin espacios):
   - `Quattrocento-Bold.ttf` → `quattrocento_bold.ttf`
   - `Quattrocento-Regular.ttf` → `quattrocento_regular.ttf`

---

## 📁 Estructura Final

```
app/src/main/res/
└── font/
    ├── quattrocento_bold.ttf
    └── quattrocento_regular.ttf
```

---

## ✅ Verificación

### En el XML ya está configurado:
```xml
<!-- Título - usa font bold -->
<TextView
    ...
    android:fontFamily="@font/quattrocento_bold"
    android:text="Serenum" />

<!-- Subtítulo - usa font regular -->
<TextView
    ...
    android:fontFamily="@font/quattrocento_regular"
    android:text="Tu compañero de bienestar" />
```

---

## 🚀 Pasos Finales

1. **Sync Gradle:**
   - `File` → `Sync Now`

2. **Compila el proyecto:**
   - `Build` → `Make Project`

3. **Ejecuta:**
   - `Run` → `Run 'app'`

---

## 🎨 Resultado

Verás:
- ✅ Serenum en font **Quattrocento Bold** (49sp, elegante, serif)
- ✅ "Tu compañero de bienestar" en **Quattrocento Regular** (16sp, ligero)
- ✅ Ambos en gris oscuro (#2C3E50)
- ✅ Fondo pastel (#F8F9FB)

---

## 📝 Alternativa: Si no puedes descargar

Si Google Fonts no está disponible, puedes usar una fuente similar:
1. Buscar "Quattrocento TTF" en otros repositories
2. O usar fuentes del sistema: `serif`, `sans-serif`, `monospace`

**Para cambiar en el XML:**
```xml
<!-- Opción 1: Sin font personalizado (serif genérico) -->
android:fontFamily="serif"

<!-- Opción 2: Noto Serif (si la tienes disponible) -->
android:fontFamily="@font/noto_serif"
```

---

## ❓ Problemas Comunes

### "Resource not found: @font/quattrocento_bold"
- **Solución:** Verifica que los archivos .ttf estén en `app/src/main/res/font/`
- Reinicia Android Studio
- Click derecho en `font` folder → `Invalidate Caches and Restart`

### "Font file not found"
- **Solución:** Comprueba que los nombres son exactamente:
  - `quattrocento_bold.ttf` (minúsculas)
  - `quattrocento_regular.ttf` (minúsculas)

### "The font size looks too big/small"
- Ajusta en el XML:
  ```xml
  android:textSize="48sp"  <!-- Cambiar este valor -->
  ```

---

## 🎓 Para tu TFG

**Párrafo sobre tipografía:**

> "Se ha integrado el font Quattrocento, una tipografía serif elegante diseñada por Pablo Impallari. Este font proporciona una apariencia sofisticada y profesional a los títulos, diferenciándose de las tipografías monótonas por defecto de Android. El peso 'Bold' se utiliza para el título principal (Serenum) con tamaño 48sp, mientras que el peso 'Regular' se aplica al subtítulo descriptivo con tamaño 16sp. La elección de una tipografía serif contribuye a la identidad visual de la marca Serenum, transmitiendo profesionalidad y cuidado."

---

**Archivo creado:** `INSTALACION_FONT_QUATTROCENTO.md`  
**Proyecto:** Serenum v2.0  
**Font:** Quattrocento (Google Fonts)

