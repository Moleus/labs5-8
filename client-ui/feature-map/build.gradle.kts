plugins {
    id("kollector-component-mvi")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))

                implementation(Module.Shared.shared)
                implementation(Module.Shared.annotation_processor)
                implementation(project(Module.utils))
            }
        }
    }
}
