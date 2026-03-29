plugins {
    id("newsapp.android.library")
    id("newsapp.android.room")
    id("newsapp.android.hilt")
}

android {
    namespace = "com.example.newsapp.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
