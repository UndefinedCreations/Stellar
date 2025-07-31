package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarConfig.setPlugin(this)

        StellarCommand("hub")
            .addAlias("lobby")
            .addExecution<Player> {
                sender.sendMessage("Sending to hub...")
            }
            .register()
    }

}