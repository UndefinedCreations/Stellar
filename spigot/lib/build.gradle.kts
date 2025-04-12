plugins {
    id("setup")
    `publishing-convention`
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    api(project(":common"))
}