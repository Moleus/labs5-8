plugins {
    id("kollector-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation("org.jline:jline:3.21.0")
                implementation(project(Module.common))
                implementation(project(Module.Feature.auth))

                implementation(Module.Shared.shared)
                implementation(Module.Shared.annotation_processor)
                implementation(Module.Shared.perform)
            }
        }
    }
}
