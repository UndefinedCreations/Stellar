plugins {
    kotlin("jvm") version "2.1.0"
}

val projectVersion = version
val projectGroupId = "com.undefined"
val projectArtifactId = "stellar"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    api("com.mojang:authlib:1.5.21")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        options.release = 8
    }
}

kotlin {
    jvmToolchain(21)
}