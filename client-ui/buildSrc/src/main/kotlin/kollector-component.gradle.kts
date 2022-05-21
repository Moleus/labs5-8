plugins {
    id("kollector-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(project(Module.common))
            }
        }
    }
}
