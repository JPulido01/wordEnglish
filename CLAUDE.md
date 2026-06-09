# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

> Decisiones de diseño, lecciones de compatibilidad y contexto histórico en la memoria del proyecto:
> `~/.claude/projects/-home-jpulido-Documentos-Codigo-Android-wordEnglish/memory/MEMORY.md`

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

# Instalar en dispositivo conectado (requiere adb en PATH; si no, usar adb manual)
./gradlew installDebug
# alternativa: /home/jpulido/Android/Sdk/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk

# Build de release
./gradlew assembleRelease

# Limpiar el proyecto
./gradlew clean
```

## Qué hace la app

Widget de pantalla de inicio que muestra una palabra en inglés con su definición. La palabra cambia según el intervalo que el usuario configure (1h / 3h / 6h / 8h / 12h / 24h). Sin configuración, el default es una vez al día (`epochDay % total`).

`MainActivity` es ahora una pantalla de configuración real con radio buttons para elegir el intervalo. El cambio se persiste en DataStore y reprograma WorkManager al instante.

## Arquitectura

Clean Architecture en tres capas estrictas:

```
wordenglish/
├── data/
│   ├── local/          # Room: WordEntity, WordDao, WordDatabase
│   │   └── seed/       # WordSeedItem (@Serializable) — modelo de parseo JSON
│   └── repository/     # WordRepositoryImpl (seed lazy con Mutex)
│                       # IntervalRepositoryImpl (DataStore<Preferences>)
├── domain/
│   ├── model/          # Word · WordInterval (enum 1/3/6/8/12/24 horas)
│   ├── repository/     # WordRepository · IntervalRepository (Flow<WordInterval?>)
│   └── usecase/        # GetWordOfTheDayUseCase — rota por día o por horas según intervalo
├── di/                 # AppModule · DatabaseModule · DataStoreModule
├── ui/
│   └── settings/       # SettingsViewModel (@HiltViewModel)
├── widget/             # WordWidget (GlanceAppWidget) · WordWidgetReceiver · WordWidgetEntryPoint
├── worker/             # DailyWordWorker — WorkManager actualiza el widget según el intervalo
├── WordEnglishApp.kt   # @HiltAndroidApp — inyecta IntervalRepository, programa WorkManager al arrancar
└── MainActivity.kt     # @AndroidEntryPoint — pantalla de selección de intervalo
```

## Decisiones clave de diseño

- **Jetpack Glance** para el widget (Compose-based, no RemoteViews manual)
- **Hilt en el widget**: Los widgets no son inyectables directamente; se usa `EntryPointAccessors.fromApplication()` con `WordWidgetEntryPoint`
- **Seed lazy con Mutex**: `WordRepositoryImpl.ensureSeeded()` siembra Room desde `assets/words.json` en el primer acceso
- **DataStore para el intervalo**: `intPreferencesKey("word_interval_hours")` en el store `"settings"`. `null` = sin configurar (usa lógica diaria)
- **Lógica de índice dual**: sin intervalo → `epochDay % count`; con intervalo → `(epochHour / hours) % count`
- **WorkManager**: al cambiar intervalo usa `KEEP` en arranque y `UPDATE` al configurar, para no sobreescribir la elección del usuario
- **WorkManager sin Hilt**: `DailyWordWorker` extiende `CoroutineWorker` directamente; no necesita inyección
- **Actualización inmediata**: `SettingsViewModel.setInterval()` llama `WordWidget().updateAll(context)` tras guardar en DataStore

## Stack y versiones

- **Kotlin** 2.2.10 · **AGP** 9.2.1
- **Compose BOM** 2026.02.01 · **Glance** 1.1.1
- **Hilt** 2.59.2 · **Room** 2.8.4 · **WorkManager** 2.9.1
- **DataStore** 1.1.4 · **Hilt Navigation Compose** 1.2.0 · **Lifecycle Runtime Compose** 2.6.1
- **KSP** 2.2.10-2.0.2 (formato `{kotlin}-{ksp}`, verificar en https://github.com/google/ksp/releases si se cambia Kotlin)
- `android.disallowKotlinSourceSets=false` en `gradle.properties` — requerido para compatibilidad KSP con AGP 9.x
- **minSdk** 33 · **targetSdk** 36
- Paquete base: `com.example.wordenglish`
- **Convención version catalog**: dependencias AndroidX nuevas llevan prefijo `androidx-` (ej. `androidx-hilt-navigation-compose`) para evitar colisiones de accessors

## Verificación end-to-end

Pasos para verificar que la app y el widget funcionan correctamente en un dispositivo real.

```bash
# 1. Compilar
./gradlew assembleDebug

# 2. Instalar
/home/jpulido/Android/Sdk/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk

# 3. Abrir MainActivity
/home/jpulido/Android/Sdk/platform-tools/adb shell am start -n com.example.wordenglish/.MainActivity

# 4. Capturar pantalla
/home/jpulido/Android/Sdk/platform-tools/adb shell screencap -p /sdcard/screen.png
/home/jpulido/Android/Sdk/platform-tools/adb pull /sdcard/screen.png /tmp/screen.png

# 5. Verificar widget en logcat (SUCCESS = widget actualizado correctamente)
/home/jpulido/Android/Sdk/platform-tools/adb logcat -d 2>&1 | grep -i "wordenglish\|glance\|SessionWorker"
```

**Checklist de verificación:**
- [ ] `assembleDebug` termina con BUILD SUCCESSFUL sin errores
- [ ] `adb install` devuelve `Success`
- [ ] `MainActivity` muestra los 6 intervalos con el seleccionado destacado en azul
- [ ] Tap en un intervalo cambia la selección inmediatamente
- [ ] Widget visible en home screen con palabra + definición correctas
- [ ] Logcat muestra `Worker result SUCCESS for Work [...SessionWorker...]` sin excepciones

**Nota:** El widget debe añadirse manualmente la primera vez (long-press en home → Widgets). No se puede instalar por adb.

## Añadir palabras

Editar `app/src/main/assets/words.json`. El array de objetos `{id, word, definition}` se carga en Room en el primer arranque. El `id` debe ser único y secuencial desde 0.
