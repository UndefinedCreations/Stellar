package com.undefined.stellar.util

import com.undefined.stellar.NMSManager
import com.undefined.stellar.StellarCommand

fun command(name: String, description: String, permissions: List<String>, aliases: List<String>, builder: StellarCommand.() -> Unit): StellarCommand {
    val command = StellarCommand(name, permissions, aliases)
    command.setDescription(description)
    command.builder()
    return command
}

fun command(name: String, description: String, builder: StellarCommand.() -> Unit): StellarCommand = command(name, description, listOf(), listOf(), builder)
fun command(name: String, description: String, permissions: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, description, permissions, listOf(), builder)
fun command(name: String, permissions: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", permissions, listOf(), builder)
fun command(name: String, permissions: List<String>, aliases: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", permissions, aliases, builder)
fun command(name: String, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", builder)

fun command(name: String, description: String, permissions: List<String>, aliases: List<String>): StellarCommand {
    val command = StellarCommand(name, permissions, aliases)
    command.setDescription(description)
    return command
}

fun command(name: String, description: String): StellarCommand = command(name, description, listOf(), listOf())
fun command(name: String, description: String, permissions: List<String>): StellarCommand = command(name, description, permissions, listOf())
fun command(name: String, permissions: List<String>): StellarCommand = command(name, "", permissions, listOf())
fun command(name: String, permissions: List<String>, aliases: List<String>): StellarCommand = command(name, "", permissions, aliases)
fun command(name: String): StellarCommand = command(name, "")

fun unregisterCommand(vararg names: String) {
    for (name in names) NMSManager.unregister(name)
}