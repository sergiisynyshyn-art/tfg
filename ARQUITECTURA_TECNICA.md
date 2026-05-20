# Arquitectura Técnica: Sistema de Rutinas

## Arquitectura General

```
┌─────────────────────────────────────────────────────────────┐
│                    MainActivity                              │
│  (Punto de entrada con botones de acceso rápido)            │
└────────────────┬──────────────────────────────────────────────┘
                 │
        click on cardRoutines
                 ▼
┌─────────────────────────────────────────────────────────────┐
│                RoutinesActivity                              │
│  • Lee rutinas de RoutinesDataManager                        │
│  • Muestra 3 tarjetas MaterialCardView                       │
│  • Al click, abre RoutinePlayerActivity                      │
└────────────────┬──────────────────────────────────────────────┘
                 │
        click on routine card
                 ▼
┌─────────────────────────────────────────────────────────────┐
│           RoutinePlayerActivity                              │
│  • Recibe Routine serializada via Intent                     │
│  • Controla ciclo de reproducción de pasos                   │
│  • Instancia MediaPlayer según tipo de paso                  │
│  • Muestra UI adaptativamente (audio/texto)                  │
└─────────────────────────────────────────────────────────────┘
```

## Modelo de Datos

### Routine (Serializable)
```
Routine {
  - id: String              // Identificador único
  - title: String           // "Reducir estrés"
  - duration: String        // "5 min"
  - description: String     // Descripción corta
  - imageRes: int          // Referencia a drawable
  - steps: List<RoutineStep>
}
```

### RoutineStep (Serializable)
```
RoutineStep {
  - type: String           // "meditation" | "sound" | "text"
  - id: String             // Nombre del archivo (para audio)
  - content: String        // Texto del mensaje (para type=text)
  - title: String          // "Respiración consciente"
  - imageRes: int         // Imagen del paso
}
```

## Flujo de Reproducción

```
┌─────────────────────┐
│ Cargar Rutina       │
└──────────┬──────────┘
           │
    Recibir Intent con Routine
           │
           ▼
┌─────────────────────────────────────────┐
│ RoutinePlayerActivity.loadStep(index)   │
└────────────┬────────────────────────────┘
             │
    ¿Existe step[index]?
        /  |  \
      NO  SI  FINAL
      |   |    |
      |   │    └──→ showRoutineCompleted()
      |   │         Pantalla de cierre
      |   │
      |   ▼
      | switch(step.type)
      │  /    |    \
      │ MED  SOUND TEXT
      │  |    |    |
      │  ▼    ▼    ▼
      │ [3]  [2]  [1]
      │  |    |    |
      └──┴────┴────┘
         
[1] TEXT:      Mostrar tvStepContent + btnContinue visible
[2] SOUND:     MediaPlayer.setLooping(true) + btnContinue visible
[3] MEDITATION: MediaPlayer.setLooping(false) + btnContinue visible al terminar

Al pulsar btnContinue → nextStep() → loadStep(currentStepIndex++)
```

## Estados del MediaPlayer

### Meditación (MEDITATION)
```
CREATE
  ↓
MediaPlayer.create(this, resId)
  ↓
setLooping(false)
  ↓
start() automáticamente
  ↓
[Reproduciendo...]
  ↓
onCompletion → setButtonVisible(btnContinue)
```

### Sonido (SOUND)
```
CREATE
  ↓
MediaPlayer.create(this, resId)
  ↓
setLooping(true)
  ↓
start() automáticamente
  ↓
[Reproduciendo en loop]
  ↓
Usuario pulsa "Continuar" → nextStep()
```

### Texto (TEXT)
```
No hay MediaPlayer
  ↓
Mostrar texto centrado
  ↓
btnContinue disponible inmediatamente
```

## Gestión de Recursos

```
onCreate()
  ├─ initializeUI()          // Obtener referencias de vistas
  ├─ getIntent().getSerializable() // Obtener Routine
  └─ loadStep(0)             // Carga primer paso

loadStep()
  ├─ updateUI()              // Actualizar textos e imágenes
  ├─ playXxxStep()
  │  ├─ playMeditationStep() // Crear MediaPlayer, looping=false
  │  ├─ playSoundStep()      // Crear MediaPlayer, looping=true
  │  └─ playTextStep()       // Sin MediaPlayer
  └─ setVisibility()         // Mostrar/ocultar containers

onPause()
  └─ mediaPlayer.pause()     // Detener audio

onDestroy()
  └─ mediaPlayer.release()   // Liberar recursos
```

## Integración con MainActivity

```java
// En MainActivity.setupButtons():

if (cardRoutines != null) {
    cardRoutines.setOnClickListener(v -> {
        Intent intent = new Intent(MainActivity.this, RoutinesActivity.class);
        startActivity(intent);
    });
}
```

## Estructura de Layouts

```
activity_routines.xml
├─ FrameLayout (fondo + overlay)
├─ LinearLayout (contenedor principal)
│  ├─ LinearLayout (header)
│  │  ├─ ImageButton (atrás)
│  │  └─ TextView (título)
│  ├─ TextView (subtítulo)
│  └─ ScrollView
│     └─ LinearLayout (contenedor de tarjetas)
│        ├─ MaterialCardView (Rutina 1)
│        ├─ MaterialCardView (Rutina 2)
│        └─ MaterialCardView (Rutina 3)

activity_routine_player.xml
├─ FrameLayout (fondo + overlay)
└─ LinearLayout (contenedor principal)
   ├─ LinearLayout (header: botón atrás + número paso)
   ├─ ProgressBar (progreso de rutina)
   ├─ ImageView (imagen del paso)
   ├─ ScrollView
   │  └─ LinearLayout
   │     ├─ TextView (título)
   │     ├─ FrameLayout (playerContainer)
   │     │  ├─ SeekBar
   │     │  ├─ LinearLayout (tiempo)
   │     │  └─ ImageButton (play/pause)
   │     ├─ FrameLayout (textContainer)
   │     │  └─ TextView (contenido)
   │     └─ FrameLayout (completionContainer)
   │        └─ LinearLayout (mensaje final)
   └─ LinearLayout (botones)
      ├─ MaterialButton (Continuar)
      └─ MaterialButton (Finalizar)
```

## Serialización

```java
// En RoutinesActivity:
intent.putExtra(EXTRA_ROUTINE, routine);
// Los objetos Routine y RoutineStep implementan Serializable

// En RoutinePlayerActivity:
currentRoutine = (Routine) getIntent().getSerializableExtra(EXTRA_ROUTINE);
```

## Sistema de Recursos

### Strings
```
strings.xml
├─ Genéricos
│  ├─ "routines_open"
│  ├─ "routines_continue"
│  └─ "routine_completed_*"
└─ Específicos
   ├─ "routine_stress_relief_*"
   ├─ "routine_start_day_*"
   └─ "routine_sleep_better_*"
```

### Drawables
```
res/raw/
├─ medit1.mp3
├─ medit2.mp3
├─ medit3.mp3
├─ lluvia.mp3
├─ fuego.mp3
├─ bosque.mp3
├─ olas.mp3
└─ viento.mp3

res/drawable/
├─ ic_play_white.xml (ya existe)
├─ ic_pause_white.xml (ya existe)
├─ button_play_pause_background.xml (nuevo)
├─ medit1.jpg
├─ medit2.jpg
├─ dormir.jpeg
└─ ... (otros)

res/drawable/
├─ ic_arrow_back.xml (ya existe)
└─ meditation_image_placeholder.xml (ya existe)
```

## Puntos de Extensibilidad

```
Para agregar rutinas nuevas:
1. RoutinesDataManager.java
   └─ Agregar nuevo método getXxxRoutine()
   └─ Modificar getAllRoutines()

2. RoutinesActivity.java
   └─ Agregar cardXxx y btnOpenXxx
   └─ Agregar listener en onCreate()

3. activity_routines.xml
   └─ Agregar MaterialCardView con IDs correspondientes

4. strings.xml
   └─ Agregar routine_xxx_title, duration, description

Para cambiar audios:
1. Ir a RoutinesDataManager
2. Cambiar `RoutineStep(..., "audio_name", ...)`
3. Verificar que el audio exista en /res/raw/
```

## Control de Reproducción

```
togglePlayPause()
  ├─ if mediaPlayer.isPlaying()
  │  ├─ pause()
  │  ├─ setButtonImage(ic_play_white)
  │  └─ removeCallbacks(updateSeekRunnable)
  └─ else
     ├─ start()
     ├─ setButtonImage(ic_pause_white)
     └─ post(updateSeekRunnable)

updateSeekRunnable (cada 500ms mientras está playing)
  ├─ seekBar.setProgress(mediaPlayer.getCurrentPosition())
  └─ tvElapsed.setText(formatMillis(progress))
```

## Manejo de ErroresDetección de recursos faltantes:

```java
int resId = getResources().getIdentifier(audioName, "raw", getPackageName());
if (resId == 0) {
    resId = R.raw.medit1;  // fallback
}
```

## Consideraciones de Performance

- **MediaPlayer**: Se libera en onDestroy()
- **Handler**: Se removeCallbacks() antes de crear nuevos
- **Imágenes**: Usa drawables optimizados (JPG/WebP)
- **ScrollView**: Hay que usar fillViewport=true
- **Overhead de serialización**: Routine/RoutineStep son ligeros

## Compatibilidad Android

- **minSdk**: 24
- **targetSdk**: 36
- **Libraries**: Material3, AppCompat, Firebase (ya en proyecto)
- **Permisos**: INTERNET (ya en proyecto)

