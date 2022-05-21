plugins {
    id("kollector-component-mvi")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Module.Shared.shared)

                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))
                implementation(project(Module.Feature.builder))
                implementation(project(Module.utils))
                implementation(project(Module.domain))
            }
        }
    }
}
