pluginManagement {
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
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/") }
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "beginvegan"
include(":app")
include(":presentation")
include(":data")
include(":domain")
include(":core-fcm")
