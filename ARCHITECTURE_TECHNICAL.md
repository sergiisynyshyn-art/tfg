# 🏗️ Arquitectura Técnica - Módulo de Rutinas

## Diagrama de Flujo

```
┌─────────────────────────────────────────────────────────────────────┐
│                          APPLICATION FLOW                           │
└─────────────────────────────────────────────────────────────────────┘

                              ┌──────────────┐
                              │  LoginScreen │
                              │   (Firebase  │
                              │     Auth)    │
                              └──────┬───────┘
                                     │
                    ┌────────────────▼──────────────┐
                    │   OnboardingActivity (3 pages) │
                    └────────────────┬───────────────┘
                                     │
         ┌───────────────────────────▼────────────────────────┐
         │                                                    │
         │         ┌──────────────────────────┐               │
         │         │  InitialSurveyActivity   │               │
         │         │  (Usuario selecciona goals)│               │
         │         └──────────────┬───────────┘               │
         │                        │                           │
         │  ┌─────────────────────▼──────────────────┐        │
         │  │     MainActivity (HOME SCREEN)         │        │
         │  ├──────────────────────────────────────┤        │
         │  │ • Header: Saludo personalizado         │        │
         │  │ • Recomendación para hoy               │        │
         │  │ • ACCESOS RÁPIDOS:                     │        │
         │  │   - [🧘 Meditaciones]              │        │
         │  │   - [🔊 Sonidos]                   │        │
         │  │   - [⏱️ ROUTINES] ←─────────────────┐│        │
         │  │   - [💪 Ejercicios]                │││        │
         │  │ • Contenido destacado                 │││        │
         │  │ • Bottom Nav (home, explore, etc.)    │││        │
         │  └─────────────────────────────────────┘││        │
         │                                           ││        │
         │  ┌──────────────────────────────────────┐││        │
         │  │   RoutinesActivity (CATALOG)         │││        │
         │  ├──────────────────────────────────────┤││        │
         │  │ • Header: "Rutinas"                  │││        │
         │  │ • Subtítulo: "Secuencias guiadas..." │││        │
         │  │                                      │││        │
         │  │ [Tarjeta 1]                          │││        │
         │  │ ┌────────────────────────────────┐   │││        │
         │  │ │  Reducir estrés                 │   │││        │
         │  │ │  [medit1.jpg]                  │   │││        │
         │  │ │  5 min                         │   │││        │
         │  │ │  "Una secuencia guiada..."     │   │││        │
         │  │ │  [Abrir rutina]────────────────┼───┼┼─┐       │
         │  │ └────────────────────────────────┘   │││ │       │
         │  │                                      │││ │       │
         │  │ [Tarjeta 2]                          │││ │       │
         │  │ ┌────────────────────────────────┐   │││ │       │
         │  │ │  Empezar el día               │   │││ │       │
         │  │ │  [medit2.jpg]                  │   │││ │       │
         │  │ │  5-6 min                       │   │││ │       │
         │  │ │  "Activa tu mente y cuerpo..." │   │││ │       │
         │  │ │  [Abrir rutina]                │   │││ │       │
         │  │ └────────────────────────────────┘   │││ │       │
         │  │                                      │││ │       │
         │  │ [Tarjeta 3]                          │││ │       │
         │  │ ┌────────────────────────────────┐   │││ │       │
         │  │ │  Dormir mejor                  │   │││ │       │
         │  │ │  [dormir.jpeg]                 │   │││ │       │
         │  │ │  6 min                         │   │││ │       │
         │  │ │  "Prepárate para descansar..." │   │││ │       │
         │  │ │  [Abrir rutina]                │   │││ │       │
         │  │ └────────────────────────────────┘   │││ │       │
         │  │                                      │││ │       │
         │  │ [← Atrás]                           │││ │       │
         │  └──────────────────────────────────────┘││ │       │
         │                                          │  │       │
         │  ┌───────────────────────────────────────┘  │       │
         │  │                                           │       │
         │  │  RoutinePlayerActivity (PLAYER)          │       │
         │  ├───────────────────────────────────────┤       │
         │  │ [← Atrás] Paso 1 de 3                │       │
         │  │ ███░░░░░░░░░░░░░░ Progress         │       │
         │  │                                       │       │
         │  │ [████ Image: medit1.jpg]             │       │
         │  │                                       │       │
         │  │ Title: Respiración consciente        │       │
         │  │                                       │       │
         │  │ ═══════════════════════════════════  │       │
         │  │ IF TYPE_MEDITATION:                 │       │
         │  │ ├─ [Seekbar 0:00 - 3:00]            │       │
         │  │ ├─ [⏸️ Play/Pause]                   │       │
         │  │ ├─ Botón \"Continuar\" (hidden)      │       │
         │  │ └─ MediaPlayer.start()              │       │
         │  │                                       │       │
         │  │ ═══════════════════════════════════  │       │
         │  │ IF TYPE_SOUND:                      │       │
         │  │ ├─ [Seekbar 0:00 - 2:30]            │       │
         │  │ ├─ [⏸️ Play/Pause]                   │       │
         │  │ ├─ Botón \"Continuar\" (visible)     │       │
         │  │ └─ setLooping(true)                 │       │
         │  │                                       │       │
         │  │ ═══════════════════════════════════  │       │
         │  │ IF TYPE_TEXT:                       │       │
         │  │ ├─ Reproductor (hidden)             │       │
         │  │ ├─ Text: \"Respira profundamente...\" │       │
         │  │ └─ Botón \"Continuar\" (visible)     │       │
         │  │                                       │       │
         │  │ [Continuar] ────► currentStepIndex++ │       │
         │  └───────────────────┬──────────────────┘       │
         │                      │                         │
         │                  ┌───▼──────────────────────┐   │
         │                  │ NEW STEP (2/3)           │   │
         │                  │ Same flow, different data│   │
         │                  └───┬──────────────────────┘   │
         │                      │                         │
         │                  ┌───▼──────────────────────┐   │
         │                  │ NEW STEP (3/3)           │   │
         │                  │ Same flow, different data│   │
         │                  └───┬──────────────────────┘   │
         │                      │                         │
         │        ┌─────────────▼──────────────┐          │
         │        │ Completion Screen          │          │
         │        │ \"¡Rutina completada!\"     │          │
         │        │ Mensaje de felicitación    │          │
         │        │ [Finalizar]────┐           │          │
         │        └────────────────┼───────────┘          │
         │                         │                      │
         │                    ┌────▼──────────────┐      │
         │                    │ onDestroy():      │      │
         │                    │ • mediaPlayer.    │      │
         │                    │   release()       │      │
         │                    │ • handler.        │      │
         │                    │   removeCallbacks │      │
         │                    └────┬──────────────┘      │
         │                         │                     │
         │        ◄────────────────┘                     │
         │        Back to RoutinesActivity               │
         │                         │                     │
         │        ◄────────────────┘                     │
         │        Back to MainActivity                   │
         │                         │                     │
         └────────────────────────▼──────────────────────┘
                                 │
                          ▼ Continue...


```

---

## Arquitectura de Clases

```
┌────────────────────────────────┐
│ MainActivity                   │
├────────────────────────────────┤
│ + playMeditation(View v)       │
│ + setupButtons()               │
│ + configurarSaludo()           │
│                                │
│ intentFilter: cardRoutines     │
│ ➔ RoutinesActivity            │
└────────────────────────────────┘
           │
           │ Intent
           ▼
┌────────────────────────────────┐
│ RoutinesActivity               │
├────────────────────────────────┤
│ - btnBackRoutines              │
│ - cardStressRelief             │
│ - btnOpenStressRelief          │
│ - cardStartDay                 │
│ - btnOpenStartDay              │
│ - cardSleepBetter              │
│ - btnOpenSleepBetter           │
│                                │
│ + onCreate()                   │
│ + openRoutine(Routine)         │
│                                │
│ Uses: RoutinesDataManager      │
└────────────────────────────────┘
           │
           │ Intent + putExtra(routine)
           ▼
┌────────────────────────────────┐
│ RoutinePlayerActivity          │
├────────────────────────────────┤
│ - currentRoutine: Routine      │
│ - steps: List<RoutineStep>     │
│ - currentStepIndex: int        │
│ - mediaPlayer: MediaPlayer     │
│ - handler: Handler             │
│                                │
│ - imgStep: ImageView           │
│ - tvStepTitle: TextView        │
│ - tvStepContent: TextView      │
│ - btnContinue: MaterialButton   │
│ - btnPlayPause: ImageButton     │
│ - seekBar: SeekBar             │
│ - tvElapsed: TextView          │
│ - tvTotal: TextView            │
│ - progressBarRoutine: ProgressBar
│ - playerContainer: FrameLayout │
│ - textContainer: FrameLayout   │
│ - completionContainer: FrameLayout
│                                │
│ + onCreate()                   │
│ + initializeUI()               │
│ + loadStep(int stepIndex)      │
│ + playMeditationStep()         │
│ + playSoundStep()              │
│ + playTextStep()               │
│ + togglePlayPause()            │
│ + nextStep()                   │
│ + showRoutineCompleted()       │
│ + resolveAudioResource()       │
│ + formatMillis()               │
│ + onDestroy()                  │
│ + onPause()                    │
│ + onResume()                   │
└────────────────────────────────┘


┌────────────────────────────────┐
│ Routine (Data Model)           │ ◄─┐ Serializable
├────────────────────────────────┤   │
│ - id: String                   │   │
│ - title: String                │   │
│ - duration: String             │   │
│ - description: String          │   │
│ - imageRes: int                │   │
│ - steps: List<RoutineStep>     │   │
│                                │   │
│ + getters/setters              │   │
│ + addStep()                    │   │
└────────────────────────────────┘   │
                                     │
┌────────────────────────────────┐   │
│ RoutineStep (Data Model)       │ ──┘
├────────────────────────────────┤
│ + TYPE_MEDITATION: String      │
│ + TYPE_SOUND: String           │
│ + TYPE_TEXT: String            │
│                                │
│ - type: String                 │
│ - id: String                   │
│ - title: String                │
│ - imageRes: int                │
│ - content: String              │
│                                │
│ + getters/setters              │
└────────────────────────────────┘


┌────────────────────────────────┐
│ RoutinesDataManager            │
│ (Data Source)                  │
├────────────────────────────────┤
│ + static Routine[]             │
│   getStressReliefRoutine()     │
│                                │
│ + static Routine[]             │
│   getStartDayRoutine()        │
│                                │
│ + static Routine[]             │
│   getSleepBetterRoutine()     │
│                                │
│ + static List<Routine>         │
│   getAllRoutines()             │
└────────────────────────────────┘
```

---

## Máquina de Estados - RoutinePlayerActivity

```
         ┌─────────────────────────────────────────┐
         │ START: RoutinePlayerActivity created     │
         │ - currentRoutine = intent.getSerializable│
         │ - currentStepIndex = 0                   │
         │ - loadStep(0)                            │
         └──────────────┬──────────────────────────┘
                        │
           ┌────────────▼─────────────┐
           │ PHASE: Load Step         │
           ├─────────────────────────┤
           │ if (stepIndex < 0 ||    │
           │     stepIndex >= size)  │
           │   → showRoutineCompleted│
           │   → END                 │
           │ else:                   │
           │   step = steps.get()    │
           │   tvStepNumber.setText()│
           │   progressBar.setMax()  │
           │   imgStep.setImageRes() │
           │   tvStepTitle.setText() │
           └────┬───────────────────┘
                │
         ┌──────▼──────────────────────┐
         │ DECISION: Step Type?        │
         └──────┬─────────────┬────────┘
                │             │
    ┌───────────▼─┐   ┌───────▼────────┐   ┌──────────────┐
    │ MEDITATION  │   │ SOUND          │   │ TEXT         │
    └───────┬─────┘   └───────┬────────┘   └──────┬───────┘
            │                 │                   │
    ┌───────▼──────────────┐   │   ┌──────────────▼──────┐
    │ playMeditationStep() │   │   │ playTextStep()       │
    ├──────────────────────┤   │   ├─────────────────────┤
    │ • playerContainer:   │   │   │ • textContainer:    │
    │   VISIBLE            │   │   │   VISIBLE           │
    │ • mediaPlayer.create │   │   │ • tvStepContent.    │
    │ • setLooping(false)  │   │   │   setText()         │
    │ • seekBar.setMax()   │   │   │ • mediaPlayer.pause │
    │ • mediaPlayer.start()│   │   │ • btnContinue:      │
    │ • setOnCompletion()  │   │   │   VISIBLE           │
    │ • btnContinue:       │   │   └─────────┬──────────┘
    │   HIDDEN (initially) │   │             │
    │                      │   │   ┌─────────▼───────┐
    │ Handler posts        │   │   │ playSoundStep() │
    │ updateSeekRunnable   │   │   ├─────────────────┤
    └──┬──────────────────┘   │   │ • playerContainer
       │                      │   │   VISIBLE
       │                      │   │ • mediaPlayer.create
    ┌──▼──────────────┐       │   │ • setLooping(true)
    │ Audio playing... │       │   │ • seekBar.setMax()
    │ waitingon        │       │   │ • mediaPlayer.start()
    │ completion       │       │   │ • btnContinue:
    └──┬──────────────┘       │   │   VISIBLE (always)
       │                      │   │ • Handler posts
       │ (after 3 min)        │   │   updateSeekRunnable
       │                      │   └──┬────────────────┘
    ┌──▼──────────────────────┐      │
    │ Audio completed!        │      │ (audio loops
    │ • tvElapsed = duration  │      │  until user
    │ • btnContinue: VISIBLE  │      │  decides)
    │ • btnPlayPause: PLAY    │      │
    │ • Handler.removeCallbacks      │
    └──┬───────────────────────┘     │
       │                            │
       │                            │
       └────────┬───────────────────┘
                │
        ┌───────▼──────────────────┐
        │ User touches "Continuar" │
        ├───────────────────────────┤
        │ • mediaPlayer.pause()     │
        │ • Handler.removeCallbacks │
        │ • currentStepIndex++      │
        │ • loadStep(currentIndex)  │
        └────┬──────────────────────┘
             │
         ┌───▼──────────────────────┐
         │ Check if more steps      │
         ├──────────────────────────┤
         │ if (currentIndex < size) │
         │   → New step             │
         │ else                     │
         │   → Completion           │
         └──┬───────────────────────┘
            │
   ┌────────▼─────────────────────┐
   │ COMPLETION SCREEN            │
   ├──────────────────────────────┤
   │ • completionContainer:       │
   │   VISIBLE                    │
   │ • playerContainer: GONE      │
   │ • textContainer: GONE        │
   │ • tvTitle: "¡Completada!"    │
   │ • tvMessage: (congratulations)
   │ • btnFinish.onClick()        │
   │   → finish()                 │
   └────┬─────────────────────────┘
        │
   ┌────▼──────────────────────────┐
   │ onDestroy()                    │
   ├────────────────────────────────┤
   │ • mediaPlayer.stop() (safe)    │
   │ • mediaPlayer.release()        │
   │ • handler.removeCallbacks()    │
   │ • mediaPlayer = null           │
   │ • Handler = null (GC)          │
   └────┬─────────────────────────┘
        │
        ▼
   Activities.finish()
   Back to RoutinesActivity

```

---

## MediaPlayer Lifecycle

```
INITIALIZATION
  ├─ MediaPlayer.create(context, resId)
  │  └─ Prepares audio file
  └─ mediaPlayer != null checks passed

PLAYING STATE
  ├─ mediaPlayer.start()
  │  ├─ Audio empieza a reproducir
  │  └─ Handler.post(updateSeekRunnable) cada 500ms
  │
  ├─ Handler Update Runnable:
  │  ├─ seekBar.setProgress(mediaPlayer.getCurrentPosition())
  │  ├─ tvElapsed.setText(formatMillis(pos))
  │  └─ Handler.postDelayed(this, 500) → loop
  │
  ├─ User toca pause:
  │  ├─ mediaPlayer.pause()
  │  ├─ Handler.removeCallbacks(updateSeekRunnable)
  │  └─ btnPlayPause.setImageResource(IC_PLAY)
  │
  ├─ User toca play:
  │  ├─ mediaPlayer.start()
  │  ├─ Handler.post(updateSeekRunnable)
  │  └─ btnPlayPause.setImageResource(IC_PAUSE)

SEEKING (User drags SeekBar)
  ├─ OnSeekBarChangeListener.onStartTrackingTouch():
  │  ├─ if mediaPlayer.isPlaying() → pause
  │  └─ wasPlaying = true
  │
  ├─ OnSeekBarChangeListener.onProgressChanged(fromUser):
  │  ├─ if fromUser:
  │  │  ├─ mediaPlayer.seekTo(progress)
  │  │  └─ tvElapsed.setText()
  │
  └─ OnSeekBarChangeListener.onStopTrackingTouch():
     └─ если wasPlaying:
        └─ mediaPlayer.start()

COMPLETION
  ├─ mediaPlayer.setOnCompletionListener():
  │  ├─ btnPlayPause.setImageResource(IC_PLAY)
  │  ├─ Handler.removeCallbacks(updateSeekRunnable)
  │  └─ if (btnContinue) btnContinue.setVisibility(VISIBLE)
  │
  └─ User taps "Continuar":
     ├─ mediaPlayer.pause() (safe)
     ├─ Handler.removeCallbacks()
     └─ Move to next step or completion

CLEANUP (onDestroy)
  ├─ if mediaPlayer != null:
  │  ├─ try:
  │  │  ├─ if mediaPlayer.isPlaying():
  │  │  │  └─ mediaPlayer.stop()
  │  │  └─ mediaPlayer.release()
  │  └─ catch Exception: (ignored)
  │
  └─ Handler.removeCallbacks(updateSeekRunnable)

LOOPING (Special case for sounds)
  ├─ mediaPlayer.setLooping(true)
  └─ Audio nunca dispara onCompletion
     (debe presionar usuario "Continuar")

NO-LOOPING (Meditation/Text)
  ├─ mediaPlayer.setLooping(false)
  └─ Audio dispara onCompletion cuando termina

```

---

## Thread Safety

```
MAIN THREAD (UI Thread)
├─ onCreate() → initialize UI
├─ setContentView() → inflate layouts
├─ findViewById() → get references
│
├─ onClick handlers:
│  ├─ btnPlayPause.onClick() → togglePlayPause()
│  ├─ btnContinue.onClick() → nextStep()
│  ├─ seekBar.onChange() → mediaPlayer.seekTo()
│  └─ btnBack.onClick() → finish()
│
├─ Handler.post(runnable) → postDelayed every 500ms
│  └─ updateSeekRunnable runs on Main Thread
│
├─ mediaPlayer operations:
│  ├─ mediaPlayer.start() ✓ Safe on Main Thread
│  ├─ mediaPlayer.pause() ✓ Safe on Main Thread
│  ├─ mediaPlayer.seekTo() ✓ Safe on Main Thread
│  ├─ mediaPlayer.release() ✓ Safe on Main Thread (onDestroy)
│  └─ mediaPlayer.isPlaying() ✓ Safe on Main Thread
│
└─ UI updates:
   ├─ seekBar.setProgress() ✓ Main Thread only
   ├─ tvElapsed.setText() ✓ Main Thread only
   ├─ imgStep.setImageResource() ✓ Main Thread only
   ├─ btnContinue.setVisibility() ✓ Main Thread only
   └─ playerContainer.setVisibility() ✓ Main Thread only

IMPORTANT:
⚠️  MediaPlayer.create() is SYNCHRONOUS and MAY BLOCK
    → Happens on Main Thread but is acceptable for small files

💡 OPTIMIZATION OPTIONS (not yet implemented):
   ├─ Use Service with MediaSession for background playback
   ├─ Use MediaPlayer in separate thread + synchronization
   ├─ Use WorkManager for background audio tasks
   └─ Pre-load audio files before playback
```

---

## Resource Management

```
MEMORY MANAGEMENT
├─ MediaPlayer Instance:
│  ├─ Created: MediaPlayer.create(context, resId)
│  ├─ Stored: private MediaPlayer mediaPlayer;
│  ├─ Lifecycle: onCreat() → onDestroy()
│  └─ Released: mediaPlayer.release() in onDestroy()
│
├─ Handler Instance:
│  ├─ Created: private final Handler handler = new Handler();
│  ├─ Runnable: updateSeekRunnable
│  ├─ Posted: handler.post(updateSeekRunnable) every 500ms
│  └─ Cleared: handler.removeCallbacks() onDestroy() + onPause()
│
├─ Intent Extra:
│  ├─ Type: Serializable (Routine object)
│  ├─ Size: ~2KB per routine (acceptable)
│  └─ Garbage collected: When activity destroyed
│
└─ ImageView Resources:
   ├─ setImageResource() → Android manages
   ├─ Previous drawable freed automatically
   └─ Bitmaps cached by system

DRAWABLE RESOURCES
├─ mount.jpg: ~2MB (background, cached)
├─ medit1.jpg: ~500KB (step image)
├─ medit2.jpg: ~500KB (step image)
├─ dormir.jpeg: ~500KB (step image)
├─ meditation_image_placeholder.xml: Vector (negligible)
└─ ic_play_white.xml: Vector (negligible)

AUDIO RESOURCES
├─ medit1.mp3: ~3MB (3 min audio)
├─ medit2.mp3: ~3MB (3 min audio)
├─ medit3.mp3: ~3MB (3 min audio)
├─ lluvia.mp3: ~2MB (looped water sound)
├─ fuego.mp3: ~2MB (looped fire sound)
└─ bosque.mp3: ~2MB (looped forest sound)

TOTAL APK SIZE: ~40-50MB (estimated)

OPTIMIZATION OPPORTUNITIES (for future):
├─ Use Lossless Audio Codec (reduce file size)
├─ Stream audio from server (instead of bundled)
├─ Lazy-load images (don't cache all routines)
├─ Use WebP for images (instead of JPG)
└─ Compress drawables with vector optimization
```

---

## Networking & Dependencies

```
EXTERNAL DEPENDENCIES
├─ androidx.appcompat:appcompat (Activity, resources)
├─ androidx.constraintlayout:constraintlayout (if used)
├─ com.google.android.material:material (MaterialButton, CardView)
├─ androidx.activity:activity-ktx (EdgeToEdge, lifecycle)
└─ (No external audio fetching at this version)

INTERNAL DEPENDENCIES
├─ MainActivity → RoutinesActivity (Intent navigation)
├─ RoutinesActivity → RoutinePlayerActivity (Intent + Serializable)
├─ RoutinePlayerActivity → RoutinesDataManager (static methods)
├─ RoutinePlayerActivity → Routine (model)
├─ RoutinePlayerActivity → RoutineStep (model)
└─ Resources: strings.xml, colors.xml, drawables, raw audio

FIREBASE INTEGRATION
├─ Currently: Only used in LoginActivity (auth)
├─ Not used in Routines module
└─ Future: Could log routine completions to Firebase Analytics

LOCAL STORAGE
├─ NOT IMPLEMENTED: Routines are hardcoded in RoutinesDataManager
├─ Future opportunity: SQLite database for user routines
├─ Future opportunity: Room database for persistence
└─ Current: In-memory Routine objects (GC after activity close)
```

---

## Performance Metrics

```
EXPECTED PERFORMANCE
├─ Activity Launch Time:
│  ├─ RoutinesActivity: ~200-300ms (3 cards, 3 images loading)
│  └─ RoutinePlayerActivity: ~100-150ms (UI inflation + first image)
│
├─ Audio Playback:
│  ├─ Start latency: ~50-100ms (MediaPlayer.create + start)
│  ├─ Pause latency: <10ms
│  ├─ Seek latency: ~100-200ms (depends on audio codec)
│  └─ Memory usage: ~1-2MB per active MediaPlayer
│
├─ UI Updates:
│  ├─ SeekBar update: Every 500ms (smooth, not too frequent)
│  ├─ Timestamp update: Every 500ms (synchronized with SeekBar)
│  ├─ Visibility changes: <5ms
│  └─ TextView setText: <5ms
│
├─ Frame Rate:
│  ├─ Target: 60 FPS (expected on most devices)
│  ├─ No heavy computations in onDraw
│  └─ Handler.postDelayed doesn't block UI
│
└─ Battery Consumption:
   ├─ Audio playback: ~30mA (typical MediaPlayer)
   ├─ Screen on: Additional battery drain (expected)
   ├─ No unnecessary background threads
   └─ MediaPlayer paused when not in use

OPTIMIZATION TIPS (if needed):
├─ Use MediaSessionService for better performance
├─ Cache ImagesResources (already done by Android)
├─ Pre-load next routine while current plays
├─ Use ViewerBinding instead of findViewById (code quality)
└─ Profile with Android Profiler (CPU, Memory, Network)
```

---

## Error Handling Strategy

```
ERRORS HANDLED
├─ MediaPlayer.create() returns null:
│  └─ Toast: "No se pudo cargar el audio"
│
├─ Audio file not found (resolveAudioResource):
│  └─ Fallback to R.raw.medit1
│
├─ findViewById returns null:
│  └─ null-safe checks: if (view != null) { ... }
│
├─ Routine from Intent is null:
│  └─ Toast: "Error: Rutina no encontrada" + finish()
│
├─ Null pointer in mediaPlayer operations:
│  └─ if (mediaPlayer != null) { mediaPlayer.start(); }
│
├─ SeekBar out of bounds:
│  └─ SeekBar handles bounds automatically
│
└─ Activity destroyed during playback:
   └─ onDestroy() safely releases MediaPlayer

FUTURE ERROR LOGGING
├─ Implement Crashlytics for tracking
├─ Log user actions: "User clicked Continue after 2:30"
├─ Log audio errors: "medit2.mp3 failed to load"
├─ Monitor performance: "First routine launch took 250ms"
└─ Track user behavior: "50% of users complete all routines"

EDGE CASES HANDLED
├─ Back button pressed mid-audio: MediaPlayer released safely
├─ Screen rotation: (if not locked, mediaPlayer survives)
├─ Low battery: (no special handling, system manages)
├─ Audio device changes: (headphones to speaker, etc.)
│  ├─ MediaPlayer continues on default audio route
│  └─ No explicit handling needed
└─ User minimizes app (onPause): mediaPlayer.pause() called
   └─ Resumes on onResume() if user returns
```

---

## Seguridad & Privacy

```
NO SENSITIVE DATA
├─ Routines are public content
├─ No user auth required for routines
├─ No personal data collected
├─ No network requests
└─ No analytics tracking (yet)

PERMISSIONS USED
├─ <uses-permission android:name="android.permission.INTERNET" />
│  └─ Required by Firebase (auth module)
│  └─ Not used by Routines module specifically
│
└─ No special permissions needed for:
   ├─ Audio playback (INTERNET permission not required)
   ├─ File access (resources bundled in APK)
   ├─ Storage access (not used)
   └─ Camera/Microphone (not used)

FUTURE SECURITY CONSIDERATIONS
├─ If syncing routines to cloud:
│  └─ Use HTTPS + SSL pinning
│
├─ If storing user progress:
│  └─ Encrypt data at rest + in transit
│
├─ If implementing user accounts:
│  └─ Follow OAuth2 best practices
│
└─ If adding in-app purchases:
   └─ Use Google Play Billing Library
```

---

## Testing Strategy

```
UNIT TESTS (TODO)
├─ RoutineStep.getType()
├─ Routine.addStep()
├─ RoutinesDataManager.getStressReliefRoutine()
└─ formatMillis(int ms)

INTEGRATION TESTS (TODO)
├─ Intent passing Routine from RoutinesActivity to RoutinePlayerActivity
├─ MediaPlayer lifecycle (create → play → pause → stop → release)
├─ Handler updates seekBar correctly
└─ Visibility changes for containers based on step type

UI TESTS (TODO - Espresso)
├─ Tap on RoutinesActivity card → load RoutinePlayerActivity
├─ Verify "Paso X de Y" text updates
├─ Verify audio plays and stops correctly
├─ Tap Continue → move to next step
├─ Tap Back → return to RoutinesActivity
└─ Verify MediaPlayer released after finish()

MANUAL TESTS (RECOMMENDED)
├─ Complete full routine (5-10 min)
├─ Test all three routines (Reducir estrés, Empezar día, Dormir)
├─ Test Play/Pause controls
├─ Test SeekBar seeking
├─ Test back button at various stages
├─ Test device rotation (if not locked)
├─ Test connecting/disconnecting headphones
└─ Test low battery scenario
```

---

## Deployment Checklist

```
PRE-RELEASE
  ☑ BUILD SUCCESSFUL without errors
  ☑ No critical lint warnings
  ☑ All strings externalized
  ☑ All resources present (audio, images)
  ☑ AndroidManifest updated
  ☑ All activities registered
  ☑ Target SDK = 34 (or higher)
  ☑ Min SDK = 24
  ☑ App signing configured
  ☑ ProGuard rules configured (if minify enabled)
  ☑ Version code incremented
  ☑ Version name updated

TESTING COMPLETE
  ☑ Runs on API 24 emulator
  ☑ Runs on API 34 emulator
  ☑ Runs on real device (if available)
  ☑ No crashes observed
  ☑ No ANR (Application Not Responding)
  ☑ Memory usage acceptable (<100MB)
  ☑ Battery drain acceptable
  ☑ Audio quality good
  ☑ UI responsive
  ☑ Back navigation works

RELEASE NOTES
  - Feature: Rutinas (Routines) module
  - 3 predefined routines:
    * Reducir estrés (5 min)
    * Empezar el día (5-6 min)
    * Dormir mejor (6 min)
  - Each routine combines meditation, sound, and text steps
  - Auto-play for meditations, looping for sounds
  - Visual progress bar and step counter
  - Completion screen with celebration message

ANALYTICS (if implemented)
  - Track routine completions
  - Track step-by-step duration
  - Track user dropoff points
  - Collect feedback via surveys
```


