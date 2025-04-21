plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.19.3", printDebug = true)
    compileOnly(project(":spigot:lib"))
}