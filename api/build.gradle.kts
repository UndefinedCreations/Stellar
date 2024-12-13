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
    compileOnly(project(":v1_20_2"))
    compileOnly(project(":v1_20_4"))
    compileOnly(project(":v1_20_6"))
    compileOnly(project(":v1_21"))
    compileOnly(project(":v1_21_1"))
    compileOnly(project(":v1_21_3"))
    compileOnly(project(":v1_21_4"))
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = groupIdVar
            artifactId = artifactIdVar
            version = versionVar

            from(components["java"])
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}

kotlin {
    jvmToolchain(21)
}
