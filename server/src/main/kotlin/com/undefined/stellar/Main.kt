package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: Main
    }

    override fun onEnable() {
        INSTANCE = this

        StellarCommand("test")
            .addSubCommand("abc")
            .addFailureMessage("testing!")
            .addFailureExecution<Player> {
                sendMessage(it)
            }
            .register(this)
    }

}