package com.undefined.stellar.util

import com.undefined.stellar.NMSManager
import com.undefined.stellar.StellarCommand
import com.undefined.stellar.exception.UnsupportedVersionException
import org.bukkit.plugin.java.JavaPlugin

/**
 * Creates a new command with the specified parameters and a builder function.
 *
 * @param name The name of the command.
 * @param description The description of the command.
 * @param permissions A list of permissions required to execute the command.
 * @param aliases A list of aliases for the command.
 * @param builder A lambda function used to configure the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, description: String, permissions: List<String>, aliases: List<String>, builder: StellarCommand.() -> Unit): StellarCommand {
    val command = StellarCommand(name, permissions, aliases)
    command.setDescription(description)
    command.builder()
    return command
}

/**
 * Creates a new command with a name, description, and a builder function.
 *
 * @param name The name of the command.
 * @param description The description of the command.
 * @param builder A lambda function used to configure the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, description: String, builder: StellarCommand.() -> Unit): StellarCommand = command(name, description, listOf(), listOf(), builder)

/**
 * Creates a new command with a name, description, permissions, and a builder function.
 *
 * @param name The name of the command.
 * @param description The description of the command.
 * @param permissions A list of permissions required to execute the command.
 * @param builder A lambda function used to configure the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, description: String, permissions: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, description, permissions, listOf(), builder)

/**
 * Creates a new command with a name, permissions, and a builder function.
 *
 * @param name The name of the command.
 * @param permissions A list of permissions required to execute the command.
 * @param builder A lambda function used to configure the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, permissions: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", permissions, listOf(), builder)

/**
 * Creates a new command with a name, permissions, aliases, and a builder function.
 *
 * @param name The name of the command.
 * @param permissions A list of permissions required to execute the command.
 * @param aliases A list of aliases for the command.
 * @param builder A lambda function used to configure the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, permissions: List<String>, aliases: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", permissions, aliases, builder)

/**
 * Creates a new command with a name and a builder function.
 *
 * @param name The name of the command.
 * @param builder A lambda function used to configure the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", builder)

/**
 * Creates a new command with the specified parameters.
 *
 * @param name The name of the command.
 * @param description The description of the command.
 * @param permissions A list of permissions required to execute the command.
 * @param aliases A list of aliases for the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, description: String, permissions: List<String>, aliases: List<String>): StellarCommand {
    val command = StellarCommand(name, permissions, aliases)
    command.setDescription(description)
    return command
}

/**
 * Creates a new command with a name and description.
 *
 * @param name The name of the command.
 * @param description The description of the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, description: String): StellarCommand = command(name, description, listOf(), listOf())

/**
 * Creates a new command with a name, description, and permissions.
 *
 * @param name The name of the command.
 * @param description The description of the command.
 * @param permissions A list of permissions required to execute the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, description: String, permissions: List<String>): StellarCommand = command(name, description, permissions, listOf())

/**
 * Creates a new command with a name and permissions.
 *
 * @param name The name of the command.
 * @param permissions A list of permissions required to execute the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, permissions: List<String>): StellarCommand = command(name, "", permissions, listOf())

/**
 * Creates a new command with a name, permissions, and aliases.
 *
 * @param name The name of the command.
 * @param permissions A list of permissions required to execute the command.
 * @param aliases A list of aliases for the command.
 * @return The created StellarCommand instance.
 */
fun command(name: String, permissions: List<String>, aliases: List<String>): StellarCommand = command(name, "", permissions, aliases)

/**
 * Creates a new command with a name.
 * @return The created StellarCommand instance.
 */
fun command(name: String): StellarCommand = command(name, "")

/**
 * Unregisters a command with the given name.
 */
fun unregisterCommand(name: String) = NMSManager.unregister(name)