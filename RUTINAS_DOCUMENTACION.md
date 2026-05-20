# Documentación: Sección de Rutinas - Serenum

## Implementación Completada

Se ha generado exitosamente la **sección de Rutinas** para la aplicación Android Serenum. Las rutinas son secuencias guiadas que combinan content existente (meditaciones, sonidos relajantes y mensajes breves).

---

## 📁 Estructura de Archivos Creados

### Clases Java (Modelos de Datos)
1. **Routine.java** - Modelo principal de Rutina
   - Propiedades: id, title, duration, description, imageRes, steps
   - Implementa Serializable para pasar entre Activities

2. **RoutineStep.java** - Modelo de cada paso dentro de una rutina
   - Tipos soportados: MEDITATION, SOUND, TEXT
   - Propiedades: type, id, content, title, imageRes

3. **RoutinesDataManager.java** - Gestor de datos de rutinas
   - Proporciona 3 rutinas iniciales predefinidas
   - Métodos: getStressReliefRoutine(), getStartDayRoutine(), getSleepBetterRoutine()

### Activities
4. **RoutinesActivity.java** - Pantalla principal de Rutinas
   - Muestra lista de 3 rutinas disponibles
   - Cada rutina es una tarjeta clicable
   - Abre RoutinePlayerActivity al seleccionar una rutina

5. **RoutinePlayerActivity.java** - Pantalla de Reproducción de Rutina
   - Reproduce paso a paso cada rutina
   - Maneja 3 tipos de pasos:
     - **Meditación**: Reproduce audio completo, muestra botón "Continuar" al terminar
     - **Sonido**: Reproduce audio en loop, usuario controla con "Continuar"
     - **Texto**: Muestra mensaje guiado con botón "Continuar"
   - Incluye barra de progreso, seekbar, controles play/pause
   - Pantalla final de cierre al completar rutina

### Layouts XML
6. **activity_routines.xml** - Pantalla principal de rutinas
   - Fondo con overlay oscuro
   - Header con botón atrás y título
   - ScrollView con 3 tarjetas de rutinas

7. **activity_routine_player.xml** - Pantalla de reproducción
   - Header con botón atrás y número de paso
   - Barra de progreso de pasos
   - Imagen del paso actual
   - Container flexible para reproductor de audio o texto
   - SeekBar con tiempo actual/total
   - Botón play/pause
   - Botones "Continuar" y "Finalizar"
   - Pantalla de finalización con mensaje de cierre

8. **item_routine_card.xml** - Tarjeta reutilizable de rutina
   - Imagen de la rutina (140dp de alto)
   - Título, duración y descripción
   - Botón "Abrir rutina" con ícono play

### Recursos
9. **button_play_pause_background.xml** - Drawable para botón play/pause
   - Forma circular con color translúcido azul

10. **dimens.xml** - Dimensiones reutilizables
    - Padding de tarjetas, tamaños de botones, radio de esquinas

11. **Actualizado: colors.xml** - Se agregaron colores
    - card_background, card_background_1

12. **Actualizado: strings.xml** - Se agregaron 20+ strings nuevos
    - Títulos, duraciones y descripciones de las 3 rutinas
    - Strings genéricos de la sección de rutinas

13. **Actualizado: AndroidManifest.xml** - Registro de Activities
    - RoutinesActivity (no exportada)
    - RoutinePlayerActivity (no exportada)

---

## 🎯 Las 3 Rutinas Iniciales

### 1️⃣ Reducir Estrés (5 min)
- **Paso 1**: Meditación "Respiración consciente" (medit1.mp3)
- **Paso 2**: Sonido "Lluvia" (lluvia.mp3, loop)
- **Paso 3**: Texto "Mensaje de calma"

### 2️⃣ Empezar el Día (5-6 min)
- **Paso 1**: Texto "Activación matutina"
- **Paso 2**: Meditación "Meditación energizante" (medit2.mp3)
- **Paso 3**: Sonido "Bosque" (bosque.mp3, loop)

### 3️⃣ Dormir Mejor (6 min)
- **Paso 1**: Meditación "Relajación corporal" (medit3.mp3)
- **Paso 2**: Sonido "Fuego" (fuego.mp3, loop)
- **Paso 3**: Texto "Mensaje para el sueño"

---

## 🔌 Integración con MainActivity

Se actualizó **MainActivity.java** para agregar listener al botón de Rutinas:
```java
if (cardRoutines != null) {
    cardRoutines.setOnClickListener(v -> {
        Intent intent = new Intent(MainActivity.this, RoutinesActivity.class);
        startActivity(intent);
    });
}
```

El elemento `cardRoutines` ya existía en el layout `activity_home.xml`.

---

## 🎮 Flujo de Navegación

```
MainActivity
    ↓ (click en cardRoutines)
RoutinesActivity (lista de 3 rutinas)
    ↓ (click en tarjeta de rutina)
RoutinePlayerActivity (reproducción paso a paso)
    ↓ (autoriza/pausa)
[Paso 1] → [Paso 2] → [Paso 3] → Pantalla Final
```

---

## 🔊 Lógica de Reproducción

### Para Tipo: MEDITATION
- Reproduce audio completo sin loop
- Al terminar: botón "Continuar" se hace visible
- Usuario puede pausar/reanudar

### Para Tipo: SOUND
- Reproduce audio en loop infinito
- Botón "Continuar" visible desde el inicio
- Usuario puede pausar/reanudar
- No se pausa automáticamente

### Para Tipo: TEXT
- Muestra mensaje centrado
- Botón "Continuar" visible
- Sin reproductor de audio

---

## ✅ Validación de Compilación

El proyecto compila exitosamente con `./gradlew build`. 

**Estado**: ✅ BUILD SUCCESSFUL

Los únicos avisos de lint son del código existente (OnboardingActivity), no están relacionados con las rutinas.

---

## 📱 Recomendaciones de Uso

1. **Agregar más rutinas**: Extender `RoutinesDataManager.getAllRoutines()` con nuevas rutinas.

2. **Personalizar audios**: Cambiar referencias en `RoutineStep` para usar otros audios disponibles en `/res/raw/`.

3. **Internacionalización**: Todos los strings están en `strings.xml` para fácil traducción.

4. **Temas**: Los colores están centralizados en `colors.xml`.

---

## 🚀 Funcionalidades Implementadas

✅ Modelo de datos completo (Routine, RoutineStep)
✅ 3 rutinas iniciales con contenido relevante
✅ Sistema de reproducción paso a paso
✅ Manejo de 3 tipos de contenido (meditación, sonido, texto)
✅ UI coherente con la aplicación existente
✅ Barra de progreso visual
✅ Controles de reproducción (play/pause, seekbar)
✅ Pantalla de cierre celebratorio
✅ Integración con MainActivity
✅ Serialización para paso de datos entre Activities
✅ Gestión correcta del lifecycle de MediaPlayer

---

**Fecha**: 18 de Mayo de 2026
**Estado**: Completado y compilable

