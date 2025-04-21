plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.17.1", printDebug = true)
    compileOnly(project(":spigot:lib"))
}