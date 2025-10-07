plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.21.9", printDebug = true) // TODO CHANGE TO 1.21.10
    compileOnly(project(":spigot:lib"))
}