plugins {
    id("setup")
    id("com.undefinedcreations.mapper")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.21.4-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly(project(":spigot:lib"))
}

tasks {
    jar {
        finalizedBy(remap)
    }
    remap {
        minecraftVersion("1.21.4")
    }
}
