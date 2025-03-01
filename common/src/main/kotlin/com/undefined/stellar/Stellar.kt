package com.undefined.stellar

import com.undefined.stellar.command.AbstractStellarCommand

object Stellar {
    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()
    fun getStellarCommand(command: String): AbstractStellarCommand<*>? = commands.firstOrNull { it.name == command } // TODO add aliases
}