plugins {
    id("setup")
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    compileOnly(libs.papermc)
    compileOnly(libs.brigadier)
    api(project(":paper:lib"))
    implementation(project(":paper:v1_21_4", "reobf"))
}