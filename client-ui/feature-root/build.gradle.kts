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
            }
        }
    }
}
