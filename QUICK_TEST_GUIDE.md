# 🚀 Guía Rápida de Prueba - Módulo de Rutinas

## Instalación y Ejecución Rápida

### **Paso 1: Generar APK**
```bash
cd C:\Users\serhii.synyshyn\AndroidStudioProjects\Serenum
.\gradlew.bat assembleDebug
# APK se genera en: app\build\outputs\apk\debug\app-debug.apk
```

### **Paso 2: Instalar en Dispositivo/Emulador**
```bash
adb install app\build\outputs\apk\debug\app-debug.apk
# O usar Android Studio para instalar directamente
```

### **Paso 3: Ejecutar App**
```
1. Abrir "Serenum" en el dispositivo
2. Navegar a través del flow de login/onboarding
3. Llegar a MainActivity (Home Screen)
```

---

## 🎯 Ruta de Prueba Principal

### **Test Flow: Reducir Estrés (5 min)**

```
HOME SCREEN
    ↓ (Scroll down to "Accesos Rápidos")
    ↓
Ver tarjeta "Rutinas" [☷ icono de historicidad]
    ↓ TOUCH
    ↓
ROUTINES CATALOG
    ├─ [← Atrás]
    ├─ Título: "Rutinas"
    ├─ Subtítulo: "Secuencias guiadas..."
    ├─ Tarjeta 1: "Reducir estrés" (medit1.jpg, 5 min)
    ├─ Tarjeta 2: "Empezar el día" (medit2.jpg, 5-6 min)
    └─ Tarjeta 3: "Dormir mejor" (dormir.jpeg, 6 min)
    ↓ TOUCH "Reducir estrés"
    ↓
ROUTINE PLAYER - STEP 1/3
    ├─ Header: "Paso 1 de 3" | [← Atrás]
    ├─ Barra de progreso: ████░░░░░░ (1/3)
    ├─ Imagen: medit1.jpg
    ├─ Título: "Respiración consciente"
    ├─ [≡ Player Container]
    │   ├─ Barra de búsqueda (0:00 - 3:00)
    │   ├─ [⏸️ Botón Play/Pause]
    │   └─ Tiempo: 0:00 / 3:00
    ├─ Botón "Continuar": OCULTO (esperando audio)
    └─ MediaPlayer: medit1.mp3 reproduce automáticamente
    
    [Esperar 3 minutos o tocar ⏩ para avanzar...]
    
    Cuando termina medit1.mp3:
    ├─ Icono de pausa cambia a play
    ├─ Botón "Continuar": VISIBLE ✅
    └─ Handler detiene actualizaciones de UI
    ↓ TOUCH "Continuar"
    ↓
ROUTINE PLAYER - STEP 2/3
    ├─ Header: "Paso 2 de 3" | [← Atrás]
    ├─ Barra de progreso: ████████░░ (2/3)
    ├─ Imagen: meditation_image_placeholder.xml (placeholder gris)
    ├─ Título: "Sonido de lluvia"
    ├─ [≡ Player Container]
    │   ├─ Barra de búsqueda (0:00 - 2:30)
    │   ├─ [⏸️ Botón Play/Pause]
    │   └─ Tiempo: 0:00 / 2:30
    ├─ Botón "Continuar": VISIBLE ✅
    └─ MediaPlayer: lluvia.mp3 reproduce en LOOP (infinito)
    
    [Lluvia reproduciendo...]
    ↓ TOUCH "Continuar"
    ↓
ROUTINE PLAYER - STEP 3/3
    ├─ Header: "Paso 3 de 3" | [← Atrás]
    ├─ Barra de progreso: ██████████ (3/3)
    ├─ Imagen: meditation_image_placeholder.xml
    ├─ Título: "Mensaje de calma"
    ├─ [Text Container - VISIBLE]
    │   └─ "Respira profundamente. Ya has hecho un excelente
    │      trabajo cuidando de ti mismo. La calma está dentro
    │      de ti, y ahora está más cerca que nunca. Sigue adelante
    │      con esta paz en tu corazón."
    ├─ Botón "Continuar": VISIBLE ✅
    └─ MediaPlayer: SE PAUSA (ya no está en loop)
    ↓ TOUCH "Continuar"
    ↓
COMPLETION SCREEN
    ├─ [Todos los containers ocultos]
    ├─ Título: "¡Rutina completada!"
    ├─ Mensaje: "Excelente trabajo. Has completado esta secuencia.
    │            Espera que esta práctica te haya traído calma y
    │            bienestar. ¡Hasta la próxima!"
    ├─ Botón: "Finalizar"
    └─ currentStepIndex >= steps.size() → showRoutineCompleted()
    ↓ TOUCH "Finalizar"
    ↓
ROUTINES CATALOG (vuelve atrás)
    └─ MediaPlayer liberado en onDestroy()
```

---

## 🔍 Escenarios de Prueba Adicionales

### **Escenario 1: Play/Pause en Meditación**
```
STEP 1 (Meditación)
1. Esperar 10 segundos, audio está reproduciendo
2. TOUCH botón ⏸️ (pausa)
   ✓ Audio se detiene
   ✓ Icono cambia a ▶️
   ✓ Tiempo en tvRoutineElapsed congela
3. TOUCH botón ▶️ (play)
   ✓ Audio reanuda desde posición anterior
   ✓ Icono cambia a ⏸️
```

### **Escenario 2: SeekBar (Buscar en Audio)**
```
STEP 1 (Meditación)
1. Esperar 10 segundos
2. DRAG SeekBar a partir de ~1:00
   ✓ Audio salta a esa posición
   ✓ tvRoutineElapsed muestra 1:00
   ✓ Reproductor mantiene estado (playing/paused)
3. DRAG regresar a 0:30
   ✓ Audio salta a 0:30
```

### **Escenario 3: Botón Atrás en Paso 2**
```
STEP 2/3 (Sonido)
1. TOUCH botón ← (atrás)
   ✓ RoutinePlayerActivity cierra
   ✓ Vuelve a RoutinesActivity
   ✓ MediaPlayer.release() se ejecuta en onDestroy()
   ✓ No hay errores de memoria
```

### **Escenario 4: Rutina 2 (Empezar el día)**
```
1. Desde catálogo, TOUCH "Empezar el día"
   ✓ Se abre RoutinePlayerActivity con rutina 2
2. STEP 1/3: Texto
   ✓ Se muestra container de texto
   ✓ Botón "Continuar" VISIBLE
   ✓ No hay reproductor visible
3. TOUCH "Continuar"
4. STEP 2/3: Meditación (medit2.mp3)
   ✓ Reproductor visible
   ✓ medit2.mp3 auto-play
5. Esperar a terminar (o buscar al final)
6. TOUCH "Continuar"
7. STEP 3/3: Sonido (bosque.mp3)
   ✓ Reproductor visible
   ✓ bosque.mp3 loop
   ✓ Botón "Continuar" VISIBLE
8. TOUCH "Continuar"
   ✓ Pantalla de completación
```

### **Escenario 5: Rutina 3 (Dormir mejor)**
```
1. Desde catálogo, TOUCH "Dormir mejor"
2. STEP 1/3: Meditación (medit3.mp3)
   ✓ Imagen: dormir.jpeg
   ✓ Auto-play medit3.mp3
3. [Igual al flujo anterior]
```

---

## 🛡️ Validaciones de Calidad

### **Memory Management** ✓
```java
// En RoutinePlayerActivity.onDestroy():
if (mediaPlayer != null) {
    if (mediaPlayer.isPlaying()) mediaPlayer.stop();
    mediaPlayer.release();
    mediaPlayer = null;
}
handler.removeCallbacks(updateSeekRunnable);
```
**Verificación**: App no muestra memory leaks después de completar rutina

### **Threading** ✓
```java
// UI updates siempre en main thread
handler.post(updateSeekRunnable); // cada 500ms
handler.removeCallbacks(updateSeekRunnable); // limpiar
```
**Verificación**: No hay crashes por acceso a UI desde thread secundario

### **State Management** ✓
```
currentStepIndex avanza correctamente de 0→1→2→3
progressBarRoutine.setProgress(currentStepIndex + 1)
tvStepNumber.setText("Paso " + (currentStepIndex + 1) + " de " + steps.size())
```
**Verificación**: Progresión es consistente y visual

### **Audio Resource Resolution** ✓
```java
private int resolveAudioResource(String audioName) {
    return getResources().getIdentifier(audioName, "raw", getPackageName());
}
// Fallback: R.raw.medit1 si no existe
```
**Verificación**: Todos los audios se encontran y reproducen

---

## 📱 Dispositivos Testeados

- [ ] **Emulador API 24** (Android 7.0)
- [ ] **Emulador API 30** (Android 11.0)
- [ ] **Emulador API 34** (Android 14.0)
- [ ] **Emulador API 35** (Android 15.0)
- [ ] **Dispositivo Real** (Especificar modelo y versión)

---

## 🐛 Troubleshooting

### **Problema**: App crashea al abrir RoutinesActivity
**Solución**:
```
1. Verificar que drawables existen (medit1, medit2, dormir)
2. Limpiar build: .\gradlew.bat clean
3. Reconstruir: .\gradlew.bat build
```

### **Problema**: Audio no suena
**Solución**:
```
1. Verificar que archivos MP3 existen en /res/raw/
2. Verificar volumen del dispositivo
3. Verificar que MediaPlayer.create() no retorna null
4. Aceptar permisos en Android 6.0+
```

### **Problema**: Botón "Continuar" no aparece después de meditación
**Solución**:
```
1. El audio debe estar completando (verificar duración real)
2. Verificar que mediaPlayer.setOnCompletionListener() se ejecute
3. Revisar logs: Log.d("RoutinePlayer", "Audio completed")
```

### **Problema**: SeekBar no funciona
**Solución**:
```
1. Verificar que seekBar no es null
2. Verificar que OnSeekBarChangeListener está registrado
3. Revisar logs: Log.d("SeekBar", "Progress: " + progress)
```

### **Problema**: MediaPlayer crash después de pausar
**Solución**:
```
1. Verificar que mediaPlayer != null antes de pausar
2. Verificar que mediaPlayer.isPlaying() es correcto
3. Usar try-catch en mediaPlayer.pause()
```

---

## ✅ Checklist Final Pre-Producción

- [ ] BUILD SUCCESSFUL sin errores
- [ ] APK generado correctamente
- [ ] Instalación en dispositivo exitosa
- [ ] Home Screen carga sin crashes
- [ ] Tarjeta "Rutinas" es toca y navega
- [ ] RoutinesActivity carga correctamente
- [ ] 3 rutinas visibles
- [ ] RoutinePlayerActivity funciona para Rutina 1
- [ ] RoutinePlayerActivity funciona para Rutina 2
- [ ] RoutinePlayerActivity funciona para Rutina 3
- [ ] Audios reproducen correctamente
- [ ] Textos muestran correctamente
- [ ] Botones de control funcionan
- [ ] Pantalla de completación se muestra
- [ ] No hay memory leaks
- [ ] No hay crashes
- [ ] Performance es fluido (60 FPS)

**Fecha de Verificación**: ____________  
**Tester**: ____________  
**Versión APK**: app-debug signed  

---

## 📞 Soporte

Para cualquier issue, revisar:
1. `ROUTINES_IMPLEMENTATION_COMPLETE.md` - Documentación completa
2. `Logcat` en Android Studio - Errores y warnings
3. `app/build/reports/lint-results-debug.html` - Lint warnings


