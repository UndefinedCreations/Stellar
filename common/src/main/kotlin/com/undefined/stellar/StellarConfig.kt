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

    @ApiStatus.Internal
    fun getStellarCommand(name: String): AbstractStellarCommand<*>? =
        commands.firstOrNull { it.name == name.substringAfter(':') || name in it.aliases }

    /**
     * Sets the [MiniMessage] instance in [StellarConfig], which will be used a default value.
     * @return The modified [StellarConfig].
     */
    fun setMiniMessage(miniMessage: MiniMessage): StellarConfig = apply {
        this.miniMessage = miniMessage
    }

    /**
     * Sets the default prefix in [StellarConfig], which will be used a default value.
     * @return The modified [StellarConfig].
     */
    fun setPrefix(prefix: String): StellarConfig = apply {
        this.prefix = prefix
    }

    /**
     * Sets the [JavaPlugin] instance in [StellarConfig], which will be used a default value.
     * @return The modified [StellarConfig].
     */
    fun setPlugin(plugin: JavaPlugin): StellarConfig = apply {
        this.plugin = plugin
    }

}