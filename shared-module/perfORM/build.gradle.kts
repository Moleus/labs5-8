import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency

plugins {
    kotlin("jvm")

    kotlin("plugin.lombok") version "1.6.10"
    id("net.bytebuddy.byte-buddy-gradle-plugin") version "1.12.9"
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.apache.commons:commons-text:1.9")

    implementation("org.reflections:reflections:0.10.2")
    implementation("net.bytebuddy:byte-buddy:1.12.9")
    runtimeOnly("org.postgresql:postgresql:42.3.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

//kotlin {
//    jvm() {
//        withJava()
//    }
//
//    sourceSets {
//        named("jvmMain") {
//            kotlin.srcDirs("src/jvmMain/java")
//
//            dependencies {
//                val deps = configurations["kapt"].dependencies
//                deps.add(
//                    DefaultExternalModuleDependency(
//                        "org.projectlombok", "lombok", "1.18.22"
//                    )
//                )
//                deps.add(
//                    DefaultExternalModuleDependency(
//                        "org.apache.logging.log4j", "log4j-core", "2.17.1"
//                    )
//                )
//
//                implementation("org.apache.logging.log4j:log4j-core:2.17.1")
//                implementation("org.apache.logging.log4j:log4j-api:2.17.1")
//                implementation("org.reflections:reflections:0.10.2")
//                implementation("org.apache.commons:commons-lang3:3.0")
//                implementation("org.apache.commons:commons-text:1.9")
//                implementation("net.bytebuddy:byte-buddy:1.12.9")
//                runtimeOnly("org.postgresql:postgresql:42.3.4")
////                    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
////                    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
////                    testImplementation("org.assertj:assertj-core:3.11.1")
//            }
//        }
//    }
//}