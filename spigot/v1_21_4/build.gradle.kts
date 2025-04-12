plugins {
    id("setup")
    id("com.undefinedcreations.echo")
    `publishing-convention`
}

dependencies {
    echo("1.21.4", printDebug = true)
    compileOnly(project(":spigot:lib"))
}