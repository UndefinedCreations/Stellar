plugins {
    id("setup")
    id("com.undefinedcreations.echo")
    `publishing-convention`
}

dependencies {
    echo("1.16.3", mojangMappings = false, printDebug = true)
    compileOnly(project(":spigot:lib"))
}