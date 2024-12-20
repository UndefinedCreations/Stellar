plugins {
    kotlin("jvm") version "1.9.22"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    pluginRemapper("net.fabricmc:tiny-remapper:0.10.3:fat")
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