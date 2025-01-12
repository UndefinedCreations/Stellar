plugins {
    kotlin("jvm") version "1.9.22"
    id("com.undefinedcreations.mapper") version "1.1.1"
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.17.1-R0.1-SNAPSHOT:remapped-mojang")
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
        minecraftVersion("1.17.1")
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}