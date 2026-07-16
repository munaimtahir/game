pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "OfflineMiniArcade"

include(
    ":app",
    ":core:model",
    ":core:data",
    ":core:common",
    ":core:ui",
    ":feature:home",
    ":feature:challenges",
    ":feature:stats",
    ":feature:settings",
    ":feature:marketplace",
    ":game:pulseorbit",
    ":game:lanedrift",
    ":game:stackdrop"
)
