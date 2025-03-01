plugins {
    id("setup")
    id("io.papermc.paperweight.userdev")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly(project(":common"))
}

tasks {
    paperweight {
        javaLauncher = project.javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) }
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}