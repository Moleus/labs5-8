import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Libs.GradleVersions.plugin) version Libs.GradleVersions.version
}

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
    dependencies {
//        classpath(Libs.gradle)
//        classpath("com.android.tools.build:gradle:7.0.0")
        classpath(Libs.Kotlin.gradlePlugin)
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}
