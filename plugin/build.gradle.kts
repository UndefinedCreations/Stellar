import com.undefinedcreations.nova.ServerType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("setup")
    id("com.gradleup.shadow")
    id("com.undefinedcreations.nova") version "0.0.8"
    id("com.undefinedcreations.echo") version "0.0.11"
}

repositories {
    maven {
        name = "undefined-releases"
        url = uri("https://repo.undefinedcreations.com/releases")
    }
    maven("https://repo.codemc.org/repository/maven-public/")
}

val serverType = ServerType.SPIGOT
val minecraftVersion = "1.21.10"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    implementation(project(":${serverType.loaderName}:api"))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName = "stellar-${serverType.loaderName}-$version.jar"
        outputs.upToDateWhen { false }
    }
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }
    compileJava {
        options.release = 21
    }
    runServer {
        minecraftVersion(minecraftVersion)
        perVersionFolder(true)
        acceptMojangEula()
        serverType(serverType)
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}