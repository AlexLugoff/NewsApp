package io.github.alexlugoff.newsapp.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidKotlinSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
        }
    }
}
