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
include(":core:coroutines")
include(":core:database")
include(":core:ext")
include(":core:model")
include(":core:network")
include(":core:preferences")
include(":core:resources")
include(":core:ui")
include(":feature:airing")
include(":feature:discover")
include(":feature:home")
include(":feature:library")
include(":feature:login")
include(":feature:profile")
include(":feature:settings")
include(":library:datasource:airing")
include(":library:datasource:auth")
include(":library:datasource:favorite")
include(":library:datasource:kitsumodels")
include(":library:datasource:library")
include(":library:datasource:search")
include(":library:datasource:stats")
include(":library:datasource:trending")
include(":library:datasource:user")
