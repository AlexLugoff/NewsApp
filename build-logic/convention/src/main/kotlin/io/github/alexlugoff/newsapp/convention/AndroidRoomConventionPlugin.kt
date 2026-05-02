package io.github.alexlugoff.newsapp.convention

import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.findPlugin("room").get().get().pluginId)
            pluginManager.apply(libs.findPlugin("ksp").get().get().pluginId)

            extensions.configure<RoomExtension> {
                // Путь к схеме БД для миграций
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                add("implementation", libs.findBundle("room").get())
                add("ksp", libs.findLibrary("room-compiler").get())
            }
        }
    }
}
