# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep @kotlinx.serialization.Serializable class ** { *; }
-keepclassmembers class ** { @kotlinx.serialization.SerialName *; }

# Retrofit
-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }
-dontwarn retrofit2.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Room
-keep @androidx.room.Entity class ** { *; }
-keep @androidx.room.Dao interface ** { *; }

# WorkManager
-keep class * extends androidx.work.Worker { *; }
-keep class * extends androidx.work.CoroutineWorker { *; }

# Hilt EntryPoints
-keep @dagger.hilt.EntryPoint interface ** { *; }

# Glance
-dontwarn androidx.glance.**

# Preserve line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
