plugins {
    kotlin("jvm") version "1.9.22"
    id("com.undefinedcreations.mapper") version "1.1.1"
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.17-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly(project(":spigot:common"))
}

tasks {
    jar {
        finalizedBy(remap)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        options.release = 8
    }
    remap {
        minecraftVersion("1.17")
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(16)
}