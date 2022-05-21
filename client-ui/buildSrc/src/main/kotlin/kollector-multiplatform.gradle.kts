import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    id("kollector-android")
    kotlin("multiplatform")
}

kotlin {
//    android()  // only for android
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    } // only for desktop

    sourceSets {
//        val commonMain by getting {
//            dependencies {
//            }
//        }
//
//        val androidMain by getting {
//            dependsOn(commonMain)
//        }

        val desktopMain by getting {
            // IMPORTANT for intellij indexing JDK (import java.*) and project dependencies
            kotlin.srcDir("src/commonMain/kotlin")
        }
//
//        val androidAndroidTestRelease by getting
//        val androidTest by getting {
//            dependsOn(androidAndroidTestRelease)
//        }
    }
}
