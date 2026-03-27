plugins {
    id("newsapp.android.library")
    id("newsapp.android.compose")
}

android {
    namespace = "com.example.newsapp.core.common"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    api(libs.bundles.lifecycle)
    implementation(libs.timber)
}