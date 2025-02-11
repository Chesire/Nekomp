import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
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
            baseName = "preferences"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
        }
        commonMain.dependencies {
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
        }
        iosMain.dependencies {
        }
    }
}

android {
    namespace = "com.chesire.nekomp.core.preferences"
    compileSdk = 35
    defaultConfig {
        minSdk = 27
    }
}
