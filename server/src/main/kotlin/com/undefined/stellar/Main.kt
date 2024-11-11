package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addEntityAnchorSubCommand("test")
            .addEntityAnchorSubCommand<Player> {
                sendMessage(it.toString())
                sendMessage(it.anchorName)
                sendMessage(it.apply(this).toString())
            }
            .register()
    }

}