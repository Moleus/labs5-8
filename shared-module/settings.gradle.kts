rootProject.name = "shared-module"

include(
    ":annotation-processor",
    ":perfORM",
    ":shared"
)

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        }
    }
}
