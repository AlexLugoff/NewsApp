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
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.bundles.lifecycle)

    // Hilt & Navigation
    implementation(libs.androidx.hilt.navigation.compose)

    // Compose (Base in plugin, rest in bundle)
    implementation(libs.bundles.compose)

    // Image Loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Network & Data
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.rssparser)
    implementation(libs.jsoup)

    // Room
    implementation(libs.androidx.room.paging)

    // Utils
    implementation(libs.timber)
    implementation(libs.kotlinx.datetime)

    // Testing
    testImplementation(libs.bundles.unitTests)
    androidTestImplementation(libs.bundles.androidTests)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
