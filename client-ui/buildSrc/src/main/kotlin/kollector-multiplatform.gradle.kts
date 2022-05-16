import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("kollector-android")
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {

        val androidAndroidTestRelease by getting
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
