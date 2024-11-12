package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addHeightMapSubCommand("test")
            .addHeightMapExecution<Player> {
                sendMessage(it.name)
            }
            .register()
    }

}