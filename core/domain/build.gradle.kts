plugins {
    id("newsapp.android.library")
}

android {
    namespace = "com.example.newsapp.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.bundles.unitTests)
}
