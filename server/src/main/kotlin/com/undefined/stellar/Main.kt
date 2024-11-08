package com.undefined.stellar

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addBlockDataSubCommand("data")
            .addBlockDataExecution<Player> { predicate ->
                sendMessage(predicate.asString)
            }
            .register()
    }

}