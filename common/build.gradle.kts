plugins {
    kotlin("jvm") version "1.9.22"
}

val versionVar = version
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

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
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    api("com.mojang:authlib:1.5.21")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        options.release.set(8)
    }
}

kotlin {
    jvmToolchain(21)
}
