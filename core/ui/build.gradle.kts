plugins {
    id("newsapp.android.library")
    id("newsapp.android.compose")
}

android {
    namespace = "io.github.alexlugoff.newsapp.core.ui"
}

dependencies {
    api(project(":core:common"))
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.bundles.compose)
    implementation(libs.material)
}
