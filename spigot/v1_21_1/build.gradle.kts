plugins {
    id("setup")
    id("com.undefinedcreations.echo")
    `publishing-convention`
}

dependencies {
    echo("1.21.1", printDebug = true)
    compileOnly(project(":spigot:lib"))
}