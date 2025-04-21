plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.18.1", printDebug = true)
    compileOnly(project(":spigot:lib"))
}