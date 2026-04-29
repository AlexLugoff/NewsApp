plugins {
    id("newsapp.android.library")
    id("newsapp.android.hilt")
}

android {
    namespace = "io.github.alexlugoff.newsapp.core.data"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))

    implementation(libs.rssparser)
    implementation(libs.jsoup)
    implementation(libs.timber)
    implementation(libs.kotlinx.datetime)

    testImplementation(project(":core:testing"))
}
