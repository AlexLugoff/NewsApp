plugins {
    id("newsapp.android.application")
    id("newsapp.android.compose")
    id("newsapp.android.hilt")
    id("newsapp.android.room")
    id("newsapp.android.kotlin.serialization")
}

android {
    namespace = "com.example.newsapp"

    defaultConfig {
        applicationId = "com.example.newsapp"
        testInstrumentationRunner = "com.example.newsapp.HiltTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.core.splashscreen)

    // Hilt & Navigation
    implementation(libs.androidx.hilt.navigation.compose)

    // Compose (Базовые вещи в плагине, здесь специфичные)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.compose.scrollbar)

    // Coil (Image Loading)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Network & Data
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.rssparser)
    implementation(libs.jsoup)

    // Room (Зависимости уже в плагине, здесь только Paging)
    implementation(libs.androidx.room.paging)

    // Utils
    implementation(libs.timber)
    implementation(libs.kotlinx.datetime)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)

    // Instrumented Testing
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.mockk.android)
    
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
