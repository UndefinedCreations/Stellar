plugins {
    kotlin("jvm") version "1.9.22"
}

val versionVar = version.toString()
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    api("com.mojang:authlib:1.5.21")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }
}

kotlin {
    jvmToolchain(16)
}
