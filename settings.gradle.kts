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
include(":appNekomp")
include(":core:database")
include(":core:network")
include(":core:preferences")
include(":core:resources")
include(":feature:library")
include(":feature:login")
include(":library:datasource:auth")
include(":library:datasource:library")
include(":library:datasource:search")
include(":library:datasource:trending")
include(":library:datasource:user")
