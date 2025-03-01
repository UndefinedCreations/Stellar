import com.undefinedcreations.runServer.ServerType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("com.undefinedcreations.runServer") version "0.1.6"
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

val versionVar = version
val groupIdVar = "com.undefined"

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation(project(":paper:api"))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("Stellar-shadow.jar")
    }
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }
    compileJava {
        options.release = 21
    }
    runServer {
        minecraftVersion("1.21.4")
        serverFolderName { "run" }
        acceptMojangEula()
        serverType(ServerType.PAPERMC)
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}