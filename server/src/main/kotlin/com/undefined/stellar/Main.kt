package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addRangeSubCommand("test")
            .addRangeExecution<Player> {
                sendMessage(it.toString())
                sendMessage(it.first.toString())
                sendMessage(it.last.toString())
            }
            .register()
    }

}