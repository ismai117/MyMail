import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.buildConfig)
}

android {
    namespace = "org.ncgroup.versereach"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.ncgroup.versereach.webApp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activityCompose)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.uitooling)
    implementation(libs.wear.foundation)
    implementation(libs.wear.material)
    implementation(libs.horologist.compose.material)
    implementation(libs.horologist.compose.layout)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation(libs.precompose.navigation)
    implementation(libs.precompose.viewmodel)
    implementation(libs.kottie)
    implementation(libs.generativeai)
    implementation(project(":common-ksend"))
    implementation(project(":common-gemini"))
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