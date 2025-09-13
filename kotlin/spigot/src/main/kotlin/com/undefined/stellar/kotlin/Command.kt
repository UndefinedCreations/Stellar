package com.undefined.stellar.kotlin

import com.undefined.stellar.StellarConfig
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Registers a new command with a name, permissions, and a builder function.
 * @param name The name of the command.
 * @param plugin The plugin with which the command will be registered. If this is null, it will not register the command.
 * @return The created StellarCommand instance.
 */
fun command(
    name: String,
    plugin: JavaPlugin? = StellarConfig.plugin,
    builder: DSLStellarCommand.() -> Unit,
): DSLStellarCommand = DSLStellarCommand(name).apply(builder).also { command ->
    plugin?.let { command.register(plugin) }
}

fun main() { // TODO to remove
    command("test") {
        description = "test"
        aliases += "test"

        execution<Player> {
            sender.sendMessage("test")
        }
        "test" { // or literalArgument ("test")
            aliases += "test"
        }
        literalArgument("test") {

        }
    }
}