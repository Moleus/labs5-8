plugins {
    id("kollector-component-mvi")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
                implementation(project(Module.Data.local))
                implementation(project(Module.Data.remote))

                implementation(project(Module.domain))
                implementation(project(Module.Feature.map))
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.overview))
                implementation(project(Module.Feature.builder))

                implementation(Module.Shared.shared)
            }
        }
    }
}
