plugins {
    id("newsapp.android.library")
}

android {
    namespace = "io.github.alexlugoff.newsapp.core.common"
}

dependencies {
    api(libs.bundles.lifecycle)
    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
}
