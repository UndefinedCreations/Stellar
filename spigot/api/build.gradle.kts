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
}

tasks {
    shadowJar {
        exclude("**/kotlin/**")
        archiveClassifier = "spigot"
    }
}