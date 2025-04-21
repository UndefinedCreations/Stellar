plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.15.1", mojangMappings = false, printDebug = true)
    compileOnly(project(":spigot:lib"))
}