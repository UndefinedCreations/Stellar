package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addTeamSubCommand("dfafd")
            .addTeamExecution<Player> {
                sendMessage(it.entries.joinToString())
                sendMessage(it.displayName)
            }
            .register()
    }

}