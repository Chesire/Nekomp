import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
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
            baseName = "featurelogin"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.compose.material3)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.core.resources)
            implementation(projects.library.datasource.auth)
            implementation(projects.library.datasource.favorite)
            implementation(projects.library.datasource.library)
            implementation(projects.library.datasource.stats)
            implementation(projects.library.datasource.trending)
            implementation(projects.library.datasource.user)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)
            implementation(libs.touchlab.kermit)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.chesire.nekomp.feature.login"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
