pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://repository.zmkn.com/repository/maven-public/")
        }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "public-kotlin"
include(":util-kotlin", ":model-kotlin", ":module-kotlin", ":service-kotlin", ":constant-kotlin", ":extension-kotlin", ":enumeration-kotlin")
project(":util-kotlin").name = "util-kotlin"
project(":model-kotlin").name = "model-kotlin"
project(":module-kotlin").name = "module-kotlin"
project(":service-kotlin").name = "service-kotlin"
project(":constant-kotlin").name = "constant-kotlin"
project(":extension-kotlin").name = "extension-kotlin"
project(":enumeration-kotlin").name = "enumeration-kotlin"
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
