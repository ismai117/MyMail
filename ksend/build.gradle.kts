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

    js{
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()


    sourceSets {

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.napier)
            implementation(libs.voyagerScreenModel)
            implementation(libs.ksend)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }

        val jsMain by getting {
            dependsOn(commonMain.get())
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

compose.experimental {
    web.application {}
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

buildConfig {
    buildConfigField(
        type = "String",
        name = "API_KEY",
        value = "\"${properties.getProperty("API_KEY")}\""
    )
    buildConfigField(
        type = "String",
        name = "ACCOUNTSID",
        value = "\"${properties.getProperty("ACCOUNTSID")}\""
    )
    buildConfigField(
        type = "String",
        name = "AUTHTOKEN",
        value = "\"${properties.getProperty("AUTHTOKEN")}\""
    )
    buildConfigField(
        type = "String",
        name = "SENDER_EMAIL_ADDRESS",
        value = "\"${properties.getProperty("SENDER_EMAIL_ADDRESS")}\""
    )
    buildConfigField(
        type = "String",
        name = "SENDER_PHONE_NUMBER",
        value = "\"${properties.getProperty("SENDER_PHONE_NUMBER")}\""
    )
    buildConfigField(
        type = "String",
        name = "GEMINI_API_KEY",
        value = "\"${properties.getProperty("GEMINI_API_KEY")}\""
    )
}