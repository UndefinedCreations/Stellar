plugins {
    kotlin("jvm") version "1.9.22"
    id("io.papermc.paperweight.userdev")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

dependencies {
    paperweight.paperDevBundle("1.18.1-R0.1-SNAPSHOT")
    compileOnly(project(":common"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        options.release.set(8)
    }
    paperweight {
        javaLauncher.set(
            project.javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(17)) }
        )
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}