package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin

object StellarCommands {

    val commands: MutableList<StellarCommand> = mutableListOf()
    var PLUGIN: JavaPlugin? = null

    fun getStellarCommand(command: String): StellarCommand? = commands.firstOrNull { it.name == command || it.aliases.contains(command) }

    fun initialize(plugin: JavaPlugin) {
        if (PLUGIN != null) return
        PLUGIN = plugin
    }

}