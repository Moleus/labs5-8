plugins {
    id("java")
    id("application")
    id("io.freefair.lombok") version "6.0.0-m2"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("shared-module:annotation-processor")
    implementation("shared-module:shared")
    implementation("shared-module:perfORM")

    implementation("org.apache.commons:commons-csv:1.9.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("at.favre.lib:bcrypt:0.9.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    annotationProcessor("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("org.modelmapper:modelmapper:3.1.0")
}

application {
    mainClassName = "app.App"
}


tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}