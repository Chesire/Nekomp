import io.gitlab.arturbosch.detekt.Detekt
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotest.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktorfit) apply false
    alias(libs.plugins.mockkery) apply false
}

tasks.register<Detekt>("detektCheck") {
    parallel = true
    ignoreFailures = true
    basePath = "$rootDir/.."
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    config.setFrom(files("$rootDir/detekt.yml"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

private val excludeProjects = setOf(
    ":core", // Container project
    ":feature", // Container project
    ":library", // Container project
    ":library:datasource" // Container project
)
dependencies {
    detektPlugins(libs.detekt.compose.rules)
    detektPlugins(libs.detekt.formatting)
    subprojects.forEach { subproject ->
        if (!excludeProjects.contains(subproject.path)) {
            kover(subproject)
        }
    }
}

subprojects {
    if (!excludeProjects.contains(path)) {
        afterEvaluate {
            if (
                plugins.hasPlugin("android") ||
                plugins.hasPlugin("android-library") ||
                plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")
            ) {
                pluginManager.apply(libs.plugins.kover.get().pluginId)

                configure<KoverProjectExtension> {
                    currentProject {
                        createVariant("merged") {
                            sources {
                                excludeJava = true
                            }
                            addWithDependencies("debug")
                        }
                    }
                }
            }
            if (plugins.hasPlugin("android") || plugins.hasPlugin("android-library")) {
                configure<com.android.build.gradle.BaseExtension> {
                    compileOptions {
                        sourceCompatibility = JavaVersion.VERSION_21
                        targetCompatibility = JavaVersion.VERSION_21
                    }
                    testOptions {
                        unitTests {
                            all {
                                it.ignoreFailures = true
                                it.useJUnitPlatform()
                            }
                        }
                    }
                }
            }
            if (plugins.hasPlugin("io.kotest.multiplatform")) {
                tasks.named<Test>("jvmTest") {
                    useJUnitPlatform()
                    filter {
                        isFailOnNoMatchingTests = false
                    }
                    testLogging {
                        showExceptions = true
                        showStandardStreams = true
                        events = setOf(
                            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
                        )
                        exceptionFormat =
                            org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    }
                }
            }
        }
    }
}

kover {
    currentProject {
        createVariant("merged") {}
    }
    reports {
        variant("merged") {
            filters {
                excludes {
                    androidGeneratedClasses()
                    annotatedBy("androidx.compose.runtime.Composable")
                    classes(
                        "*Activity",
                        "*BuildConfig",
                        "*Fragment",
                        "*Graph",
                        "*Module*",
                        "*Pane*",
                        "*Screen*"
                    )
                    packages(
                        "com.chesire.nekomp.core.ui"
                    )
                }
            }
        }
    }
}
