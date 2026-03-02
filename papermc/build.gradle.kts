plugins {
    kotlin("jvm")
    id("detekt-conventions")
}

dependencies {
    compileOnly(project(":common"))

    compileOnly(libs.papermc)
}

kotlin {
    jvmToolchain(21)
}