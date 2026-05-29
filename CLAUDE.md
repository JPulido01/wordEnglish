# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Comandos principales

```bash
# Compilar el proyecto
./gradlew assembleDebug

# Ejecutar tests unitarios
./gradlew test

# Ejecutar un test unitario específico
./gradlew test --tests "com.example.wordenglish.ExampleUnitTest"

# Ejecutar tests instrumentados (requiere dispositivo/emulador)
./gradlew connectedAndroidTest

# Instalar en dispositivo conectado
./gradlew installDebug

# Build de release
./gradlew assembleRelease

# Limpiar el proyecto
./gradlew clean
```

## Qué hace la app

Widget de pantalla de inicio que muestra una palabra en inglés con su definición en inglés. La palabra cambia automáticamente cada día (rotación por índice: `epochDay % total`). No hay pantalla principal — `MainActivity` solo muestra instrucciones para añadir el widget.

## Arquitectura

Clean Architecture en tres capas estrictas:

```
wordenglish/
├── data/
│   ├── local/          # Room: WordEntity, WordDao, WordDatabase
│   │   └── seed/       # WordSeedItem (@Serializable) — modelo de parseo JSON
│   └── repository/     # WordRepositoryImpl — siembra DB desde assets/words.json si está vacía
├── domain/
│   ├── model/          # Word (modelo puro, sin dependencias Android)
│   ├── repository/     # WordRepository (interfaz)
│   └── usecase/        # GetWordOfTheDayUseCase — calcula índice por fecha
├── di/                 # DatabaseModule (Room + DAO), AppModule (binding repositorio)
├── widget/             # WordWidget (GlanceAppWidget), WordWidgetReceiver, WordWidgetEntryPoint
├── worker/             # DailyWordWorker — WorkManager actualiza el widget a medianoche
├── WordEnglishApp.kt   # @HiltAndroidApp — programa WorkManager al arrancar
└── MainActivity.kt     # @AndroidEntryPoint — pantalla mínima de instrucciones
```

## Decisiones clave de diseño

- **Jetpack Glance** para el widget (Compose-based, no RemoteViews manual)
- **Hilt en el widget**: Los widgets no son inyectables directamente; se usa `EntryPointAccessors.fromApplication()` con `WordWidgetEntryPoint` para acceder al grafo de Hilt
- **Seed lazy con Mutex**: `WordRepositoryImpl.ensureSeeded()` siembra Room desde `assets/words.json` en el primer acceso, protegido con `Mutex` contra concurrencia
- **Sin DataStore**: El índice del día se calcula en vuelo (`LocalDate.now().toEpochDay() % count`), no se persiste
- **WorkManager sin Hilt**: `DailyWordWorker` extiende `CoroutineWorker` directamente (sin `@HiltWorker`) porque no necesita inyección; llama a `WordWidget().updateAll(context)`

## Stack y versiones

- **Kotlin** 2.2.10 · **AGP** 9.2.1
- **Compose BOM** 2026.02.01 · **Glance** 1.1.1
- **Hilt** 2.59.2 · **Room** 2.8.4 · **WorkManager** 2.9.1
- **KSP** 2.2.10-2.0.2 (formato `{kotlin}-{ksp}`, verificar en https://github.com/google/ksp/releases si se cambia Kotlin)
- `android.disallowKotlinSourceSets=false` en `gradle.properties` — requerido para compatibilidad KSP con AGP 9.x
- **minSdk** 33 · **targetSdk** 36
- Paquete base: `com.example.wordenglish`

## Añadir palabras

Editar `app/src/main/assets/words.json`. El array de objetos `{id, word, definition}` se carga en Room en el primer arranque. El `id` debe ser único y secuencial desde 0.
