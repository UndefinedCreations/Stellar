import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    kotlin("jvm") version "1.9.22"
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.8" apply false
}

apply(plugin = "maven-publish")
val projectGroupId = "com.undefined"
val projectVersion = "0.0.39"
val projectArtifactId = "stellar"

group = projectGroupId
version = projectVersion

val minecraftVersion = "1.20.6"

repositories {
    mavenCentral()
    mavenLocal()
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
            name = "UndefinedCreations"
            url = uri("https://repo.undefinedcreations.com/releases")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_SECRET")
            }
        }
    }

    publications {
        register<MavenPublication>("maven") {
            groupId = projectGroupId
            artifactId = projectArtifactId
            version = projectVersion

            from(components["java"])
        }
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    version = projectVersion

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
}

tasks {
    withType<ShadowJar> {
        archiveFileName.set("${project.name}-${project.version}.jar")
        archiveClassifier.set("mapped")
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}