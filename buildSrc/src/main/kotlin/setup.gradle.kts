import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    kotlin("jvm")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

group = properties["group"]!!
version = properties["version"]!!

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        name = "paper-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "minecraft-repo"
        url = uri("https://libraries.minecraft.net/")
    }
    maven {
        name = "spigot-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    }
}

dependencies {
    compileOnly("net.kyori:adventure-api:4.22.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.25.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.25.0")
    compileOnly(libs.findLibrary("kotlin-coroutines").orElseThrow())
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_1_8
    }
    compileJava {
        options.release = 8
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}