package com.undefined.stellar

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

object Stellar {

    var miniMessage: MiniMessage = MiniMessage.miniMessage()
        private set
    var prefix: String = ""
        private set
    var plugin: JavaPlugin? = null
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

    fun setPlugin(plugin: JavaPlugin): Stellar = apply {
        this.plugin = plugin
    }

}