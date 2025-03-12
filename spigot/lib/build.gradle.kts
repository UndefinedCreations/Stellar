plugins {
    id("setup")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    api(project(":common"))
}