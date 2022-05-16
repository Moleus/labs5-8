rootProject.name = "Kollector"

include(
    ":app-android",
    ":app-desktop",
    ":common",
    ":feature-root",
    ":feature-main",
    ":feature-auth",
    ":feature-map",
    ":feature-builder",
    ":feature-overview",

    ":data-local",
    ":data-remote",

    ":ui-compose",
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

//    plugins {
//        val kotlinVersion = extra["kotlin.version"] as String
//        val agpVersion = extra["agp.version"] as String
//        val composeVersion = extra["compose.version"] as String
//
//        kotlin("jvm").version(kotlinVersion)
//        kotlin("multiplatform").version(kotlinVersion)
//        kotlin("android").version(kotlinVersion)
//        id("org.jetbrains.kotlin.plugin.parcelize").version(kotlinVersion)
//        id("org.jetbrains.kotlin.kotlin-gradle-plugin").version(kotlinVersion)
//        id("com.android.application").version(agpVersion)
//        id("com.android.library").version(agpVersion)
//        id("org.jetbrains.compose").version(composeVersion)
//    }
}
include("feature-auth")
