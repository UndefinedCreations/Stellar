import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("setup")
    id("org.jetbrains.dokka") version "2.0.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14" apply false
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

java {
    withSourcesJar()
    withJavadocJar()
}