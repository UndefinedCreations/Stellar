package com.undefined.stellar

object StellarCommands {
    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()
    fun getStellarCommand(command: String): AbstractStellarCommand<*>? = commands.firstOrNull { it.name == command || it.aliases.contains(command) }
}