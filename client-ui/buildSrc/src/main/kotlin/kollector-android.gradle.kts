import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("kotlin-parcelize")
}

android {
    compileSdk = Sdk.Version.compile

    defaultConfig {
        minSdk = Sdk.Version.min
        targetSdk = Sdk.Version.target
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
//            java.srcDirs("src/main/java") // << that's what makes Android target recognize Java
        }
    }
}