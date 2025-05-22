package com.undefined.stellar

import com.undefined.stellar.util.unregisterCommand
import org.bukkit.configuration.file.FileConfiguration

object Config {

    private val config: FileConfiguration = StellarPlugin.INSTANCE.config

    fun init() {
        StellarPlugin.INSTANCE.saveDefaultConfig()
        StellarConfig.setPrefix(prefix)
        for (commandName in blacklistedCommand) unregisterCommand(commandName, StellarPlugin.INSTANCE)
    }

    fun save() {
        StellarPlugin.INSTANCE.saveConfig()
    }

    var blacklistedCommand: MutableList<String>
        get() = config.getStringList("blacklisted-commands")
        set(value) = config.set("blacklisted-commands", value)

    var prefix: String
        get() = config.getString("prefix") ?: ""
        set(value) = config.set("prefix", value)
}