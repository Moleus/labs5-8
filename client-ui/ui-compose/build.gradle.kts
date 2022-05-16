plugins {
    id("kollector-multiplatform")
    id("org.jetbrains.compose") version Libs.AndroidX.Compose.version
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {

//                implementation(project(Module.utils))

                implementation(project(Module.Feature.root))
                implementation(project(Module.Feature.main))
                implementation(project(Module.Feature.auth))
                implementation(project(Module.Feature.map))
                implementation(project(Module.Feature.builder))

                implementation(compose.material)
                implementation(compose.materialIconsExtended)

//                implementation(Libs.Kodein.compose)
                implementation(Libs.ArkIvanov.Decompose.common)
                implementation(Libs.ArkIvanov.Decompose.compose)
            }
        }

        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
