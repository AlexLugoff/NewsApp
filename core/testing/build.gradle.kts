plugins {
    id("newsapp.android.library")
}

android {
    namespace = "io.github.alexlugoff.newsapp.core.testing"
}

dependencies {
    api(project(":core:common"))
    
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.unitTests)
    api(libs.bundles.androidTests)
    api(libs.mockk)
}
