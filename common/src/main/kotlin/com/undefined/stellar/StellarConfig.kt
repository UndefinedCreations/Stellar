package com.undefined.stellar

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

object StellarConfig {

    @ApiStatus.Internal
    var miniMessage: MiniMessage? = null
        private set
        get() = field ?: MiniMessage.miniMessage()
    @ApiStatus.Internal
    var prefix: String = ""
        private set
    @ApiStatus.Internal
    var plugin: JavaPlugin? = null
        private set
    @ApiStatus.Internal
    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()

    fun getStellarCommand(name: String): AbstractStellarCommand<*>? =
        commands.firstOrNull { it.name == name.substringAfter(':') || name in it.aliases }

    fun setMiniMessage(miniMessage: MiniMessage): StellarConfig = apply {
        this.miniMessage = miniMessage
    }

    fun setPrefix(prefix: String): StellarConfig = apply {
        this.prefix = prefix
    }

    fun setPlugin(plugin: JavaPlugin): StellarConfig = apply {
        this.plugin = plugin
    }

}