plugins {
    id("newsapp.android.library")
    id("newsapp.android.hilt")
}

android {
    namespace = "com.example.newsapp.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.rssparser)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
}
