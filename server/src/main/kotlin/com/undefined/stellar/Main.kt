package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addStructureRotationSubCommand("test")
            .addStructureRotationExecution<Player> {
                sendMessage(it.name)
            }
            .register()
    }

}