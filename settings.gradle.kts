pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "NewsApp"
include(":app")
include(":core:model")
include(":core:common")
include(":core:database")
include(":core:network")
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":core:testing")
include(":feature:news")
include(":feature:details")
include(":feature:sources")
