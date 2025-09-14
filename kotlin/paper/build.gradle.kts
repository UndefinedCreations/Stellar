plugins {
    id("setup")
    id("com.gradleup.shadow")
}

dependencies {
    compileOnly(libs.papermc)
    compileOnly(libs.brigadier)
    compileOnly(project(":paper:api"))

    api(project(":kotlin:core"))
}

tasks {
    shadowJar {
        minimize {
            exclude("/kotlin/**")
            exclude("**/intellij/**")
            exclude("**/jetbrains/**")
        }
        archiveClassifier = project.name
        archiveFileName = "${rootProject.name}-${project.version}.jar"
    }
}