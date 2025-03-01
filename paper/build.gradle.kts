plugins {
    java
    kotlin("jvm") version "2.1.0"
    id("com.gradleup.shadow") version "8.3.5"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14" apply false
}

subprojects {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}