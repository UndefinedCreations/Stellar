plugins {
    id("setup")
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    api(project(":spigot:lib"))
    implementation(project(":spigot:v1_21_4"))
}