plugins {
    kotlin("jvm") version "1.9.22"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

dependencies {
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
    implementation("com.mojang:brigadier:1.2.9")
    compileOnly(project(":common"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(17)
}