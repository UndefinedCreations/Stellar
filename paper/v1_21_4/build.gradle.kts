plugins {
    id("setup")
    id("io.papermc.paperweight.userdev")
    `maven-publish`
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly(project(":paper:lib"))
}

tasks {
    paperweight {
        javaLauncher = project.javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) }
    }
}
