plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.21.8", printDebug = true)
    compileOnly(project(":spigot:lib"))
}