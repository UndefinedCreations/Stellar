plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

subprojects {
    repositories {
        mavenCentral()
        maven {
            name = "paper-repo"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }
}