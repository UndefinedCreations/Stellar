plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly(project(":common"))

    compileOnly(libs.papermc)
}

kotlin {
    jvmToolchain(21)
}