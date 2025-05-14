import com.undefinedcreations.nova.ServerType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("setup")
    id("com.gradleup.shadow")
    id("com.undefinedcreations.nova") version "0.0.4"
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

val versionVar = version
val groupIdVar = "com.undefined"

dependencies {
    compileOnly(libs.spigot)
    implementation(kotlin("reflect"))
    implementation("com.undefined:lynx:0.0.22:core")
    implementation("com.undefined:lynx:0.0.22:npc")
    implementation("com.undefined:akari:0.0.2")
    implementation("com.undefined:stellar:0.1.70")
    implementation("net.kyori:adventure-api:4.20.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")
    implementation("net.kyori:adventure-text-minimessage:4.20.0")
//    implementation(project(":spigot:api"))
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
        minecraftVersion("1.21.4")
        perVersionFolder(true)
        acceptMojangEula()
        serverType(ServerType.SPIGOT)
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}