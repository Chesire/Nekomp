import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
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
            baseName = "librarydatasourceauth"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidUnitTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.core.preferences)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.bundles.ktorfit)
            implementation(libs.koin.core)
            api(libs.kotlin.result)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.touchlab.kermit)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kotest)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
    }
}

dependencies {
    with(libs.ktorfit.ksp) {
        add("kspAndroid", this)
        add("kspCommonMainMetadata", this)
        add("kspIosArm64", this)
        add("kspIosSimulatorArm64", this)
        add("kspIosX64", this)
        add("kspJvm", this)
    }
}

android {
    namespace = "com.chesire.nekomp.library.datasource.auth"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
