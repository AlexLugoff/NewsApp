plugins {
    id("newsapp.android.library")
    id("newsapp.android.compose")
    id("newsapp.android.hilt")
}

android {
    namespace = "com.example.newsapp.feature.sources"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.bundles.compose)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
