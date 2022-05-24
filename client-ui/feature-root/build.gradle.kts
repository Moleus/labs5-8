plugins {
    id("kollector-component-mvi")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))
                implementation(project(Module.Feature.main))
                implementation(project(Module.Feature.connection))

                implementation(Module.Shared.shared)
                implementation(Module.Shared.annotation_processor)
            }
        }
    }
}
