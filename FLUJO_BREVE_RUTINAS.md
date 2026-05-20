# 📄 Flujo de Trabajo - Módulo Rutinas (BREVE)

## Lo Que Hice Hoy

1. **Resolví errores de compilación** (lint checks)
2. **Cambié botón final**: de "Continuar" a "Finalizar"
3. **Compilé exitosamente**: BUILD SUCCESSFUL ✅

---

## Clases y Funciones

### **1. Routine.java** (Modelo de datos)
```java
Serializable {
    id: String
    title: String
    duration: String
    description: String
    imageRes: int
    steps: List<RoutineStep>
}
```
**Qué hace**: Almacena datos de una rutina completa (3 pasos)

---

### **2. RoutineStep.java** (Modelo de paso)
```java
Serializable {
    type: String (MEDITATION/SOUND/TEXT)
    id: String (nombre audio: "medit1")
    title: String
    imageRes: int
    content: String (para texto)
}
```
**Qué hace**: Representa un paso dentro de una rutina

---

### **3. RoutinesDataManager.java** (Datos)
```java
static getStressReliefRoutine() {
    Routine r = new Routine("stress_relief", "Reducir estrés", "5 min", ...)
    r.addStep(new RoutineStep(MEDITATION, "medit1", ...))
    r.addStep(new RoutineStep(SOUND, "lluvia", ...))
    r.addStep(new RoutineStep(TEXT, "msg", ..., "Respira profundamente..."))
    return r
}

// Lo mismo para:
- getStartDayRoutine()
- getSleepBetterRoutine()
```
**Qué hace**: Crea y provee las 3 rutinas predefinidas

---

### **4. RoutinesActivity.java** (Catálogo)
```java
onCreate() {
    setContentView(activity_routines.xml)  // ← Layout
    cardStressRelief.setOnClickListener() → openRoutine()
    cardStartDay.setOnClickListener() → openRoutine()
    cardSleepBetter.setOnClickListener() → openRoutine()
}

openRoutine(Routine r) {
    Intent.putExtra("routine", r)  // Serializable
    startActivity(RoutinePlayerActivity.class)
}
```
**Qué hace**: Muestra 3 tarjetas, abre reproductor al tocar

---

### **5. RoutinePlayerActivity.java** (Reproductor)
```java
onCreate() {
    currentRoutine = intent.getSerializableExtra("routine")  // ← Recibe Routine
    steps = currentRoutine.getSteps()
    setContentView(activity_routine_player.xml)  // ← Layout
    loadStep(0)
}

loadStep(int idx) {
    if (idx >= steps.size()) {
        showRoutineCompleted()  // ← Botón "Finalizar" VISIBLE ✅
        return
    }
    
    step = steps.get(idx)
    tvStepNumber.setText("Paso " + (idx+1) + " de " + steps.size())
    progressBar.setProgress(idx+1)
    imgStep.setImageResource(step.getImageRes())
    tvStepTitle.setText(step.getTitle())
    
    switch(step.getType()) {
        MEDITATION: playMeditationStep(step)
        SOUND: playSoundStep(step)
        TEXT: playTextStep(step)
    }
}

playMeditationStep(step) {
    playerContainer.VISIBLE
    mediaPlayer = MediaPlayer.create(this, resolveAudioResource(step.id))
    mediaPlayer.setLooping(false)
    mediaPlayer.setOnCompletionListener( → btnContinue.VISIBLE )
    mediaPlayer.start()
    handler.post(updateSeekRunnable each 500ms)
}

playSoundStep(step) {
    playerContainer.VISIBLE
    mediaPlayer = MediaPlayer.create(...)
    mediaPlayer.setLooping(true)  // ← INFINITO
    mediaPlayer.start()
    btnContinue.VISIBLE  // ← SIEMPRE VISIBLE
}

playTextStep(step) {
    textContainer.VISIBLE
    tvStepContent.setText(step.content)
    mediaPlayer.pause()
    btnContinue.VISIBLE
}

nextStep() {
    currentStepIndex++
    loadStep(currentStepIndex)
}

showRoutineCompleted() {  // ← CAMBIO DE HOY
    completionContainer.VISIBLE
    playerContainer.GONE
    textContainer.GONE
    btnContinue.GONE
    btnFinishRoutine.VISIBLE  // ✅ AHORA VISIBLE
    btnFinishRoutine.onClick( → finish() )
}

onDestroy() {
    mediaPlayer.release()
    handler.removeCallbacks(updateSeekRunnable)
}
```
**Qué hace**: Ejecuta paso a paso, maneja audio, progresión

---

## Layouts Usados

### **activity_routines.xml** (RoutinesActivity)
```xml
FrameLayout [fondo montaña + overlay]
└─ LinearLayout (vertical)
   ├─ Header: "Rutinas"
   ├─ ScrollView
   │  └─ 3x MaterialCardView
   │     ├─ ImageView (medit1.jpg, medit2.jpg, dormir.jpeg)
   │     ├─ TextView (título, duración, descripción)
   │     └─ MaterialButton "Abrir rutina"
   └─ ImageButton "Atrás"
```

### **activity_routine_player.xml** (RoutinePlayerActivity)
```xml
FrameLayout [fondo montaña + overlay]
└─ LinearLayout (vertical)
   ├─ Header: "Paso X de Y"
   ├─ ProgressBar [0-100%]
   ├─ ImageView [imagen del paso]
   ├─ ScrollView
   │  ├─ FrameLayout playerContainer (SeekBar + Play/Pause)
   │  ├─ FrameLayout textContainer (TextView texto)
   │  └─ FrameLayout completionContainer (Mensaje finalización)
   ├─ MaterialButton "Continuar" (visibility toggled)
   ├─ MaterialButton "Finalizar" (visibility toggled) ✅
   └─ ImageButton "Atrás"
```

---

## Cómo Se Conectan

```
MainActivity
    ↓ cardRoutines.onClick()
    ↓
RoutinesActivity
    ├─ ReadRoutines: RoutinesDataManager.getStress/StartDay/SleepRoutine()
    │  └─ Returns Routine object with 3 RoutineSteps
    ├─ Shows 3 cards
    └─ On card click → Intent.putExtra(Routine)
       ↓
RoutinePlayerActivity
    ├─ Receives Routine via getSerializableExtra()
    ├─ currentStepIndex = 0
    ├─ loadStep(0) → playMeditationStep/playSoundStep/playTextStep
    ├─ Handler updates SeekBar every 500ms
    ├─ mediaPlayer.start/pause/release
    └─ btnContinue.onClick() → nextStep() → loadStep(++index)
       └─ When index >= size → showRoutineCompleted() → finish()
          └─ Returns to RoutinesActivity
```

---

## Lógica por Tipo

### **MEDITATION**
- Auto-play: `mediaPlayer.start()`
- Sin loop: `setLooping(false)`
- Botón Continuar: OCULTO (espera `onCompletionListener`)
- Cuando termina audio → Botón VISIBLE

### **SOUND**
- Auto-play: `mediaPlayer.start()`
- Con loop: `setLooping(true)` (infinito)
- Botón Continuar: SIEMPRE VISIBLE
- Usuario decide cuándo continuar

### **TEXT**
- Sin reproductor (solo texto)
- `tvStepContent.setText(content)`
- Botón Continuar: VISIBLE

---

## Cambio de Hoy ✅

**showRoutineCompleted()**:
```java
// ANTES: btnContinue no se ocultaba, btnContinue visible
// DESPUÉS:
btnContinue.setVisibility(View.GONE)      // ← NUEVO
btnFinish.setVisibility(View.VISIBLE)     // ← NUEVO
btnFinish.setOnClickListener(v → finish())
```

**Impacto**: Se aplica a todas las 3 rutinas automáticamente

---

## Recursos

**Audio** (`/res/raw/`):
```
medit1.mp3, medit2.mp3, medit3.mp3  (meditaciones)
lluvia.mp3, fuego.mp3, bosque.mp3   (sonidos)
```

**Imágenes** (`/res/drawable/`):
```
medit1.jpg, medit2.jpg, medit3.jpg
dormir.jpeg, mount.jpg (fondo)
```

**Strings** (`/res/values/strings.xml`):
```
routine_stress_relief_title = "Reducir estrés"
routines_continue = "Continuar"
routines_finish = "Finalizar"  ✅
routine_completed_title = "¡Rutina completada!"
```

---

## Resumen

| Componente | Rol | Entrada | Salida |
|-----------|-----|---------|--------|
| **Routine** | Modelo | - | Estructura con 3 RoutineSteps |
| **RoutineStep** | Modelo | - | Paso (tipo + contenido) |
| **RoutinesDataManager** | Datos | - | 3 Routine objects |
| **RoutinesActivity** | UI Catálogo | RoutinesDataManager | Intent + Routine |
| **RoutinePlayerActivity** | UI Reproductor | Intent + Routine | finish() |

---

**Status**: ✅ BUILD SUCCESSFUL  
**APK**: `/app/build/outputs/apk/debug/app-debug.apk`


