import org.jetbrains.compose.compose

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

dependencies {
    implementation(Module.Shared.shared)

    implementation(project(Module.Feature.root))
    implementation(project(Module.UI.compose))
    implementation(project(Module.domain))

    implementation(compose.desktop.currentOs)

    implementation(Libs.ArkIvanov.Decompose.common)
    implementation(Libs.ArkIvanov.Decompose.compose)
}

compose.desktop {
    application {
        mainClass = "ru.moleus.kollector.MainKt"
    }
}