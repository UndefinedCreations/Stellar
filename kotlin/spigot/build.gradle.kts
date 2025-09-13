plugins {
    id("setup")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    compileOnly(project(":spigot:api"))
    compileOnly(project(":kotlin:core"))
}