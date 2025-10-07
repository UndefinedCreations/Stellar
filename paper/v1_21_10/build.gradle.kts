plugins {
    id("setup")
    id("io.papermc.paperweight.userdev")
}

dependencies {
    paperweight.paperDevBundle("1.21.9-R0.1-SNAPSHOT") // TODO CHANGE TO 1.21.10
    compileOnly(project(":paper:lib"))
}

tasks {
    paperweight {
        javaLauncher = project.javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) }
    }
}
