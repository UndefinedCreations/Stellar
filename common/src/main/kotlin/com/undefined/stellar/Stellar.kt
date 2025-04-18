package com.undefined.stellar

import net.kyori.adventure.text.minimessage.MiniMessage

object Stellar {

    var miniMessage: MiniMessage = MiniMessage.miniMessage()
        private set
    var prefix: String = ""
        private set
    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()

    fun getStellarCommand(name: String): AbstractStellarCommand<*>? =
        commands.firstOrNull { it.name == name.substringAfter(':') || name in it.aliases }

    fun setMiniMessage(miniMessage: MiniMessage): Stellar = apply {
        this.miniMessage = miniMessage
    }

    fun setPrefix(prefix: String): Stellar = apply {
        this.prefix = prefix
    }

}