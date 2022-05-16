plugins {
    id("com.android.library")
}

android {

    compileSdk = Sdk.Version.compile
    buildToolsVersion = Sdk.Version.buildTools

    defaultConfig {
        minSdk = Sdk.Version.min
        targetSdk = Sdk.Version.target
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
