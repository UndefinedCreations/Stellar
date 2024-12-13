import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    kotlin("jvm") version "1.9.22"
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

apply(plugin = "maven-publish")
val projectGroupId = "com.undefined"
val projectVersion = "0.0.7"
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
            url = uri("https://repo.undefinedcreation.com/releases")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_SECRET")
            }
        }
    }
//
//    publications {
//        register<MavenPublication>("maven") {
//            groupId = projectGroupId
//            artifactId = projectArtifactId
//            version = projectVersion
//            from(components["java"])
//
////            artifact(tasks["shadowJar"]) // Use the shadow jar artifact
//
//            pom {
//                name.set("Stellar")
//                description.set("Command API for Spigot.")
//                url.set("https://undefinedcreation.com")
//
//                licenses {
//                    license {
//                        name.set("MIT License")
//                        url.set("https://opensource.org/license/mit")
//                    }
//                }
//            }
//        }
//    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    publishing {
        publications {
            register<MavenPublication>("maven") {
                groupId = projectGroupId
                artifactId = projectArtifactId
                version = projectVersion

                from(components["java"])
            }
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
    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":v1_20_6", "reobf"))
    implementation(project(":v1_21", "reobf"))
    implementation(project(":v1_21_1", "reobf"))
    implementation(project(":v1_21_3", "reobf"))
    implementation(project(":v1_21_4", "reobf"))
}

tasks {
//    assemble {
//        dependsOn(shadowJar)
//    }

//    jar.configure {
//        dependsOn("shadowJar")
//        archiveClassifier.set("dev")
//    }

    withType<ShadowJar> {
        archiveFileName.set("${project.name}-${project.version}.jar")
        archiveClassifier.set("mapped")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
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