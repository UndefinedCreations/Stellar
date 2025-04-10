import com.undefinedcreations.runServer.ServerType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("setup")
    id("com.undefinedcreations.runServer") version "0.1.6"
    id("com.gradleup.shadow") version "8.3.5"
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
    compileOnly(libs.papermc)
    implementation("net.kyori:adventure-api:4.20.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")
    implementation(project(":paper:api"))
//    implementation("com.undefined:stellar:0.1.37:paper")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("Stellar-shadow.jar")
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