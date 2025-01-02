plugins {
    kotlin("jvm") version "1.9.22"
    id("com.undefinedcreation.mapper") version "0.0.5"
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.21.4-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly(project(":common"))
}

tasks {
    jar {
        finalizedBy(remap)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        options.release.set(8)
    }
    remap {
        dependsOn(shadowJar)
        mcVersion.set("1.21.4")
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}