// settings.gradle.kts
pluginManagement {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
        gradlePluginPortal() // For plugins
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "MyApplication"
include(":app")
