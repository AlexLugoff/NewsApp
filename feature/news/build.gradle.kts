plugins {
    id("newsapp.android.library")
    id("newsapp.android.compose")
    id("newsapp.android.hilt")
}

android {
    namespace = "io.github.alexlugoff.newsapp.feature.news"
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
    
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
