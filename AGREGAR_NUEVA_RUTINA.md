# Guía: Agregar Nueva Rutina a Serenum

Esta guía muestra cómo agregar una nueva rutina personalizada a la aplicación Serenum.

## Paso 1: Definir la rutina en RoutinesDataManager

Abre `RoutinesDataManager.java` y agrega un nuevo método público:

```java
/**
 * Rutina 4: Meditación al Mediodía (4 min)
 */
public static Routine getNoonMeditationRoutine() {
    Routine routine = new Routine(
            "noon_meditation",
            "Meditación al Mediodía",
            "4 min",
            "Una pausa mindful para revitalizarte a mitad del día.",
            R.drawable.medit2  // Tu imagen aquí
    );

    // Paso 1: Texto - Introducción
    RoutineStep step1 = new RoutineStep(
            RoutineStep.TYPE_TEXT,
            "noon_intro",
            "Introducción",
            R.drawable.medit2,
            "Tómate un momento para ti. Es hora de recargar energía y volver al presente."
    );

    // Paso 2: Meditación
    RoutineStep step2 = new RoutineStep(
            RoutineStep.TYPE_MEDITATION,
            "medit2",
            "Meditación Mindfulness",
            R.drawable.medit2
    );

    // Paso 3: Sonido - Pájaros
    RoutineStep step3 = new RoutineStep(
            RoutineStep.TYPE_SOUND,
            "olas",  // Usa un audio disponible
            "Sonido Natural",
            R.drawable.meditation_image_placeholder
    );

    routine.addStep(step1);
    routine.addStep(step2);
    routine.addStep(step3);

    return routine;
}
```

## Paso 2: Agregar la rutina a getAllRoutines()

Modifica el método `getAllRoutines()`:

```java
public static List<Routine> getAllRoutines() {
    List<Routine> routines = new ArrayList<>();
    routines.add(getStressReliefRoutine());
    routines.add(getStartDayRoutine());
    routines.add(getSleepBetterRoutine());
    routines.add(getNoonMeditationRoutine());  // ← AGREGAR AQUÍ
    return routines;
}
```

## Paso 3: Actualizar RoutinesActivity.java

Abre `RoutinesActivity.java` y ve a `onCreate()`. Agrega un nuevo card para tu rutina:

```java
// En onCreate(), después de las otras tarjetas:

MaterialCardView cardNoonMeditation = findViewById(R.id.cardNoonMeditation);
MaterialButton btnOpenNoonMeditation = findViewById(R.id.btnOpenNoonMeditation);

// Rutina 4: Meditación al Mediodía
View.OnClickListener openNoonMeditation = v -> openRoutine(RoutinesDataManager.getNoonMeditationRoutine());
if (cardNoonMeditation != null) cardNoonMeditation.setOnClickListener(openNoonMeditation);
if (btnOpenNoonMeditation != null) btnOpenNoonMeditation.setOnClickListener(openNoonMeditation);
```

## Paso 4: Actualizar activity_routines.xml

Dentro del `<ScrollView>`, antes de la etiqueta de cierre `</LinearLayout>`, agrega:

```xml
<!-- Rutina 4: Meditación al Mediodía -->
<com.google.android.material.card.MaterialCardView
    android:id="@+id/cardNoonMeditation"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    android:clickable="true"
    android:focusable="true"
    android:foreground="?attr/selectableItemBackground"
    app:cardBackgroundColor="@color/card_background_1"
    app:cardCornerRadius="24dp"
    app:cardElevation="0dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="120dp"
            android:contentDescription="@string/routine_image_desc"
            android:scaleType="centerCrop"
            android:src="@drawable/medit2" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:gravity="center_horizontal"
            android:padding="12dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:fontFamily="@font/quattrocentobold"
                android:text="@string/routine_noon_title"
                android:textColor="@color/white"
                android:textSize="16sp" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="4dp"
                android:fontFamily="@font/quattrocentoregular"
                android:text="@string/routine_noon_duration"
                android:textColor="@color/white"
                android:textSize="12sp" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="6dp"
                android:fontFamily="@font/quattrocentoregular"
                android:text="@string/routine_noon_description"
                android:textColor="@color/white"
                android:textSize="12sp" />

            <com.google.android.material.button.MaterialButton
                android:id="@+id/btnOpenNoonMeditation"
                android:layout_width="200dp"
                android:layout_height="38dp"
                android:layout_marginTop="10dp"
                android:text="@string/routines_open"
                android:textSize="13sp"
                android:textAllCaps="false"
                android:textColor="@color/white"
                app:backgroundTint="@color/primary"
                app:cornerRadius="18dp" />

        </LinearLayout>
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

## Paso 5: Agregar strings a strings.xml

Abre `res/values/strings.xml` y agrega en la sección de rutinas específicas:

```xml
<string name="routine_noon_title">Meditación al Mediodía</string>
<string name="routine_noon_duration">4 min</string>
<string name="routine_noon_description">Una pausa mindful para revitalizarte a mitad del día.</string>
```

## Paso 6: Compilar y Probar

```bash
./gradlew build
```

Si no hay errores, la nueva rutina aparecerá en la lista cuando ejecutes la app.

---

## Notas Importantes

- **Audios disponibles**: Usa archivos que existan en `/res/raw/`:
  - `medit1.mp3`, `medit2.mp3`, `medit3.mp3`
  - `lluvia.mp3`, `fuego.mp3`, `bosque.mp3`, `olas.mp3`, `viento.mp3`

- **Imágenes**: Usa drawables existentes o agrega nuevas en `/res/drawable/`

- **Orden de pasos**: Puedes tener cualquier número de pasos en cualquier orden

- **Duración**: Es solo informativo, la duración real depende de los audios

- **ID único**: Usa IDs descriptivos para cada rutina (ej: "morning_boost", "stress_relief", etc.)

---

## Ejemplo: Crear una rutina super simple

Si quieres una rutina de un solo audio:

```java
public static Routine getSingleMeditationRoutine() {
    Routine routine = new Routine(
            "quick_zen",
            "Zenitud Rápida",
            "3 min",
            "Una meditación rápida de relajación.",
            R.drawable.medit1
    );
    
    routine.addStep(new RoutineStep(
            RoutineStep.TYPE_MEDITATION,
            "medit1",
            "Meditación Corta",
            R.drawable.medit1
    ));
    
    return routine;
}
```

---

## Solución de Problemas

| Problema | Solución |
|----------|----------|
| "Audio no encontrado" | Verifica que el archivo esté en `/res/raw/` sin la extensión |
| Card no aparece | Reinicia Android Studio y limpia `gradlew clean` |
| Compilación falla | Asegúrate de agregar el ID del card en activity_routines.xml |
| Estilo inconsistente | Copia el XML de otro card y modifica solo los valores |


