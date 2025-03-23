import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("setup")
    `publishing-convention`
    id("com.gradleup.shadow") version "8.3.5"
    id("org.jetbrains.dokka") version "2.0.0"
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)

    api(project(":paper:lib"))
    api(project(":spigot:lib"))

    dokkaPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:2.0.0")
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
    tasks.withType<DokkaTask>()
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