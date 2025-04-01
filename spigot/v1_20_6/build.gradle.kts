plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.20.6", printDebug = true)
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
