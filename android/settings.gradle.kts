pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "GreenTest"
include(":app")
include(":hash-function")
include(":features:feature-applications-list")
include(":repository:repository-impl")
include(":repository:repository-api")
include(":ui-common")
include(":cache:cache-api")
include(":cache:cache-impl")
include(":features:feature-app-details")
