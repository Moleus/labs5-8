//plugins {
//    id("java")
////    id("application")
//    id("io.freefair.lombok") version "6.0.0-m2"
//    id("com.github.johnrengelman.shadow") version "5.2.0"
//}
//
//repositories {
//    mavenCentral()
//}
//
//dependencies {
//    implementation(project(":shared"))
//
//    implementation("org.jline:jline:3.21.0")
//
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
//
//    annotationProcessor("org.projectlombok:lombok:1.18.22")
//    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")
//}
//
//mainClassName = ""
//application {
//    mainClass = "app.ClientMain"
//}
//
//jar {
//    manifest {
//        attributes "Main-Class": "app.ClientMain"
//    }
//}
//

//test {
//    useJUnitPlatform()
//}
//
//run {
//    standardInput = System.`in`
//}
//
//application {
//    mainClass = "app.ClientMain"
//}
//
//compileKotlin {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}
//compileTestKotlin {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}