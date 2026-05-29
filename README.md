# wordEnglish

Widget de pantalla de inicio para Android que muestra una palabra en inglés con su definición cada día.

## ¿Cómo funciona?

- Cada día se muestra una palabra diferente (rotación automática por fecha)
- Las palabras están incluidas en la app — no requiere conexión a internet
- El widget se actualiza automáticamente a medianoche

## Requisitos

- Android 13 (API 33) o superior
- Android Studio Ladybug o superior

## Compilar y ejecutar

```bash
# Debug
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug

# Tests unitarios
./gradlew test

# Tests instrumentados (requiere dispositivo/emulador)
./gradlew connectedAndroidTest
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

El `id` debe ser único y el archivo se carga en la base de datos local la primera vez que se instala la app.

## Stack técnico

| Tecnología | Uso |
|---|---|
| Jetpack Glance 1.1.1 | Widget con API Compose |
| Room 2.8.4 | Base de datos local |
| Hilt 2.59.2 | Inyección de dependencias |
| WorkManager 2.9.1 | Actualización diaria a medianoche |
| Kotlin 2.2.10 + KSP 2.2.10-2.0.2 | Lenguaje y procesado de anotaciones |
