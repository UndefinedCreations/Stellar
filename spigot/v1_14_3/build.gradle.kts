plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.14.3", mojangMappings = false, printDebug = true)
    compileOnly(project(":spigot:lib"))
}