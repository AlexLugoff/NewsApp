plugins {
    id("newsapp.android.library")
}

android {
    namespace = "com.example.newsapp.core.common"
}

dependencies {
    api(libs.bundles.lifecycle)
    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
}
