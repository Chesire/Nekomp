import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "coreext"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidUnitTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.preferences)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kotest)
            implementation(libs.koin.test)
        }
        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
    }
}

android {
    namespace = "com.chesire.nekomp.core.ext"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
