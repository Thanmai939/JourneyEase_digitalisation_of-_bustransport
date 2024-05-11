pluginManagement {
    repositories {



        google()
        mavenCentral()
        gradlePluginPortal()

            flatDir {
                dir("libs")
            }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "JourneyEase"
include(":app")
 