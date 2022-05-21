object App {

    const val id = "ru.moleus.kollector"

    object Version {
        const val code = 1
        const val name = "1.0"
    }
}

object Sdk {
    object Version {
        const val min = 26
        const val target = 31
        const val compile = 31
        const val buildTools = "33.0.0-rc4"
    }
}

object Module {

    const val utils = ":utils"

    const val domain = ":domain"

    const val common = ":common"

    object Feature {
        const val root = ":feature-root"
        const val main = ":feature-main"
        const val auth = ":feature-auth"
        const val map = ":feature-map"
        const val overview = ":feature-overview"
        const val builder = ":feature-builder"
    }

    object Shared {
        const val shared: String = "shared-module:shared"
        const val annotation_processor: String = "shared-module:annotation-processor"
        const val perform: String = "shared-module:perfORM"
    }

    object Data {
        const val local = ":data-local"
        const val remote: String = ":data-remote"
    }

    object UI {
        const val compose = ":ui-compose"
    }
}

object Libs {

    // Idea has 2020.03 Android plugin which only supports AGP 7.0
    //const val gradle = "com.android.tools.build:gradle:7.2.0"
    const val gradle = "com.android.tools.build:gradle:7.0.2"
//    const val gradle = "com.android.tools.build:gradle:4.2.1"

    object Kotlin {
        const val version = "1.6.21"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val jvmGradlePlugin = "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:$version"

        object Serialization {
            const val common = "org.jetbrains.kotlin:kotlin-serialization:$version"
            const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1"
        }
    }

    object Badoo {
        private const val version = "1.2.1"
        const val reaktive = "com.badoo.reaktive:reaktive:$version"
    }

    object ArkIvanov {

        object MVIKotlin {
            private const val version = "3.0.0-beta02"
            const val common = "com.arkivanov.mvikotlin:mvikotlin:$version"
            const val main = "com.arkivanov.mvikotlin:mvikotlin-main:$version"
            const val reaktive = "com.arkivanov.mvikotlin:mvikotlin-extensions-reaktive:$version"
            const val rx = "com.arkivanov.mvikotlin:rx:$version"
        }

        object Decompose {
            private const val version = "0.6.0"
            const val common = "com.arkivanov.decompose:decompose:$version"
            const val jvm = "com.arkivanov.decompose:decompose-jvm:$version"
            const val compose = "com.arkivanov.decompose:extensions-compose-jetbrains:$version"
        }

        object Essenty {
            private const val version = "0.3.1"
            const val instanceKeeper = "com.arkivanov.essenty:instance-keeper:$version"
            const val parcelable = "com.arkivanov.essenty:parcelable:$version"
        }
    }

    object GradleVersions {
        const val version = "0.39.0"
        const val plugin = "com.github.ben-manes.versions"
    }

    object AndroidX {

        const val core = "androidx.core:core-ktx:1.7.0"
        const val appCompat = "androidx.appcompat:appcompat:1.4.0"

        object Compose {
//            const val version = "1.1.1"
            const val version = "1.2.0-alpha01-dev686"
            const val activity = "androidx.activity:activity-compose:1.3.0"
        }
    }
}
