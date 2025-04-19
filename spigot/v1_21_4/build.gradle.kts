import com.undefinedcreations.echo.tasks.RemapTask

plugins {
    id("setup")
    id("com.undefinedcreations.echo")
    `publishing-convention`
}

dependencies {
    echo("1.21.4", printDebug = true)
    compileOnly(project(":spigot:lib"))
}

tasks {
    remap {
        minecraftVersion("1.21.4")
        action(RemapTask.Action.MOJANG_TO_SPIGOT)
    }
}