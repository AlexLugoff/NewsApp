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
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":feature:news"))
    implementation(project(":feature:details"))
    implementation(project(":feature:sources"))

    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.bundles.lifecycle)

    // Hilt & Navigation
    implementation(libs.androidx.hilt.navigation.compose)

    // Compose
    implementation(libs.bundles.compose)

    // Image Loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Utils
    implementation(libs.timber)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.compose.foundation)

    // Testing
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
