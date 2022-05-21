import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(Libs.Kotlin.gradlePlugin)
//    implementation(Libs.Kotlin.jvmGradlePlugin)
    implementation(Libs.gradle)
}

kotlin {
    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

