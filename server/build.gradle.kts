import com.undefinedcreations.nova.ServerType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("setup")
    id("com.gradleup.shadow")
    id("com.undefinedcreations.nova") version "0.0.8"
}

repositories {
    maven {
        name = "undefined-releases"
        url = uri("https://repo.undefinedcreations.com/releases")
    }
    maven {
        name = "undefined-snapshots"
        url = uri("https://repo.undefinedcreations.com/snapshots")
    }
}

val serverType = "paper"

dependencies {
    compileOnly(libs.spigot)
    implementation(kotlin("reflect"))
    implementation(project(":kotlin:paper"))
    implementation(libs.kotlin.coroutines)
    implementation("com.undefined:lynx:1.1.1-SNAPSHOT")
    implementation("com.undefined:akari:0.0.2")
    implementation("net.kyori:adventure-api:4.20.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")
    implementation("net.kyori:adventure-text-minimessage:4.20.0")
    implementation(project(":$serverType:api"))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName = "server.jar"
        outputs.upToDateWhen { false }
    }
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }
    compileJava {
        options.release = 21
    }
    runServer {
        minecraftVersion("1.21.10")
        perVersionFolder(true)
        acceptMojangEula()
        if (serverType == "spigot") serverType(ServerType.SPIGOT) else serverType(ServerType.PAPERMC)
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}