plugins {
    id("kollector-component")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Libs.ArkIvanov.MVIKotlin.main)
                implementation(Libs.ArkIvanov.MVIKotlin.common)
                implementation(Libs.ArkIvanov.MVIKotlin.reaktive)
                implementation(Libs.ArkIvanov.MVIKotlin.rx)

                implementation(Libs.Badoo.reaktive)
            }
        }
    }
}
