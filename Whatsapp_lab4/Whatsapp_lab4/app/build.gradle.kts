plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.0.21" // Versión explícita
}

android {
    namespace = "com.example.whatsapp_lab4"
    compileSdk = 34

    defaultConfig {
        // ... (configuración existente)
    }

    buildTypes {
        release {
            // ... (configuración existente)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // Cambiado a 17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17) // JDK 17 obligatorio para Kotlin 2.0+
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {
    // ... (dependencias existentes)
    implementation(platform("androidx.compose:compose-bom:2024.02.00")) // BOM estable
}