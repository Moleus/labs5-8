plugins {
    id("kollector-component-mvi")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(Module.domain))
                implementation(project(Module.utils))
            }
        }
    }
}
