plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.dagger.hilt.android")
    id("androidx.room")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.timmay.tarot"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.timmay.tarot"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

    dependencies {
        // Compose
        implementation(platform("androidx.compose:compose-bom:2025.11.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")
        debugImplementation("androidx.compose.ui:ui-tooling")
        implementation("androidx.compose.material3:material3:1.4.0")
        implementation("androidx.navigation:navigation-compose:2.9.6")

        // Core
        implementation("androidx.core:core-ktx:1.17.0")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
        implementation("androidx.activity:activity-compose:1.11.0")

        // Navigation
        implementation("androidx.navigation:navigation-compose:2.9.6")

        // Hilt
        implementation("com.google.dagger:hilt-android:2.57.2")
        ksp("com.google.dagger:hilt-android-compiler:2.57.2")
        implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

        // Room
        implementation("androidx.room:room-ktx:2.8.3")
        ksp("androidx.room:room-compiler:2.8.3")

        // DataStore
        implementation("androidx.datastore:datastore-preferences:1.1.7")

        // Serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    }