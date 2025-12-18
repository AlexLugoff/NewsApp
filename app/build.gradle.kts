plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    alias(libs.plugins.safeargs.kotlin)
}

android {
    namespace = "com.example.newsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.newsapp"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.example.app.HiltTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.hilt.android)
    implementation(libs.androidx.espresso.contrib)
    kapt(libs.hilt.compiler)

    implementation(libs.picasso)

    // Test
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    // Mocking for Instrumented Tests
    androidTestImplementation(libs.mockk.android)
    testImplementation(libs.androidx.room.compiler.processing.testing)
    testImplementation(libs.junit)

    // Fragment/Activity Test Utilities
    androidTestImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.core.ktx)

    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.androidx.rules)

    // Hilt Testing
    androidTestImplementation(libs.dagger.hilt.android.testing)

    // LiveData Testing
    androidTestImplementation(libs.androidx.core.testing)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Glide
    implementation(libs.glide)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.simplexml)

    // SimpleXML Runtime
    implementation(libs.simple.xml)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.androidx.room.paging)

    //Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)


    //Jsoup
    implementation(libs.jsoup)

    // Timber
    implementation(libs.timber)
}