plugins {
    id("setup")
    `publishing-convention`
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    api(project(":paper:lib"))
    api(project(":spigot:lib"))
}

tasks {
    shadowJar {
        exclude("**/kotlin/**")
        archiveClassifier = ""
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}