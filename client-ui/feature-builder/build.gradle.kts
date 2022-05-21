plugins {
    id("kollector-component-mvi")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(Module.domain))
                implementation(project(Module.utils))
                implementation(project(Module.Data.local))
                implementation(Module.Shared.shared)
                implementation(Module.Shared.annotation_processor)
                implementation(Module.Shared.perform)
            }
        }
    }
}
