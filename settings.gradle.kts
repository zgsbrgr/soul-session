pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "soulsession"
include(":app")

include(":core-ui")
include(":core-model")
include(":core-common")
include(":core-testing")
include(":core-network")
include(":benchmark")
include(":core-database")
include(":core-data")
include(":feature-episodes")
include(":core-navigation")
include(":feature-episode")
include(":core-media")
include(":feature-player")
