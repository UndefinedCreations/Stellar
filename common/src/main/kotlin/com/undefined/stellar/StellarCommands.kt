package com.undefined.stellar

import org.jetbrains.annotations.ApiStatus

object StellarCommands {
    @ApiStatus.Internal
    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()
    @ApiStatus.Internal
    fun getStellarCommand(command: String): AbstractStellarCommand<*>? = commands.firstOrNull { it.name == command || it.aliases.contains(command) }
}