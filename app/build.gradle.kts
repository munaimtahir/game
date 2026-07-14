plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

import java.util.Properties
import org.gradle.api.GradleException

val releaseKeyProperties = Properties().apply {
    val propsFile = rootProject.file("key.properties")
    if (propsFile.exists()) {
        propsFile.inputStream().use(::load)
    }
}

fun releaseProp(name: String): String? =
    providers.environmentVariable(name).orNull
        ?: releaseKeyProperties.getProperty(name)

val releaseStoreFile = releaseProp("storeFile")
val releaseStorePassword = releaseProp("storePassword")
val releaseKeyAlias = releaseProp("keyAlias")
val releaseKeyPassword = releaseProp("keyPassword")
val hasReleaseSigning =
    !releaseStoreFile.isNullOrBlank() &&
        !releaseStorePassword.isNullOrBlank() &&
        !releaseKeyAlias.isNullOrBlank() &&
        !releaseKeyPassword.isNullOrBlank()

android {
    namespace = "com.vexel.arcadetrio"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vexel.arcadetrio"
        minSdk = 24
        targetSdk = 35
        versionCode = 13
        versionName = "1.1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = rootProject.file(requireNotNull(releaseStoreFile))
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            ndk {
                debugSymbolLevel = "symbol_table"
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
}

tasks.withType<org.gradle.api.tasks.testing.Test>().configureEach {
    // Robolectric + Compose can be memory-hungry; keep unit tests stable in CI/dev machines.
    maxHeapSize = "2g"

    // Work around occasional missing-dir failures when the test process aborts early.
    doFirst {
        file("$buildDir/test-results/$name").mkdirs()
        file("$buildDir/test-results/$name/binary").mkdirs()
    }
}

val requestedTasks = gradle.startParameter.taskNames
val releaseTaskRequested = requestedTasks.any { task ->
    val normalized = task.lowercase()
    normalized.contains("assemblerelease") ||
        normalized.contains("bundlerelease") ||
        normalized.contains("packagerelease") ||
        normalized.contains("publish")
}

if (releaseTaskRequested && !hasReleaseSigning) {
    throw GradleException(
        "Release signing is required for release/bundle tasks. " +
            "Set storeFile/storePassword/keyAlias/keyPassword via key.properties " +
            "or environment variables."
    )
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":feature:home"))
    implementation(project(":feature:challenges"))
    implementation(project(":feature:stats"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:marketplace"))
    implementation(project(":game:pulseorbit"))
    implementation(project(":game:lanedrift"))
    implementation(project(":game:stackdrop"))
    implementation(project(":game:brickvolley"))
    implementation(project(":game:loopsnake"))
    implementation(project(":game:shielddash"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.tooling.preview)
    implementation(libs.androidx.room.runtime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.google.material)

    debugImplementation(libs.androidx.compose.tooling)
    debugImplementation(libs.androidx.tracing)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.robolectric)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.test.junit4)
    androidTestImplementation(libs.androidx.tracing)
    debugImplementation(libs.androidx.compose.test.manifest)
}
