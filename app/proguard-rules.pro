# Базовые правила для Android
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# Kotlin Serialization (R8 обычно справляется сам, но для надежности)
-keepclassmembers class ** {
    *** Companion;
    *** $serializer;
}

# Hilt / Dagger
-keep class dagger.hilt.**
-keep class com.google.dagger.**

# Coil
-keep class coil3.**

# Timber (не удаляем логи в release, если нужно - можно настроить удаление)
-keep class timber.log.** { *; }

# Модели данных (Keep all models to avoid serialization issues)
-keep class io.github.alexlugoff.newsapp.core.model.** { *; }
-keep class io.github.alexlugoff.newsapp.core.network.model.** { *; }
-keep class io.github.alexlugoff.newsapp.core.database.entity.** { *; }

# Ускорение сборки (отключаем некоторые проверки R8)
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.checkerframework.**
