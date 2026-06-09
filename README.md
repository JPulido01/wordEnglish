# wordEnglish

Widget de pantalla de inicio para Android que muestra una palabra en inglés con su definición. La palabra cambia según el intervalo que el usuario configure (1 h / 3 h / 6 h / 8 h / 12 h / 24 h).

## ¿Cómo funciona?

- Muestra una palabra diferente según el intervalo configurado (por defecto, una vez al día)
- Las palabras están incluidas en la app — funciona sin conexión a internet
- Al pulsar el widget se abre el detalle de la palabra *(en desarrollo)*
- El widget se actualiza automáticamente con WorkManager

## Requisitos

- Android 13 (API 33) o superior
- Android Studio Meerkat o superior

## Compilar y ejecutar

```bash
# Debug
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug
# alternativa si adb no está en PATH:
# /home/jpulido/Android/Sdk/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk

# Tests unitarios
./gradlew test

# Tests instrumentados (requiere dispositivo/emulador)
./gradlew connectedAndroidTest

# Release
./gradlew assembleRelease
```

## Añadir el widget

1. Mantén pulsada la pantalla de inicio
2. Selecciona **Widgets**
3. Busca **wordEnglish** y arrástralo a la pantalla

## Ampliar el vocabulario

Las palabras están en `app/src/main/assets/words.json`. Cada entrada sigue este formato:

```json
{ "id": 100, "word": "Serendipity", "definition": "The occurrence of fortunate events by chance" }
```

El `id` debe ser único y secuencial desde 0. El archivo se carga en Room la primera vez que se instala la app.

## Stack técnico

| Tecnología | Uso |
|---|---|
| Jetpack Glance 1.1.1 | Widget con API Compose |
| Room 2.8.4 | Base de datos local |
| Hilt 2.59.2 | Inyección de dependencias |
| WorkManager 2.9.1 | Actualización periódica del widget |
| DataStore 1.1.4 | Persistencia del intervalo configurado |
| Kotlin 2.2.10 + KSP 2.2.10-2.0.2 | Lenguaje y procesado de anotaciones |

## Arquitectura

Clean Architecture en tres capas:

```
wordenglish/
├── data/
│   ├── local/          # Room: WordEntity, WordDao, WordDatabase
│   │   └── seed/       # WordSeedItem — modelo de parseo JSON
│   └── repository/     # WordRepositoryImpl · IntervalRepositoryImpl (DataStore)
├── domain/
│   ├── model/          # Word · WordInterval (enum 1/3/6/8/12/24 h)
│   ├── repository/     # WordRepository · IntervalRepository
│   └── usecase/        # GetWordOfTheDayUseCase
├── di/                 # AppModule · DatabaseModule · DataStoreModule
├── ui/
│   └── settings/       # SettingsViewModel
├── widget/             # WordWidget (Glance) · WordWidgetReceiver · WordWidgetEntryPoint
└── worker/             # DailyWordWorker (WorkManager)
```

---

## Hoja de ruta (Roadmap)

> Estado: **Fase 0 — en progreso**

### Fase 0 — Consolidación del estado actual
- [ ] Commit de los módulos de intervalo y settings
- [ ] Verificar compilación limpia y flujo del widget end-to-end
- [ ] Revisar warnings de KSP/Hilt para build de release

### Fase 1 — Pantalla de detalle de palabra `[PRIORIDAD 1]`
Al pulsar el widget se abre una pantalla con información completa de la palabra.

- [ ] Ampliar el modelo `Word` con: `examples`, `ipa`, `synonyms`, `antonyms`
- [ ] Actualizar `words.json` con los nuevos campos
- [ ] Integrar [Free Dictionary API](https://dictionaryapi.dev/) (híbrido: local + API)
- [ ] `WordDetailScreen` (Compose) + `WordDetailViewModel`
- [ ] Dark / Light mode manual (toggle en Settings)
- [ ] Conectar tap del widget → pantalla de detalle

### Fase 2 — Favoritos con repaso `[PRIORIDAD 2]`
- [ ] Marcar palabras como favoritas desde el detalle
- [ ] Pantalla de lista de favoritos
- [ ] Modo repaso: recorre los favoritos uno por uno

### Fase 3 — Historial `[PRIORIDAD 3]`
- [ ] Registrar cada palabra mostrada por el widget (máx. 50)
- [ ] Pantalla de historial ordenada por fecha
- [ ] Acceso al detalle desde el historial

### Fase 4 — Notificaciones `[PRIORIDAD 4]`
- [ ] Notificación push al cambiar la palabra del día
- [ ] Tap en notificación → detalle de la palabra
- [ ] Toggle en Settings para activar/desactivar

### Fase 5 — Calidad y publicación `[MVP FINAL]`
- [ ] Tests unitarios: `GetWordOfTheDayUseCase`, `WordDetailViewModel`, `SettingsViewModel`
- [ ] Auditoría de crashes (eliminar `!!`, gestionar nulos)
- [ ] ProGuard/R8 rules para Retrofit + Room + Hilt
- [ ] Icono adaptativo y nombre definitivo
- [ ] Build release firmado verificado en dispositivo real
- [ ] Publicación en Play Store (gratis)
