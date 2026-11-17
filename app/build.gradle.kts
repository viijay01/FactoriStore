plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Values passed by generate.js (-PappName=... -PstoreUrl=... -PappSlug=...)
val appName = (project.findProperty("appName") as String?) ?: "Zolara"
val storeUrl = (project.findProperty("storeUrl") as String?) ?: "https://zolara.in"
val appSlug = (project.findProperty("appSlug") as String?) ?: "zolara"

android {
    namespace = "com.factori.store"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.factori.store.$appSlug"   // unique per store, never changes
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
        resValue("string", "app_name", appName)
        buildConfigField("String", "STORE_URL", "\"$storeUrl\"")
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
}
