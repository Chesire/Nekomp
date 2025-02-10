import io.gitlab.arturbosch.detekt.Detekt
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension

plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.androidx.room).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.ktorfit).apply(false)
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
            if (plugins.hasPlugin("android") || plugins.hasPlugin("android-library")) {
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

                configure<com.android.build.gradle.BaseExtension> {
                    compileOptions {
                        sourceCompatibility = JavaVersion.VERSION_21
                        targetCompatibility = JavaVersion.VERSION_21
                    }
                    testOptions {
                        unitTests {
                            all {
                                it.ignoreFailures = true
                            }
                        }
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
                    packages(
                        listOf()
                    )
                    annotatedBy("androidx.compose.runtime.Composable")
                    classes(
                        listOf(
                            "*Activity",
                            "*Activity\$*",
                            "*Adapter",
                            "*BuildConfig",
                            "*ComposableSingletons\$*",
                            "*Directions",
                            "*Directions\$*",
                            "*Factory",
                            "*Factory\$*",
                            "*Fragment",
                            "*Fragment\$*",
                            "*FragmentArgs",
                            "*Module",
                            "*ModuleKt",
                            "*Module\$*",
                            "*NavigationArgs",
                            "*Navigator",
                            "*NavigatorBinder",
                            "*Preview*Kt",
                            "*Request",
                            "*Response",
                            "*Screen",
                            "*Screen\$*",
                            "*ScreenKt",
                            "*ScreenKt\$*",
                            "*Sheet",
                            "*ShowkaseMetadata*",
                            "*Stub.kt",
                            "*Widget"
                        )
                    )
                }
            }
        }
    }
}
