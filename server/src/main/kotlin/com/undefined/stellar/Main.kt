package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addScoreHolderSubCommand("display")
            .addScoreHolderExecution<Player> {
                sendMessage(it)
            }
//            .addScoreHoldersSubCommand("multiple")
//            .addScoreHoldersExecution<Player> {
//                sendMessage(it.joinToString())
//            }
            .register()
    }

}