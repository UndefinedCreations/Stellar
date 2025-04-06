plugins {
    setup
    `publishing-convention`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    compileOnly(libs.papermc)
    compileOnly(libs.brigadier)
    api(project(":paper:lib"))
    api(project(":paper:v1_21_4", "reobf"))
}

tasks {
    shadowJar {
        exclude("**/kotlin/**")
        archiveClassifier = "paper"
    }
}