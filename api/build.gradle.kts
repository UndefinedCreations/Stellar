plugins {
    `java-library`
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val versionVar = version.toString()
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

group = groupIdVar
version = versionVar

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    implementation(project(":common"))
    implementation(project(":v1_20"))
    implementation(project(":v1_20_1"))
    implementation(project(":v1_20_2"))
    implementation(project(":v1_20_4"))
    implementation(project(":v1_20_6"))
    implementation(project(":v1_21"))
    implementation(project(":v1_21_1"))
    implementation(project(":v1_21_3"))
    implementation(project(":v1_21_4"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

kotlin {
    jvmToolchain(17)
}