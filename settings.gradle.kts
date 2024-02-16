rootProject.name = "VerseReach"
include(":androidApp")
include(":shared")
include(":desktopApp")
include(":webApp")
include(":webApp-wasm")
include(":wearApp")
include(":common-ksend")
include(":common-gemini")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}
