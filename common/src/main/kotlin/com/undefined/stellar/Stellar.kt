package com.undefined.stellar

import com.undefined.stellar.command.AbstractStellarCommand

object Stellar {
    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()
    fun getStellarCommand(name: String): AbstractStellarCommand<*>? {
        return commands.firstOrNull { it.name == name.substringAfter(':') || name in it.aliases }
    }
}