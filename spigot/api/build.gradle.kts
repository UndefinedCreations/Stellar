plugins {
    id("setup")
    `publishing-convention`
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    api(project(":spigot:lib"))
    api(project(":spigot:v1_21_5"))
    api(project(":spigot:v1_21_4"))
    api(project(":spigot:v1_21_3"))
    api(project(":spigot:v1_21_1"))
    api(project(":spigot:v1_20_6"))
    api(project(":spigot:v1_20_4"))
    api(project(":spigot:v1_20_2"))
    api(project(":spigot:v1_20_1"))
    api(project(":spigot:v1_19_4"))
    api(project(":spigot:v1_19_3"))
    api(project(":spigot:v1_19_2"))
    api(project(":spigot:v1_18_2"))
    api(project(":spigot:v1_18_1"))
}

tasks {
    shadowJar {
        exclude("**/kotlin/**")
        archiveClassifier = "spigot"
    }
}