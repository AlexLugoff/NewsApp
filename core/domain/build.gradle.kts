plugins {
    id("newsapp.android.library")
}

android {
    namespace = "io.github.alexlugoff.newsapp.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
}
