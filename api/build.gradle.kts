plugins {
    kotlin("jvm") version "1.9.22"
}

val versionVar = version
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly(project(":common"))
    compileOnly(project(":v1_20_6"))
    compileOnly(project(":v1_21"))
    compileOnly(project(":v1_21_1"))
    compileOnly(project(":v1_21_3"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}

kotlin {
    jvmToolchain(21)
}
