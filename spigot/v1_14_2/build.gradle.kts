plugins {
    id("setup")
    id("com.undefinedcreations.echo")
    `publishing-convention`
}

dependencies {
    echo("1.14.2", mojangMappings = false, printDebug = true)
    compileOnly(project(":spigot:lib"))
}