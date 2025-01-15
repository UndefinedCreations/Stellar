plugins {
    kotlin("jvm") version "1.9.22"
}

val projectVersion = version
val projectGroupId = "com.undefined"
val projectArtifactId = "stellar"

//publishing {
//    publications {
//        register<MavenPublication>("maven") {
//            groupId = projectGroupId
//            artifactId = projectArtifactId
//            version = projectVersion.toString()
//
//            from(components["java"])
//        }
//    }
//}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
    api("com.mojang:authlib:1.5.21")
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