plugins {
    id("setup")
    id("io.papermc.paperweight.userdev")
}

dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
    compileOnly(project(":paper:lib"))
}

tasks {
    paperweight {
        javaLauncher = project.javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) }
    }
}
