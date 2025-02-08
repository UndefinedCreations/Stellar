plugins {
    java
    kotlin("jvm") version "1.9.22"
    id("com.gradleup.shadow") version "8.3.5"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14" apply false
}

allprojects {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}