plugins {
    id("setup")
    id("com.gradleup.shadow")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    compileOnly(project(":spigot:api"))
    compileOnly(project(":kotlin:core"))
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