package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addTimeSubCommand("test")
            .addTimeExecution<Player> {
                sendMessage(it.seconds.toString())
            }
            .register()
    }

}