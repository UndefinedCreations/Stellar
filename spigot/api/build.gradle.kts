plugins {
    id("setup")
    `publishing-convention`
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    api(project(":spigot:lib"))
    api(project(":spigot:v1_21_4"))
    api(project(":spigot:v1_21_3"))
    api(project(":spigot:v1_21_1"))
}

tasks {
    shadowJar {
        exclude("**/kotlin/**")
        archiveClassifier = "spigot"
    }
}