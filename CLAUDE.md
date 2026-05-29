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

## Arquitectura y tecnologías

- **UI**: Jetpack Compose con Material3
- **Actividad principal**: `MainActivity` — única actividad, punto de entrada de la app
- **Tema**: `WordEnglishTheme` en `ui/theme/` — soporta tema dinámico (Android 12+), claro y oscuro
- **minSdk**: 33 · **targetSdk**: 36
- **Kotlin**: 2.2.10 · **AGP**: 9.2.1 · **Compose BOM**: 2026.02.01

## Versiones de dependencias

Las versiones se gestionan en `gradle/libs.versions.toml` mediante Version Catalogs. Todas las dependencias se referencian con `libs.*` en los `build.gradle.kts`.

## Estado actual del proyecto

El proyecto es un esqueleto inicial generado por Android Studio. La pantalla principal solo muestra "Hello Android!". La intención del proyecto es construir una app de aprendizaje de vocabulario en inglés.
