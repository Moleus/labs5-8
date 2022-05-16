//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//plugins {
//    id("com.android.application")
//    kotlin("android")
//    id("org.jetbrains.compose")
//}
//
//android {
//    compileSdkVersion(31)
//
//    defaultConfig {
//        minSdkVersion(23)
//        targetSdkVersion(31)
//        versionCode = 1
//        versionName = "1.0"
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//
//    packagingOptions {
//        exclude("META-INF/*")
//    }
//}
//
//group = "demo.android"
//version = "unspecified"
//
//repositories {
//    mavenCentral()
//    google()
//    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
//    maven( "https://maven.google.com" )
//}
//
//dependencies {
//    implementation(project(":common"))
//
//    implementation("com.android.tools.build:gradle:7.4.2")
//    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.6.0")
//    implementation("com.arkivanov.mvikotlin:mvikotlin:3.0.0-beta02")
//    implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0-beta02")
//    implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-reaktive:3.0.0-beta02")
//    implementation("com.arkivanov.decompose:decompose:0.6.0")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
//    implementation(kotlin("stdlib-jdk8"))
//}
//
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    jvmTarget = "1.8"
//}
