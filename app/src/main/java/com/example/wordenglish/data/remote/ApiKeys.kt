package com.example.wordenglish.data.remote

import com.example.wordenglish.BuildConfig

// La key se inyecta desde local.properties → BuildConfig (ver app/build.gradle.kts).
// Registrarse en: https://developer.wordnik.com
val WORDNIK_API_KEY: String = BuildConfig.WORDNIK_API_KEY
