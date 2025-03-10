plugins {
    id("setup")
    `maven-publish`
    `publishing-convention`
}

dependencies {
    compileOnly(libs.papermc)
    compileOnly(libs.brigadier)
    api(project(":common"))
}