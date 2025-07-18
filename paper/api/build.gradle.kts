plugins {
    id("setup")
    id("com.gradleup.shadow")
}

dependencies {
    compileOnly(libs.papermc)
    compileOnly(libs.brigadier)
    api(project(":paper:lib"))
    api(project(":paper:v1_21_8"))
    api(project(":paper:v1_21_5"))
    api(project(":paper:v1_21_4"))
    api(project(":paper:v1_21_3"))
    api(project(":paper:v1_21_1"))
    api(project(":paper:v1_21"))
    api(project(":paper:v1_20_6"))
}

tasks {
    shadowJar {
        minimize {
            exclude("**/kotlin/**")
            exclude("**/intellij/**")
            exclude("**/jetbrains/**")
        }
        archiveClassifier = project.name
        archiveFileName = "${rootProject.name}-${project.version}.jar"
    }
}