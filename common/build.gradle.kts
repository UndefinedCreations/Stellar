 plugins {
    id("setup")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    compileOnly(libs.authlib)
    api(kotlin("reflect"))
}