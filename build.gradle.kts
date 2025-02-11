plugins {
    java
    kotlin("jvm") version "1.9.22"
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5"
}

apply(plugin = "maven-publish")
val projectGroupId = "com.undefined"
val projectVersion = "0.0.79"
val projectArtifactId = "stellar"

group = projectGroupId
version = projectVersion

val minecraftVersion = "1.20.6"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

publishing {
    repositories {
        maven {
            name = "UndefinedCreations"
            url = uri("https://repo.undefinedcreations.com/releases")
            credentials(PasswordCredentials::class) {
                username = project.properties["mavenUser"].toString()
                password = project.properties["mavenPassword"].toString()
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
        mavenLocal()
        maven {
            name = "minecraft-repo"
            url = uri("https://libraries.minecraft.net/")
        }
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
    api(project(":spigot:api"))
    api(project(":spigot:common"))
    api(project(":paper:api"))
}

tasks {
    publish {
        finalizedBy(project(":paper:api").tasks["publish"])
    }
    shadowJar {
        archiveClassifier = ""
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