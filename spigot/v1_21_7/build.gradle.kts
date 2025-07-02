plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.21.7", printDebug = true)
    compileOnly(project(":spigot:lib"))
}