package com.undefined.stellar.kotlin

import com.undefined.stellar.StellarCommand
import com.undefined.stellar.StellarConfig
import com.undefined.stellar.argument.LiteralArgument
import org.bukkit.plugin.java.JavaPlugin

/**
 * An extension of [StellarCommand] improved for Kotlin DSL.
 */
class KotlinStellarCommand(name: String) : StellarCommand(name) {

    /**
     * The description of this command, stored in [StellarCommand.information]. This is mostly used by the command help topic.
     */
    var description: String?
        get() = information["Description"]
        set(value) {
            information["Description"] = value
        }

    /**
     * The usage of this command, stored in [StellarCommand.information]. This is mostly used by the command help topic.
     */
    var usage: String?
        get() = information["Usage"]
        set(value) {
            information["Usage"] = value
        }

    /**
     * Creates a [LiteralArgument] with [String].
     *
     * @return The created [LiteralArgument].
     */
    operator fun String.invoke(block: LiteralArgument.() -> Unit): LiteralArgument = addLiteralArgument(name).apply(block)

}

/**
 * Registers a new command with a name, permissions, and a builder function.
 * @param name The name of the command.
 * @param plugin The plugin with which the command will be registered. If this is null, it will not register the command.
 * @return The created [StellarCommand] instance.
 */
fun command(
    name: String,
    plugin: JavaPlugin? = StellarConfig.plugin,
    builder: KotlinStellarCommand.() -> Unit,
): KotlinStellarCommand = KotlinStellarCommand(name).apply(builder).also { command ->
    plugin?.let { command.register(plugin) }
}