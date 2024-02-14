import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.buildConfig)
}

kotlin {

    androidTarget()

    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.napier)
            implementation(libs.precompose.viewmodel)
            implementation(libs.generativeai)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }

    }

}

android {
    namespace = "org.ncgroup.versereach"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

buildConfig {
    buildConfigField(
        type = "String",
        name = "GEMINI_API_KEY",
        value = "\"${properties.getProperty("GEMINI_API_KEY")}\""
    )
}