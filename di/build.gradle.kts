import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ksp)
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
            baseName = "di"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(projects.core.database)
            implementation(projects.feature.login)
            implementation(projects.library.datasource.auth)
            implementation(projects.library.datasource.library)
            implementation(projects.library.datasource.user)
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
        }
        iosMain.dependencies {
        }
    }
}

android {
    namespace = "com.chesire.nekomp.di"
    compileSdk = 35
    defaultConfig {
        minSdk = 27
    }
}
