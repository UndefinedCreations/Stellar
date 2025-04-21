plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.18.2", printDebug = true)
    compileOnly(project(":spigot:lib"))
}