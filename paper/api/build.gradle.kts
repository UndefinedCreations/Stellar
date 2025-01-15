plugins {
    `java-library`
    kotlin("jvm") version "1.9.22"
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

val versionVar = version
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

group = groupIdVar
version = versionVar

//publishing {
//    publications {
//        register<MavenPublication>("maven") {
//            groupId = groupIdVar
//            artifactId = artifactIdVar
//            version = versionVar.toString()
//
//            from(components["java"])
//        }
//    }
//}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
    api(project(":paper:common"))
    implementation(project(":paper:v1_21_4"))
    implementation(kotlin("reflect"))
}

tasks {
    shadowJar {
        archiveClassifier = "paper"
    }
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