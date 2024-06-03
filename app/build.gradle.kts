import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("kotlinx-serialization")
}

val releaseApiKey: String = gradleLocalProperties(rootDir).getProperty("RELEASE_API_KEY")
android {
    namespace = "com.oguzdogdu.walliescompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oguzdogdu.walliescompose"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        getByName("release") {
            buildConfigField ("String", "RELEASE_API_KEY", releaseApiKey)
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            buildConfigField ("String", "RELEASE_API_KEY", releaseApiKey)
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeCompiler {
        enableStrongSkippingMode = true
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
    }
}

dependencies {
    implementation (libs.accompanist.swiperefresh)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.animation)
    implementation(libs.androidx.foundation)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation (libs.androidx.material3.window.size)
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.navigation.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    androidTestImplementation(libs.hilt.android.testing)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)
    implementation (libs.library)
    implementation (libs.gson)

    implementation (libs.androidx.paging.runtime)
    testImplementation (libs.androidx.paging.common)
    implementation (libs.androidx.paging.compose)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.compose.coil)

    implementation(libs.accompanist.permissions)
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.compose)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation (libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)

    implementation (libs.androidx.navigation.compose)
    implementation(libs.lottie.compose)

    implementation (libs.androidx.glance.appwidget)
    implementation(libs.glance.material3)
    implementation (libs.androidx.work.runtime.ktx)

    implementation (libs.play.services.auth)
    implementation (libs.firebase.auth.ktx)
    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.firestore.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.navigation.compose)

    api(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.balloon.compose)
    implementation(libs.kotlinx.serialization.json)
}