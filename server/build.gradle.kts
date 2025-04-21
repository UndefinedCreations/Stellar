import com.undefinedcreations.nova.ServerType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    setup
    id("com.gradleup.shadow")
    id("com.undefinedcreations.echo") version "0.0.11"
    id("com.undefinedcreations.nova") version "0.0.4"
}

repositories {
    maven {
        name = "undefined-repo"
        url = uri("https://repo.undefinedcreations.com/releases")
    }
}

val versionVar = version
val groupIdVar = "com.undefined"

dependencies {
//    compileOnly(libs.spigot)
    echo("1.21.4")
    implementation(kotlin("reflect"))
    implementation("net.kyori:adventure-api:4.20.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")
    implementation("net.kyori:adventure-text-minimessage:4.20.0")
    implementation(project(":spigot:api"))
//    implementation("com.undefined:stellar:0.1.63")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
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