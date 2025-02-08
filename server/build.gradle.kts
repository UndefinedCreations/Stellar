import com.undefinedcreations.runServer.ServerType

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.undefinedcreations.runServer") version "0.1.6"
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

val versionVar = version
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    implementation(project(":spigot:api"))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("Stellar-shadow.jar")
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
    compileJava {
        options.release = 21
    }
    runServer {
        minecraftVersion("1.21.1")
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