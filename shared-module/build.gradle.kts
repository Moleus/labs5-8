plugins {
    kotlin("jvm") version "1.6.21"
}

dependencies {
    implementation(kotlin("stdlib"))
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        }
    }
}
