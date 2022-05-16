plugins {
    id("kollector-component")
}

kotlin {
    sourceSets {

        named("commonMain") {
            dependencies {
//                implementation(project(Module.utils))
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.MVIKotlin.reaktive)
                implementation(Libs.ArkIvanov.MVIKotlin.rx)
            }
        }
    }
}
