import org.jetbrains.compose.compose

//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

android {
    defaultConfig {
        applicationId = App.id
        versionCode = App.Version.code
        versionName = App.Version.name

        minSdk = Sdk.Version.min
        targetSdk = Sdk.Version.target
    }


    kotlinOptions {
        jvmTarget = "11"
    }

    lintOptions {
        isAbortOnError = false
        isCheckReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    compileSdk = Sdk.Version.compile
    buildToolsVersion = Sdk.Version.buildTools

    packagingOptions {
        exclude("META-INF/*")
    }


    val RELEASE_STORE_FILE: String by project
    val RELEASE_STORE_PASSWORD: String by project
    val RELEASE_KEY_ALIAS: String by project
    val RELEASE_KEY_PASSWORD: String by project

    signingConfigs {
        create("release") {
            storeFile = file(RELEASE_STORE_FILE)
            storePassword = RELEASE_STORE_PASSWORD
            keyAlias = RELEASE_KEY_ALIAS
            keyPassword = RELEASE_KEY_PASSWORD
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
//    implementation(Module.Shared.shared)
//    implementation(Module.Shared.annotation_processor)

    implementation(project(Module.Feature.root))
    implementation(project(Module.domain))
    implementation(project(Module.common))
    implementation(project(Module.UI.compose))

    implementation(compose.runtime)
    implementation(compose.material)


    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.Compose.activity)

    implementation(Libs.ArkIvanov.Decompose.common)
    implementation(Libs.ArkIvanov.Decompose.compose)
}


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
