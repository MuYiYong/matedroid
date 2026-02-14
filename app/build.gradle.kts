import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

// Room schema export location for migrations
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.matedroid"
    compileSdk = 36

    flavorDimensions += "store"

    defaultConfig {
        applicationId = "com.matedroid"
        minSdk = 28
        targetSdk = 36
        versionCode = 34
        versionName = "1.2.0"

        val amapApiKey =
            (project.findProperty("AMAP_API_KEY") as? String)
                ?: localProperties.getProperty("AMAP_API_KEY", "")

        val amapWebApiKey =
            (project.findProperty("AMAP_WEB_API_KEY") as? String)
                ?: localProperties.getProperty("AMAP_WEB_API_KEY", "")

        val amapGeocodingStrictCn =
            (project.findProperty("AMAP_GEOCODING_STRICT_CN") as? String)
                ?.toBooleanStrictOrNull()
                ?: localProperties.getProperty("AMAP_GEOCODING_STRICT_CN", "true").toBooleanStrictOrNull()
                ?: true

        val hmsAppId =
            (project.findProperty("HMS_APP_ID") as? String)
                ?: localProperties.getProperty("HMS_APP_ID", "")

        manifestPlaceholders["AMAP_API_KEY"] =
            amapApiKey

        buildConfigField("String", "AMAP_API_KEY", "\"$amapApiKey\"")
        buildConfigField("String", "AMAP_WEB_API_KEY", "\"$amapWebApiKey\"")
        buildConfigField("boolean", "AMAP_GEOCODING_STRICT_CN", amapGeocodingStrictCn.toString())
        buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"play\"")
        buildConfigField("String", "MOBILE_SERVICES_STACK", "\"gms\"")
        buildConfigField("String", "HMS_APP_ID", "\"$hmsAppId\"")
        manifestPlaceholders["DISTRIBUTION_CHANNEL"] = "play"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    productFlavors {
        create("play") {
            dimension = "store"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"play\"")
            buildConfigField("String", "MOBILE_SERVICES_STACK", "\"gms\"")
            manifestPlaceholders["DISTRIBUTION_CHANNEL"] = "play"
        }
        create("huawei") {
            dimension = "store"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"huawei\"")
            buildConfigField("String", "MOBILE_SERVICES_STACK", "\"hms\"")
            manifestPlaceholders["DISTRIBUTION_CHANNEL"] = "huawei"
        }
        create("honor") {
            dimension = "store"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"honor\"")
            buildConfigField("String", "MOBILE_SERVICES_STACK", "\"hms\"")
            manifestPlaceholders["DISTRIBUTION_CHANNEL"] = "honor"
        }
        create("xiaomi") {
            dimension = "store"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"xiaomi\"")
            buildConfigField("String", "MOBILE_SERVICES_STACK", "\"aosp\"")
            manifestPlaceholders["DISTRIBUTION_CHANNEL"] = "xiaomi"
        }
        create("oppo") {
            dimension = "store"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"oppo\"")
            buildConfigField("String", "MOBILE_SERVICES_STACK", "\"aosp\"")
            manifestPlaceholders["DISTRIBUTION_CHANNEL"] = "oppo"
        }
        create("samsung") {
            dimension = "store"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"samsung\"")
            buildConfigField("String", "MOBILE_SERVICES_STACK", "\"gms\"")
            manifestPlaceholders["DISTRIBUTION_CHANNEL"] = "samsung"
        }
        create("fdroid") {
            dimension = "store"
            buildConfigField("String", "DISTRIBUTION_CHANNEL", "\"fdroid\"")
            buildConfigField("String", "MOBILE_SERVICES_STACK", "\"aosp\"")
            manifestPlaceholders["DISTRIBUTION_CHANNEL"] = "fdroid"
        }
    }

    signingConfigs {
        create("release") {
            // CI: use secrets from environment variables (if non-empty)
            // Local/CI without secrets: fall back to debug keystore
            val keystoreBase64 = System.getenv("KEYSTORE_BASE64")?.takeIf { it.isNotEmpty() }
            val keystorePath = if (keystoreBase64 != null) "release.keystore"
                else "${System.getProperty("user.home")}/.android/debug.keystore"
            storeFile = file(keystorePath)
            storePassword = System.getenv("KEYSTORE_PASSWORD")?.takeIf { it.isNotEmpty() } ?: "android"
            keyAlias = System.getenv("KEY_ALIAS")?.takeIf { it.isNotEmpty() } ?: "androiddebugkey"
            keyPassword = System.getenv("KEY_PASSWORD")?.takeIf { it.isNotEmpty() } ?: "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "armeabi-v7a")
            isUniversalApk = true
        }
    }

    // Disable dependency metadata for F-Droid compatibility
    // This block is encrypted with Google's key and unreadable by anyone else
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    lint {
        // Treat hardcoded text as an error to enforce localization
        error += "HardcodedText"
        error += "SetTextI18n"

        // Fail the build on errors
        abortOnError = true

        // Generate reports
        htmlReport = true
        xmlReport = true
    }

    testOptions {
        unitTests.all {
            it.jvmArgs("-Xmx1024m")
        }
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    // DataStore
    implementation(libs.datastore.preferences)
    implementation(libs.security.crypto)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)

    // Charts
    implementation(libs.vico.compose.m3)

    // Maps
    implementation(libs.amap)
    add("huaweiImplementation", libs.huawei.hms.push)
    add("honorImplementation", libs.huawei.hms.push)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
