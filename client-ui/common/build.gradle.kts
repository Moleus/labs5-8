plugins {
    id("kollector-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Essenty.instanceKeeper)
            }
        }
    }
}
//import org.jetbrains.compose.compose
//
//plugins {
//    id("com.android.library")
//    kotlin("multiplatform")
//    id("org.jetbrains.compose")
//    id("org.jetbrains.kotlin.plugin.parcelize")
//    id("dev.icerock.mobile.multiplatform-resources")
//}
//
////multiplatformResources {
////    multiplatformResourcesPackage = "org.example.library" // required
////    multiplatformResourcesClassName = "SharedRes" // optional, default MR
////    multiplatformResourcesVisibility = MRVisibility.Internal // optional, default Public
////    multiplatformResourcesSourceSet = "commonClientMain"  // optional, default "commonMain"
////}
////
//kotlin {
//    android()
//    jvm("desktop")
//    sourceSets {
//        named("commonMain") {
//            dependencies {
//                implementation(compose.runtime)
//                implementation(compose.foundation)
//                implementation(compose.material)
//                implementation(compose.ui)
//
//                implementation("dev.icerock.moko:resources:0.20.0")
//                implementation("dev.icerock.moko:resources-compose:0.20.0")
//
//                implementation("dev.icerock.moko:resources-generator:0.20.0")
//                implementation("org.jetbrains.compose.material:material-icons-extended:1.1.1")
//                implementation("com.arkivanov.mvikotlin:rx:3.0.0-beta02")
//                implementation("com.badoo.reaktive:reaktive:1.2.1")
//                implementation("com.arkivanov.mvikotlin:mvikotlin:3.0.0-beta02")
//                implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0-beta02")
//                implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-reaktive:3.0.0-beta02")
//
//                implementation("com.arkivanov.decompose:decompose:0.6.0")
//                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.6.0")
//            }
//        }
//    }
//}
//
//android {
//    compileSdk = 31
//
//    defaultConfig {
//        minSdk = 21
//        targetSdk = 31
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//
//    sourceSets {
//        named("main") {
//            manifest.srcFile("src/androidMain/AndroidManifest.xml")
////            res.srcDirs("src/androidMain/res")
//        }
//    }
//}
