package com.undefined.stellar

object Stellar {
    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()
    fun getStellarCommand(name: String): AbstractStellarCommand<*>? =
        commands.firstOrNull { it.name == name.substringAfter(':') || name in it.aliases }
}