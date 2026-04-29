plugins {
    `kotlin-dsl`
}

group = "io.github.alexlugoff.newsapp.buildlogic"

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
                "io.github.alexlugoff.newsapp.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "newsapp.android.library"
            implementationClass = "io.github.alexlugoff.newsapp.convention.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "newsapp.android.feature"
            implementationClass = "io.github.alexlugoff.newsapp.convention.AndroidFeatureConventionPlugin"
        }
        register("androidCompose") {
            id = "newsapp.android.compose"
            implementationClass = "io.github.alexlugoff.newsapp.convention.AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "newsapp.android.hilt"
            implementationClass = "io.github.alexlugoff.newsapp.convention.AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "newsapp.android.room"
            implementationClass = "io.github.alexlugoff.newsapp.convention.AndroidRoomConventionPlugin"
        }
        register("androidKotlinSerialization") {
            id = "newsapp.android.kotlin.serialization"
            implementationClass =
                "io.github.alexlugoff.newsapp.convention.AndroidKotlinSerializationConventionPlugin"
        }
        register("jvmLibrary") {
            id = "newsapp.jvm.library"
            implementationClass = "io.github.alexlugoff.newsapp.convention.JvmLibraryConventionPlugin"
        }
    }
}
