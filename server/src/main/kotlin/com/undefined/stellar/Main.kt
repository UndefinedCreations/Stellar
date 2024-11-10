package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addAxisSubCommand("axis")
            .addAxisExecution<Player> {
                sendMessage(it.joinToString())
            }
            .register()
    }

}