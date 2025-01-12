plugins {
    `java-library`
    kotlin("jvm") version "1.9.22"
    id("com.gradleup.shadow") version "8.3.5"
}

val versionVar = version
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

group = groupIdVar
version = versionVar

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = groupIdVar
            artifactId = artifactIdVar
            version = versionVar.toString()

            from(components["java"])
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
    implementation(project(":spigot:common"))
    implementation(project(":spigot:v1_13"))
    implementation(project(":spigot:v1_13_1"))
    implementation(project(":spigot:v1_13_2"))
    implementation(project(":spigot:v1_14_1"))
    implementation(project(":spigot:v1_14_2"))
    implementation(project(":spigot:v1_14_3"))
    implementation(project(":spigot:v1_14_4"))
    implementation(project(":spigot:v1_15"))
    implementation(project(":spigot:v1_15_1"))
    implementation(project(":spigot:v1_15_2"))
    implementation(project(":spigot:v1_16_1"))
    implementation(project(":spigot:v1_16_2"))
    implementation(project(":spigot:v1_16_3"))
    implementation(project(":spigot:v1_16_4"))
    implementation(project(":spigot:v1_16_5"))
    implementation(project(":spigot:v1_17"))
    implementation(project(":spigot:v1_17_1"))
    implementation(project(":spigot:v1_18_1"))
    implementation(project(":spigot:v1_18_2"))
    implementation(project(":spigot:v1_19_2"))
    implementation(project(":spigot:v1_19_3"))
    implementation(project(":spigot:v1_19_4"))
    implementation(project(":spigot:v1_20_1"))
    implementation(project(":spigot:v1_20_2"))
    implementation(project(":spigot:v1_20_4"))
    implementation(project(":spigot:v1_20_6"))
    implementation(project(":spigot:v1_21_1"))
    implementation(project(":spigot:v1_21_3"))
    implementation(project(":spigot:v1_21_4"))
    implementation(kotlin("reflect"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        options.release = 8
    }
}

kotlin {
    jvmToolchain(21)
}