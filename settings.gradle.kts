enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Nekomp"
include(":androidNekomp")
include(":core:network")
include(":core:preferences")
include(":di")
include(":feature:login")
include(":library:datasource:auth")
include(":library:datasource:library")
include(":library:datasource:user")
