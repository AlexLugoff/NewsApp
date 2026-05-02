package io.github.alexlugoff.newsapp.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKotlinSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlin-serialization").get().get().pluginId)
            }

            dependencies {
                add("implementation", libs.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}
