plugins {
    id("kollector-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(Module.common))
                implementation(project(Module.domain))
                implementation(Module.Shared.shared)
                implementation(Module.Shared.annotation_processor)
            }
        }
    }
}