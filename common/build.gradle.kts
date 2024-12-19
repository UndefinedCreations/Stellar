plugins {
    kotlin("jvm") version "1.9.22"
}

val versionVar = version.toString()
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    api("com.mojang:authlib:1.5.21")
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = groupIdVar
            artifactId = artifactIdVar
            version = version.toString()

            from(components["java"])
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

kotlin {
    jvmToolchain(17)
}
