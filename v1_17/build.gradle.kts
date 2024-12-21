plugins {
    kotlin("jvm") version "1.9.22"
    id("com.undefinedcreation.mapper") version "0.0.4"
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.17-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly(project(":common"))
}

tasks {
    jar {
        finalizedBy(remap)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }
    remap {
        mcVersion.set("1.17")
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(16)
}