rootProject.name = "client-ui"

include(
    ":app-android",
    ":app-desktop",
    ":feature-root",
    ":feature-connection",
    ":feature-main",
    ":feature-auth",
    ":feature-map",
    ":feature-builder",
    ":feature-overview",

    ":data-local",
    ":data-remote",

    ":ui-compose",

    ":domain",
    ":common",
    ":utils",
)

includeBuild("../shared-module")
