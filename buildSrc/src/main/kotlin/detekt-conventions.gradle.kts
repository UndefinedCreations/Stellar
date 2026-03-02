plugins {
    id("dev.detekt")
}

detekt {
    config.setFrom(rootProject.layout.projectDirectory.file("detekt/config.yml"))
}