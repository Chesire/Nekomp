import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
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
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Nekomp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.workmanager)
            implementation(libs.kotlinx.coroutines.android)
        }
        androidUnitTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
        commonMain.dependencies {
            implementation(projects.core.coroutines)
            implementation(projects.core.database)
            implementation(projects.core.network)
            implementation(projects.core.preferences)
            implementation(projects.core.resources)
            implementation(projects.core.ui)
            implementation(projects.feature.airing)
            implementation(projects.feature.discover)
            implementation(projects.feature.home)
            implementation(projects.feature.library)
            implementation(projects.feature.login)
            implementation(projects.feature.profile)
            implementation(projects.feature.settings)
            implementation(projects.library.datasource.airing)
            implementation(projects.library.datasource.auth)
            implementation(projects.library.datasource.favorite)
            implementation(projects.library.datasource.library)
            implementation(projects.library.datasource.mapping)
            implementation(projects.library.datasource.search)
            implementation(projects.library.datasource.stats)
            implementation(projects.library.datasource.trending)
            implementation(projects.library.datasource.user)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.materialIconsExtended)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.room.runtime)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.material3.adaptive.layout)
            implementation(libs.compose.material3.adaptive.navigation)
            implementation(libs.compose.ui.backhandler)
            implementation(libs.koin.compose)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.serialization)
            implementation(libs.touchlab.kermit)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kotest)
            implementation(libs.koin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
    }
}

android {
    namespace = "com.chesire.nekomp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.chesire.nekomp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.0.1"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    lint {
        abortOnError = false
        checkDependencies = true
        xmlReport = true
    }
}

compose.desktop {
    application {
        mainClass = "com.chesire.nekomp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            modules("jdk.unsupported") // For datastore
            modules("jdk.unsupported.desktop") // For datastore
            packageName = "com.chesire.nekomp"
            packageVersion = "1.0.0"
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}

buildkonfig {
    packageName = "com.chesire.nekomp"

    // Flavored TargetConfig > TargetConfig > Flavored DefaultConfig > DefaultConfig
    // flavor can be configured in gradle.properties
    defaultConfigs {
        buildConfigField(
            type = INT,
            name = "VERSION_CODE",
            value = "${android.defaultConfig.versionCode ?: 0}"
        )
        buildConfigField(
            type = STRING,
            name = "VERSION_NAME",
            value = android.defaultConfig.versionName ?: "0.0.0"
        )
    }
}
