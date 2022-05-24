plugins {
    id("io.freefair.lombok") version "6.0.0-m2"
    kotlin("plugin.lombok") version "1.6.10"
    kotlin("jvm")
}

dependencies {
    // FIXME: android complains about Collectible class in build/ 'multiple dex files define'.
    // Commenting this fixes the issue.
//    implementation(fileTree(mapOf("dir" to "build/libs", "include" to listOf("*.jar"))))
    implementation(project(":perfORM"))

    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("org.projectlombok:lombok")

    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    annotationProcessor("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.apache.commons:commons-csv:1.9.0")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.apache.commons:commons-text:1.9")

    implementation("com.squareup:javapoet:1.13.0")

    compileOnly("com.google.auto.service:auto-service:1.0.1")
    annotationProcessor("com.google.auto.service:auto-service:1.0.1")
}


//kotlin {
//    jvm() {
//        withJava()
//    }
//
//    sourceSets {
//        named("jvmMain") {
//            kotlin.srcDirs("src/jvmMain/java")
//            dependencies {
//                implementation(fileTree(mapOf("dir" to "build/libs", "include" to listOf("*.jar"))))
//                implementation(project(":perfORM"))
//
//                implementation("com.squareup:javapoet:1.13.0")
//
////                    annotationProcessor("org.projectlombok:lombok:1.18.22")
//                implementation("org.projectlombok:lombok:1.18.22")
//
////                    annotationProcessor("com.google.auto.service:auto-service:1.0.1")
//                implementation("com.google.auto.service:auto-service:1.0.1")
//            }
//        }
//    }
//}
