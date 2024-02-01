import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.buildConfig)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.napier)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.moko.mvvm)
            implementation(libs.kottie)
            implementation(libs.ksend)
            api(libs.precompose.navigation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.activityCompose)
            implementation(libs.compose.uitooling)
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
        }

        iosMain.dependencies {
        }

    }
}

android {
    namespace = "org.ncgroup.mymail"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        applicationId = "org.ncgroup.mymail.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
        resources.srcDirs("src/commonMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.ncgroup.mymail.desktopApp"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}


buildConfig {
    buildConfigField(type = "String", name = "API_KEY", value = "\"${loadProperties("local.properties").getProperty("API_KEY")}\"")
    buildConfigField(type = "String", name = "ACCOUNTSID", value =  "\"${loadProperties("local.properties").getProperty("ACCOUNTSID")}\"")
    buildConfigField(type = "String", name = "AUTHTOKEN", value =  "\"${loadProperties("local.properties").getProperty("AUTHTOKEN")}\"")
    buildConfigField(type = "String", name = "SENDER_EMAIL_ADDRESS", value =  "\"${loadProperties("local.properties").getProperty("SENDER_EMAIL_ADDRESS")}\"")
    buildConfigField(type = "String", name = "SENDER_PHONE_NUMBER", value =  "\"${loadProperties("local.properties").getProperty("SENDER_PHONE_NUMBER")}\"")
}