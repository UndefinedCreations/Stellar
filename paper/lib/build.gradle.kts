plugins {
    id("setup")
    `maven-publish`
}

dependencies {
    compileOnly(libs.papermc)
    compileOnly(libs.brigadier)
    api(project(":common"))
}