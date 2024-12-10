import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    kotlin("jvm") version "1.9.22"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

apply(plugin = "maven-publish")
val projectGroupId = "com.undefined"
val projectVersion = "0.0.1"
val projectArtifactId = "stellar"

group = projectGroupId
version = projectVersion

val minecraftVersion = "1.20.6"

repositories {
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "minecraft-repo"
        url = uri("https://libraries.minecraft.net/")
    }
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

publishing {
    repositories {
        maven {
            name = "UndefinedCreation"
            url = uri("https://repo.undefinedcreation.com/stellar")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_SECRET")
            }
        }
    }
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("net.kyori:adventure-api:4.17.0")
        implementation("net.kyori:adventure-platform-bukkit:4.3.4")
        implementation("net.kyori:adventure-text-minimessage:4.17.0")
        implementation("net.kyori:adventure-text-serializer-legacy:4.17.0")
    }
}

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":v1_20_6:", "reobf"))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    jar.configure {
        dependsOn("shadowJar")
        archiveClassifier.set("dev")
    }

    withType<ShadowJar> {
        archiveClassifier.set("mapped")
        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}

kotlin {
    jvmToolchain(21)
}