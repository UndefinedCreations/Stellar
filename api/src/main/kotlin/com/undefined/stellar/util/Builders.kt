package com.undefined.stellar.util

import com.undefined.stellar.StellarCommand

fun command(name: String, description: String, permissions: List<String>, builder: StellarCommand.() -> Unit): StellarCommand {
    val command = StellarCommand(name, description)
    command.builder()
    return command
}

fun command(name: String, description: String, builder: StellarCommand.() -> Unit): StellarCommand = command(name, description, listOf(), builder)

fun command(name: String, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", builder)