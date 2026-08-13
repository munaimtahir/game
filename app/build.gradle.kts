plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
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
val admobAppId = providers.environmentVariable("ADMOB_APP_ID").orNull.orEmpty()
val admobBannerAdUnitId = providers.environmentVariable("ADMOB_BANNER_AD_UNIT_ID").orNull.orEmpty()
val admobInterstitialAdUnitId = providers.environmentVariable("ADMOB_INTERSTITIAL_AD_UNIT_ID").orNull.orEmpty()
val admobRewardedAdUnitId = providers.environmentVariable("ADMOB_REWARDED_AD_UNIT_ID").orNull.orEmpty()
val premiumProductId = providers.environmentVariable("PLAY_PREMIUM_PRODUCT_ID").orNull ?: "premium_lifetime"
val umpDebugGeographyEea = providers.environmentVariable("UMP_DEBUG_GEOGRAPHY_EEA").orNull == "true"
val umpDebugTestDeviceHash = providers.environmentVariable("UMP_DEBUG_TEST_DEVICE_HASH").orNull.orEmpty()
val hasReleaseSigning =
    !releaseStoreFile.isNullOrBlank() &&
        !releaseStorePassword.isNullOrBlank() &&
        !releaseKeyAlias.isNullOrBlank() &&
        !releaseKeyPassword.isNullOrBlank()

android {
    namespace = "com.vexel.arcadetrio"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.vexel.arcadetrio"
        minSdk = 24
        targetSdk = 36
        versionCode = 17
        versionName = "1.1.6"
        manifestPlaceholders["resolvedAdMobAppId"] = admobAppId
        buildConfigField("String", "ADMOB_APP_ID", "\"$admobAppId\"")
        buildConfigField("String", "ADMOB_BANNER_AD_UNIT_ID", "\"$admobBannerAdUnitId\"")
        buildConfigField("String", "ADMOB_INTERSTITIAL_AD_UNIT_ID", "\"$admobInterstitialAdUnitId\"")
        buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"$admobRewardedAdUnitId\"")
        buildConfigField("String", "PLAY_PREMIUM_PRODUCT_ID", "\"$premiumProductId\"")
        buildConfigField("boolean", "UMP_DEBUG_GEOGRAPHY_EEA", umpDebugGeographyEea.toString())
        buildConfigField("String", "UMP_DEBUG_TEST_DEVICE_HASH", "\"$umpDebugTestDeviceHash\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
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
        getByName("debug") {
            manifestPlaceholders["resolvedAdMobAppId"] = "ca-app-pub-3940256099942544~3347511713"
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-3940256099942544~3347511713\"")
            buildConfigField("String", "ADMOB_BANNER_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/9214589741\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "ADMOB_REWARDED_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/5224354917\"")
            buildConfigField("boolean", "UMP_DEBUG_GEOGRAPHY_EEA", umpDebugGeographyEea.toString())
            buildConfigField("String", "UMP_DEBUG_TEST_DEVICE_HASH", "\"$umpDebugTestDeviceHash\"")
        }
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

if (releaseTaskRequested) {
    val productionIds = listOf(
        "ADMOB_APP_ID" to admobAppId,
        "ADMOB_BANNER_AD_UNIT_ID" to admobBannerAdUnitId,
        "ADMOB_INTERSTITIAL_AD_UNIT_ID" to admobInterstitialAdUnitId,
        "ADMOB_REWARDED_AD_UNIT_ID" to admobRewardedAdUnitId,
    )
    val missingProductionIds = productionIds.filter { it.second.isBlank() }.map { it.first }
    if (missingProductionIds.isNotEmpty()) {
        throw GradleException(
            "Production AdMob identifiers are required for release builds: ${missingProductionIds.joinToString()}. " +
                "Set the corresponding environment variables."
        )
    }
    if (productionIds.any { it.second.contains("3940256099942544") }) {
        throw GradleException(
            "Release builds must not use Google sample AdMob IDs. Set production AdMob identifiers."
        )
    }
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
    implementation(libs.google.play.billing)
    implementation(libs.google.play.services.ads)
    implementation(libs.google.user.messaging.platform)

    debugImplementation(libs.androidx.compose.tooling)
    debugImplementation(libs.androidx.tracing)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.robolectric)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.test.junit4)
    androidTestImplementation(libs.androidx.tracing)
    debugImplementation(libs.androidx.compose.test.manifest)
}
