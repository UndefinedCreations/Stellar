plugins {
    id("setup")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.papermc)
    compileOnly(libs.brigadier)
    api(project(":common"))
    api(project(":common_api"))
    implementation(project(":paper:v1_21_4"))
}