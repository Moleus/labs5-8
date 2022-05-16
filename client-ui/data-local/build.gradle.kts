plugins {
    id("kollector-multiplatform")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
//                implementation(Libs.Kodein.common)
            }
        }
    }
}