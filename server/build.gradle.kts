plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.gradleup.shadow)
}

dependencies {
    compileOnly(libs.papermc)
    implementation(project(":common"))
    implementation(project(":papermc"))
}

tasks {
    runServer {
        minecraftVersion("1.21.11")
    }
}

kotlin {
    jvmToolchain(21)
}