plugins {
    id("kollector-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(Module.common))
                implementation(project(Module.Feature.auth))

                implementation(Module.Shared.shared)
                implementation(Module.Shared.annotation_processor)
                implementation(Module.Shared.perform)

                implementation(Libs.ArkIvanov.Decompose.common)
            }
        }
    }
}
