plugins {
    id("kollector-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Module.Shared.shared)
                implementation(Module.Shared.annotation_processor)
            }
        }
    }
}
