plugins {
    id("io.freefair.lombok") version "6.0.0-m2"
    kotlin("plugin.lombok") version "1.6.10"

    kotlin("jvm")
//    id("kotlin-multiplatform")
}

dependencies {
    implementation(project(":annotation-processor"))
    annotationProcessor(project(":annotation-processor"))

    implementation(project(":perfORM"))

    implementation("org.apache.logging.log4j:log4j-api:2.17.1")

    implementation("org.apache.commons:commons-csv:1.9.0")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

}
//
//kotlin {
//    jvm("jvm") {
//        withJava()
//
//        val main by compilations.getting {
//            kotlinOptions.jvmTarget = "11"
//        }
//    }
//
//    sourceSets {
//        named("jvmMain") {
//            kotlin.srcDirs("src/jvmMain/java")
//            kotlin.srcDirs("${buildDir}/generated/source/kapt/main/")
//
//            dependencies {
//                val deps = configurations["kapt"].dependencies
//                deps.add(project(":annotation-processor"))
//                deps.add(
//                    DefaultExternalModuleDependency("org.projectlombok", "lombok", "1.18.22")
//                )
//                deps.add(
//                    DefaultExternalModuleDependency("org.apache.logging.log4j", "log4j-core", "2.17.1")
//                )
//                implementation(project(":annotation-processor"))
//                implementation(project(":perfORM"))
//                implementation("org.apache.logging.log4j:log4j-api:2.17.1")
//                implementation("org.apache.commons:commons-csv:1.9.0")
//                implementation("org.projectlombok:lombok:1.18.22")
////                    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
////                    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
//            }
//        }
//    }
//}