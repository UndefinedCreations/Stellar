package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin

class StellarCommand(name: String) : AbstractStellarCommand<StellarCommand>(name) {

    override fun register(plugin: JavaPlugin) = apply {
        NMSManager.register(this, plugin)
    }

}