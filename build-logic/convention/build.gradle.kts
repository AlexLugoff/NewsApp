plugins {
    `kotlin-dsl`
}

group = "com.example.newsapp.buildlogic"

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.serialization.plugin)
    implementation(libs.compose.gradle.plugin)
    implementation(libs.hilt.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.room.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "newsapp.android.application"
            implementationClass =
                "com.example.newsapp.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "newsapp.android.library"
            implementationClass = "com.example.newsapp.convention.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "newsapp.android.feature"
            implementationClass = "com.example.newsapp.convention.AndroidFeatureConventionPlugin"
        }
        register("androidCompose") {
            id = "newsapp.android.compose"
            implementationClass = "com.example.newsapp.convention.AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "newsapp.android.hilt"
            implementationClass = "com.example.newsapp.convention.AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "newsapp.android.room"
            implementationClass = "com.example.newsapp.convention.AndroidRoomConventionPlugin"
        }
        register("androidKotlinSerialization") {
            id = "newsapp.android.kotlin.serialization"
            implementationClass =
                "com.example.newsapp.convention.AndroidKotlinSerializationConventionPlugin"
        }
        register("jvmLibrary") {
            id = "newsapp.jvm.library"
            implementationClass = "com.example.newsapp.convention.JvmLibraryConventionPlugin"
        }
    }
}
